//
//  DeckPresentationController.swift
//  DeckTransition
//
//  Created by Harshil Shah on 15/10/16.
//  Copyright © 2016 Harshil Shah. All rights reserved.
//

import UIKit

final class DeckPresentationController: UIPresentationController, UIGestureRecognizerDelegate, DeckSnapshotUpdater {

    // MARK: - Internal variables

    /// The presentation controller holds a strong reference to the
    /// transitioning delegate because `UIViewController.transitioningDelegate`
    /// is a weak property, and thus the `DeckTransitioningDelegate` would be
    /// unallocated right after the presentation animation.
    ///
    /// Since the transitioningDelegate only vends the presentation controller
    /// object and does not hold a reference to it, there is no issue of a
    /// circular dependency here.
    var transitioningDelegate: DeckTransitioningDelegate?

    // MARK: - Private variables

    private var isSwipeToDismissGestureEnabled = true
    private var pan: UIPanGestureRecognizer?
    private var scrollViewUpdater: ScrollViewUpdater?

    private let backgroundView = UIView()
    private let roundedViewForPresentingView = RoundedView()
    private let roundedViewForPresentedView = RoundedView()

    private let snapshotViewContainer = UIView()
    private var snapshotView: UIView?

    private var snapshotViewTopConstraint: NSLayoutConstraint?
    private var snapshotViewWidthConstraint: NSLayoutConstraint?
    private var snapshotViewAspectRatioConstraint: NSLayoutConstraint?

    private var presentedViewFrameObserver: NSKeyValueObservation?
    private var presentedViewTransformObserver: NSKeyValueObservation?

    private var presentAnimation: (() -> ())? = nil
    private var presentCompletion: ((Bool) -> ())? = nil
    private var dismissAnimation: (() -> ())? = nil
    private var dismissCompletion: ((Bool) -> ())? = nil

    // MARK: - Initializers

    convenience init(presentedViewController: UIViewController,
                     presenting presentingViewController: UIViewController?,
                     isSwipeToDismissGestureEnabled: Bool,
                     presentAnimation: (() -> ())? = nil,
                     presentCompletion: ((Bool) ->())? = nil,
                     dismissAnimation: (() -> ())? = nil,
                     dismissCompletion: ((Bool) -> ())? = nil) {
        self.init(presentedViewController: presentedViewController,
                  presenting: presentingViewController)

        self.isSwipeToDismissGestureEnabled = isSwipeToDismissGestureEnabled
        self.presentAnimation = presentAnimation
        self.presentCompletion = presentCompletion
        self.dismissAnimation = dismissAnimation
        self.dismissCompletion = dismissCompletion

        NotificationCenter.default.addObserver(self, selector: #selector(updateForStatusBar), name: UIApplication.didChangeStatusBarFrameNotification, object: nil)
    }

    // MARK: - Public methods

    public func requestPresentedViewSnapshotUpdate() {
        updateSnapshotView()
    }

    // MARK: - Sizing

    private var statusBarHeight: CGFloat {
        return UIApplication.shared.statusBarFrame.height
    }

    private var scaleForPresentingView: CGFloat {
        guard let containerView = containerView else {
            return 0
        }

        return 1 - (ManualLayout.presentingViewTopInset * 2 / containerView.frame.height)
    }

    override var frameOfPresentedViewInContainerView: CGRect {
        guard let containerView = containerView else {
            return .zero
        }

        let yOffset = ManualLayout.presentingViewTopInset + Constants.insetForPresentedView

        return CGRect(x: 0,
                      y: yOffset,
                      width: containerView.bounds.width,
                      height: containerView.bounds.height - yOffset)
    }

    // MARK: - Presentation

    override func presentationTransitionWillBegin() {
        guard let containerView = containerView, let window = containerView.window else {
            return
        }

        /// A CGRect to be used as a proxy for the frame of the presentingView
        ///
        /// The actual frame isn't used directly because in the case of the
        /// double height status bar on non-X iPhones, the containerView has a
        /// reduced height
        let initialFrame: CGRect = {
            if presentingViewController.isPresentedWithDeck {
                return presentingViewController.view.frame
            } else {
                return containerView.bounds
            }
        }()

        /// The presented view's rounded view's frame is updated using KVO
        roundedViewForPresentedView.translatesAutoresizingMaskIntoConstraints = false
        containerView.addSubview(roundedViewForPresentedView)
        setupPresentedViewKVO()

        /// The snapshot view initially has the same frame as the presentingView
        containerView.insertSubview(snapshotViewContainer, belowSubview: presentedViewController.view)
        snapshotViewContainer.frame = initialFrame
        updateSnapshotView()

        /// The following transforms are performed on the snapshot view:
        /// 1. It's frame's origin is reset to 0. This is done because for
        ///    recursive Deck modals, the reference frame will not have its
        ///    origin at `.zero`
        /// 2. It is translated down by `ManualLayout.presentingViewTopInset`
        ///    points This is the desired inset from the top of the
        ///    containerView
        /// 3. It is scaled down by `scaleForPresentingView` along both axes,
        ///    such that it's top edge is at the same position. In order to do
        ///    this, we translate it up by half it's height, perform the
        ///    scaling, and then translate it back down by the same amount
        ///
        /// On versions of iOS before 11, applying a transform to the parent
        /// view modifies the frame of subviews immediately to the model value,
        /// so this transform is applied directly to the snapshot view and not
        /// the snapshot container view
        ///
        /// Note: For some reason, this behaviour only happens in the
        /// presentation phase; constraints animate as expected on iOS <11 in
        /// the dismissal 🤷🏽‍♂️
        let transformForSnapshotView = CGAffineTransform.identity
            .translatedBy(x: 0, y: -snapshotViewContainer.frame.origin.y)
            .translatedBy(x: 0, y: ManualLayout.presentingViewTopInset)
            .translatedBy(x: 0, y: -snapshotViewContainer.frame.height / 2)
            .scaledBy(x: scaleForPresentingView, y: scaleForPresentingView)
            .translatedBy(x: 0, y: snapshotViewContainer.frame.height / 2)

        /// For a recursive modal, the `presentingView` already has rounded
        /// corners so the animation must respect that
        roundedViewForPresentingView.backgroundColor = UIColor.black.withAlphaComponent(0)
        roundedViewForPresentingView.cornerRadius = presentingViewController.isPresentedWithDeck ? Constants.cornerRadius : 0
        containerView.insertSubview(roundedViewForPresentingView, aboveSubview: snapshotViewContainer)
        roundedViewForPresentingView.frame = initialFrame

        /// The background view is used to cover up the `presentedView`
        backgroundView.backgroundColor = .black
        backgroundView.translatesAutoresizingMaskIntoConstraints = false
        containerView.insertSubview(backgroundView, belowSubview: snapshotViewContainer)

        NSLayoutConstraint.activate([
            backgroundView.topAnchor.constraint(equalTo: window.topAnchor),
            backgroundView.leftAnchor.constraint(equalTo: window.leftAnchor),
            backgroundView.rightAnchor.constraint(equalTo: window.rightAnchor),
            backgroundView.bottomAnchor.constraint(equalTo: window.bottomAnchor)
        ])

        /// A snapshot view is used to represent the hierarchy of cards in the
        /// case of recursive presentation
        var rootSnapshotView: UIView?
        var rootSnapshotRoundedView: RoundedView?

        if presentingViewController.isPresentedWithDeck {
            guard let rootController = presentingViewController.presentingViewController,
                  let snapshotView = rootController.view.snapshotView(afterScreenUpdates: false)
            else {
                return
            }

            containerView.insertSubview(snapshotView, aboveSubview: backgroundView)
            snapshotView.frame = initialFrame
            snapshotView.transform = transformForSnapshotView
            snapshotView.alpha = Constants.alphaForPresentingView
            rootSnapshotView = snapshotView

            let snapshotRoundedView = RoundedView()
            snapshotRoundedView.cornerRadius = Constants.cornerRadius
            containerView.insertSubview(snapshotRoundedView, aboveSubview: snapshotView)
            snapshotRoundedView.frame = initialFrame
            snapshotRoundedView.transform = transformForSnapshotView
            rootSnapshotRoundedView = snapshotRoundedView
        }

        presentedViewController.transitionCoordinator?.animate(
            alongsideTransition: { [weak self] context in
                guard let `self` = self else {
                    return
                }

                self.presentAnimation?()
                self.snapshotView?.transform = transformForSnapshotView
                self.roundedViewForPresentingView.cornerRadius = Constants.cornerRadius
                self.roundedViewForPresentingView.transform = transformForSnapshotView
                self.roundedViewForPresentingView.backgroundColor = UIColor.black.withAlphaComponent(1 - Constants.alphaForPresentingView)
            }, completion: { _ in
                self.snapshotView?.transform = .identity
                rootSnapshotView?.removeFromSuperview()
                rootSnapshotRoundedView?.removeFromSuperview()
            }
        )
    }

    /// Method to ensure the layout is as required at the end of the
    /// presentation. This is required in case the modal is presented without
    /// animation.
    ///
    /// The various layout related functions performed by this method are:
    /// - Ensure that the view is in the same state as it would be after
    ///   animated presentation
    /// - Create and add the `presentingViewSnapshotView` to the view hierarchy
    /// - Add a black background view to present to complete cover the
    ///   `presentingViewController`'s view
    /// - Reset the `presentingViewController`'s view's `transform` so that
    ///   further layout updates (such as status bar update) do not break the
    ///   transform
    ///
    /// It also sets up the gesture recognizer to handle dismissal of the modal
    /// view controller by panning downwards
    override func presentationTransitionDidEnd(_ completed: Bool) {
        guard let containerView = containerView else {
            return
        }

        presentedViewController.view.frame = frameOfPresentedViewInContainerView

        snapshotViewContainer.transform = .identity
        snapshotViewContainer.translatesAutoresizingMaskIntoConstraints = false
        snapshotViewContainer.centerXAnchor.constraint(equalTo: containerView.centerXAnchor).isActive = true
        updateSnapshotViewAspectRatio()

        roundedViewForPresentingView.transform = .identity
        roundedViewForPresentingView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            roundedViewForPresentingView.topAnchor.constraint(equalTo: snapshotViewContainer.topAnchor),
            roundedViewForPresentingView.leftAnchor.constraint(equalTo: snapshotViewContainer.leftAnchor),
            roundedViewForPresentingView.rightAnchor.constraint(equalTo: snapshotViewContainer.rightAnchor),
            roundedViewForPresentingView.bottomAnchor.constraint(equalTo: snapshotViewContainer.bottomAnchor)
        ])

        if isSwipeToDismissGestureEnabled {
            pan = UIPanGestureRecognizer(target: self, action: #selector(handlePan))
            pan!.delegate = self
            pan!.maximumNumberOfTouches = 1
            pan!.cancelsTouchesInView = false
            presentedViewController.view.addGestureRecognizer(pan!)
        }

        presentCompletion?(completed)
    }

    // MARK: - Layout update methods

    /// This method updates the aspect ratio of the snapshot view
    ///
    /// The `snapshotView`'s aspect ratio needs to be updated here because even
    /// though it is updated with the `snapshotView` in `viewWillTransition:`,
    /// the transition is janky unless it's updated before, hence it's performed
    /// here as well, It's also an inexpensive method since constraints are
    /// modified only when a change is actually needed
    override func containerViewWillLayoutSubviews() {
        super.containerViewWillLayoutSubviews()

        guard let containerView = containerView else {
            return
        }

        updateSnapshotViewAspectRatio()
        containerView.bringSubviewToFront(roundedViewForPresentedView)

        if presentedViewController.view.isDescendant(of: containerView) {
            UIView.animate(withDuration: 0.1) { [weak self] in
                guard let `self` = self else {
                    return
                }

                self.presentedViewController.view.frame = self.frameOfPresentedViewInContainerView
            }
        }
    }

    /// Method to handle the modal setup's response to a change in
    /// orientation, size, etc.
    ///
    /// Everything else is handled by AutoLayout or `willLayoutSubviews`; the
    /// express purpose of this method is to update the snapshot view since that
    /// is a relatively expensive operation and only makes sense on orientation
    /// change
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)

        coordinator.animate(
            alongsideTransition: nil,
            completion: { [weak self] _ in
                self?.updateSnapshotViewAspectRatio()
                self?.updateSnapshotView()
            }
        )
    }

    /// Method to handle updating the view when the status bar's height changes
    ///
    /// The `containerView`'s frame is always supposed to be the go 20 pixels
    /// or 1 normal status bar height under the status bar itself, even when the
    /// status bar is of double height, to retain consistency with the system's
    /// default behaviour
    ///
    /// The containerView is the only thing that received layout updates;
    /// AutoLayout and the snapshotView method handle the rest. Additionally,
    /// the mask for the `presentedViewController` is also reset
    @objc private func updateForStatusBar() {
        guard let containerView = containerView else {
            return
        }

        /// The `presentingViewController.view` often animated "before" the mask
        /// view that should fully cover it, so it's hidden before altering the
        /// view hierarchy, and then revealed after the animations are finished
        presentingViewController.view.alpha = 0

        let fullHeight = containerView.window!.frame.size.height

        let currentHeight = containerView.frame.height
        let newHeight = fullHeight - ManualLayout.containerViewTopInset

        UIView.animate(
            withDuration: 0.1,
            animations: {
                containerView.frame.origin.y -= newHeight - currentHeight
            }, completion: { [weak self] _ in
                self?.presentingViewController.view.alpha = 1
                containerView.frame = CGRect(x: 0, y: ManualLayout.containerViewTopInset, width: containerView.frame.width, height: newHeight)
                self?.updateSnapshotView()
            }
        )
    }

    // MARK: - Snapshot view update methods

    /// Method to update the snapshot view showing a representation of the
    /// `presentingViewController`'s view
    ///
    /// The method can only be fired when the snapshot view has been set up, and
    /// then only when the width of the container is updated
    ///
    /// It resets the aspect ratio constraint for the snapshot view first, and
    /// then generates a new snapshot of the `presentingViewController`'s view,
    /// and then replaces the existing snapshot with it
    private func updateSnapshotView() {
        guard let currentSnapshotView = presentingViewController.view.snapshotView(afterScreenUpdates: true) else {
            return
        }

        snapshotView?.removeFromSuperview()

        currentSnapshotView.translatesAutoresizingMaskIntoConstraints = false
        snapshotViewContainer.addSubview(currentSnapshotView)
        NSLayoutConstraint.activate([
            currentSnapshotView.topAnchor.constraint(equalTo: snapshotViewContainer.topAnchor),
            currentSnapshotView.leftAnchor.constraint(equalTo: snapshotViewContainer.leftAnchor),
            currentSnapshotView.rightAnchor.constraint(equalTo: snapshotViewContainer.rightAnchor),
            currentSnapshotView.bottomAnchor.constraint(equalTo: snapshotViewContainer.bottomAnchor)
        ])

        snapshotView = currentSnapshotView
    }

    /// Thie method updates the aspect ratio and the height of the snapshot view
    /// used to represent the presenting view controller.
    ///
    /// The aspect ratio is only updated when the width of the container changes
    /// i.e. when just the status bar moves, nothing happens
    private func updateSnapshotViewAspectRatio() {
        guard let containerView = containerView,
              snapshotViewContainer.translatesAutoresizingMaskIntoConstraints == false
        else {
            return
        }

        snapshotViewTopConstraint?.isActive = false
        snapshotViewWidthConstraint?.isActive = false
        snapshotViewAspectRatioConstraint?.isActive = false

        let snapshotReferenceSize = presentingViewController.view.frame.size

        let topInset = ManualLayout.presentingViewTopInset

        let aspectRatio = snapshotReferenceSize.width / snapshotReferenceSize.height

        roundedViewForPresentingView.cornerRadius = Constants.cornerRadius * scaleForPresentingView

        snapshotViewTopConstraint = snapshotViewContainer.topAnchor.constraint(equalTo: containerView.topAnchor, constant: topInset)
        snapshotViewWidthConstraint = snapshotViewContainer.widthAnchor.constraint(equalTo: containerView.widthAnchor, multiplier: scaleForPresentingView)
        snapshotViewAspectRatioConstraint = snapshotViewContainer.widthAnchor.constraint(equalTo: snapshotViewContainer.heightAnchor, multiplier: aspectRatio)

        snapshotViewTopConstraint?.isActive = true
        snapshotViewWidthConstraint?.isActive = true
        snapshotViewAspectRatioConstraint?.isActive = true
    }

    // MARK: - Presented view KVO + Rounded view update methods

    private func setupPresentedViewKVO() {
        presentedViewFrameObserver = presentedViewController.view.observe(\.frame, options: [.initial]) { [weak self] _, _ in
            self?.presentedViewWasUpdated()
        }

        presentedViewTransformObserver = presentedViewController.view.observe(\.transform, options: [.initial]) { [weak self] _, _ in
            self?.presentedViewWasUpdated()
        }
    }

    private func invalidatePresentedViewKVO() {
        presentedViewFrameObserver = nil
        presentedViewTransformObserver = nil
    }

    private func presentedViewWasUpdated() {
        let offset = presentedViewController.view.frame.origin.y
        roundedViewForPresentedView.frame = CGRect(x: 0, y: offset, width: containerView!.bounds.width, height: Constants.cornerRadius)
    }

    // MARK: - Dismissal

    /// Method to prepare the view hirarchy for the dismissal animation
    ///
    /// The stuff with snapshots and the black background should be invisible to
    /// the dismissal animation, so this method effectively removes them and
    /// restores the state of the `presentingViewController`'s view to the
    /// expected state at the end of the presenting animation
    override func dismissalTransitionWillBegin() {
        guard let containerView = containerView else {
            return
        }

        let initialFrame: CGRect = {
            if presentingViewController.isPresentedWithDeck {
                return presentingViewController.view.frame
            } else {
                return containerView.bounds
            }
        }()

        let initialTransform = CGAffineTransform.identity
            .translatedBy(x: 0, y: -initialFrame.origin.y)
            .translatedBy(x: 0, y: ManualLayout.presentingViewTopInset)
            .translatedBy(x: 0, y: -initialFrame.height / 2)
            .scaledBy(x: scaleForPresentingView, y: scaleForPresentingView)
            .translatedBy(x: 0, y: initialFrame.height / 2)

        roundedViewForPresentingView.translatesAutoresizingMaskIntoConstraints = true
        roundedViewForPresentingView.frame = initialFrame
        roundedViewForPresentingView.transform = initialTransform

        snapshotViewTopConstraint?.isActive = false
        snapshotViewWidthConstraint?.isActive = false
        snapshotViewAspectRatioConstraint?.isActive = false
        snapshotViewContainer.translatesAutoresizingMaskIntoConstraints = true
        snapshotViewContainer.frame = initialFrame
        snapshotViewContainer.transform = initialTransform

        let finalCornerRadius = presentingViewController.isPresentedWithDeck ? Constants.cornerRadius : 0
        let finalTransform: CGAffineTransform = .identity

        var rootSnapshotView: UIView?
        var rootSnapshotRoundedView: RoundedView?

        if presentingViewController.isPresentedWithDeck {
            guard let rootController = presentingViewController.presentingViewController,
                  let snapshotView = rootController.view.snapshotView(afterScreenUpdates: false)
            else {
                return
            }

            containerView.insertSubview(snapshotView, aboveSubview: backgroundView)
            snapshotView.frame = initialFrame
            snapshotView.transform = initialTransform
            rootSnapshotView = snapshotView

            let snapshotRoundedView = RoundedView()
            snapshotRoundedView.cornerRadius = Constants.cornerRadius
            snapshotRoundedView.backgroundColor = UIColor.black.withAlphaComponent(1 - Constants.alphaForPresentingView)
            containerView.insertSubview(snapshotRoundedView, aboveSubview: snapshotView)
            snapshotRoundedView.frame = initialFrame
            snapshotRoundedView.transform = initialTransform
            rootSnapshotRoundedView = snapshotRoundedView
        }

        presentedViewController.transitionCoordinator?.animate(
            alongsideTransition: { [weak self] context in
                guard let `self` = self else {
                    return
                }

                self.dismissAnimation?()
                self.snapshotViewContainer.transform = finalTransform
                self.roundedViewForPresentingView.transform = finalTransform
                self.roundedViewForPresentingView.cornerRadius = finalCornerRadius
                self.roundedViewForPresentingView.backgroundColor = .clear
            }, completion: { _ in
                rootSnapshotView?.removeFromSuperview()
                rootSnapshotRoundedView?.removeFromSuperview()
            }
        )
    }

    /// Method to ensure the layout is as required at the end of the dismissal.
    /// This is required in case the modal is dismissed without animation.
    override func dismissalTransitionDidEnd(_ completed: Bool) {
        guard let containerView = containerView else {
            return
        }

        backgroundView.removeFromSuperview()
        snapshotView?.removeFromSuperview()
        snapshotViewContainer.removeFromSuperview()
        roundedViewForPresentingView.removeFromSuperview()

        let offscreenFrame = CGRect(x: 0, y: containerView.bounds.height, width: containerView.bounds.width, height: containerView.bounds.height)
        presentedViewController.view.frame = offscreenFrame
        presentedViewController.view.transform = .identity

        invalidatePresentedViewKVO()

        dismissCompletion?(completed)
    }

    // MARK: - Gesture handling

    private func isSwipeToDismissAllowed() -> Bool {
        guard let updater = scrollViewUpdater else {
            return isSwipeToDismissGestureEnabled
        }

        return updater.isDismissEnabled
    }

    @objc private func handlePan(gestureRecognizer: UIPanGestureRecognizer) {
        guard gestureRecognizer.isEqual(pan), isSwipeToDismissGestureEnabled else {
            return
        }

        switch gestureRecognizer.state {

        case .began:
            let detector = ScrollViewDetector(withViewController: presentedViewController)
            if let scrollView = detector.scrollView {
                scrollViewUpdater = ScrollViewUpdater(
                    withRootView: presentedViewController.view,
                    scrollView: scrollView)
            }
            gestureRecognizer.setTranslation(CGPoint(x: 0, y: 0), in: containerView)

        case .changed:
            if isSwipeToDismissAllowed() {
                let translation = gestureRecognizer.translation(in: presentedView)
                updatePresentedViewForTranslation(inVerticalDirection: translation.y)
            } else {
                gestureRecognizer.setTranslation(.zero, in: presentedView)
            }

        case .ended:
            UIView.animate(
                withDuration: 0.25,
                animations: {
                    self.presentedView?.transform = .identity
                    self.scrollViewUpdater?.resetTransforms()
                })
            scrollViewUpdater = nil

        default: break

        }
    }

    /// Method to update the modal view for a particular amount of translation
    /// by panning in the vertical direction.
    ///
    /// The translation of the modal view is proportional to the panning
    /// distance until the `elasticThreshold`, after which it increases at a
    /// slower rate, given by `elasticFactor`, to indicate that the
    /// `dismissThreshold` is nearing.
    ///
    /// Once the `dismissThreshold` is reached, the modal view controller is
    /// dismissed.
    ///
    /// - parameter translation: The translation of the user's pan gesture in
    ///   the container view in the vertical direction
    private func updatePresentedViewForTranslation(inVerticalDirection translation: CGFloat) {

        let elasticThreshold: CGFloat = 120
        let dismissThreshold: CGFloat = 240

        let translationFactor: CGFloat = 1/2

        /// Nothing happens if the pan gesture is performed from bottom
        /// to top i.e. if the translation is negative
        if translation >= 0 {
            let translationForModal: CGFloat = {
                if translation >= elasticThreshold {
                    let frictionLength = translation - elasticThreshold
                    let frictionTranslation = 30 * atan(frictionLength/120) + frictionLength/10
                    return frictionTranslation + (elasticThreshold * translationFactor)
                } else {
                    return translation * translationFactor
                }
            }()

            presentedView?.transform = CGAffineTransform(translationX: 0, y: translationForModal)

            if translation >= dismissThreshold {
                presentedViewController.dismiss(animated: true, completion: nil)
            }
        }
    }

    // MARK: - UIGestureRecognizerDelegate methods

    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        guard gestureRecognizer.isEqual(pan) else {
            return false
        }

        return true
    }

}
