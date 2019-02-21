//
//  ScrollViewUpdater.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

/// This class is responsible for animating and managing state for a given
/// `UIScrollView`.
///
/// It has two linked jobs:
/// 1. Animate the card effect bounce when the scroll view is scrolled beyond
///    the top.
/// 2. Signal whether the scroll view can currently be dismissed, used by the
///    `DeckPresentationController` to help with handling the swipe-to-dismiss
///    pan gesture.
final class ScrollViewUpdater {

    // MARK: - Public variables

    var isDismissEnabled = false

    // MARK: - Private variables

    private weak var rootView: UIView?
    private weak var scrollView: UIScrollView?
    private var observation: NSKeyValueObservation?

    // MARK: - Initializers

    init(withRootView rootView: UIView, scrollView: UIScrollView) {
        self.rootView = rootView
        self.scrollView = scrollView
        self.observation = scrollView.observe(\.contentOffset, options: [.initial], changeHandler: { [weak self] _, _ in
            self?.scrollViewDidScroll()
        })
    }

    deinit {
        observation = nil
    }

    // MARK: - Public methods

    public func resetTransforms() {
        guard let rootView = rootView, let scrollView = scrollView else {
            return
        }

        rootView.transform = CGAffineTransform(translationX: 0, y: 0)
        scrollView.subviews.forEach {
            $0.transform = CGAffineTransform(translationX: 0, y: 0)
        }
    }

    // MARK: - Private methods

    private func scrollViewDidScroll() {
        guard let rootView = rootView, let scrollView = scrollView else {
            return
        }

        /// Since iOS 11, the "top" position of a `UIScrollView` is not when
        /// its `contentOffset.y` is 0, but when `contentOffset.y` added to it's
        /// `safeAreaInsets.top` is 0, so that is adjusted for here.
        let offset: CGFloat = {
            if #available(iOS 11, *) {
                return scrollView.contentOffset.y + scrollView.contentInset.top + scrollView.safeAreaInsets.top
            } else {
                return scrollView.contentOffset.y + scrollView.contentInset.top
            }
        }()

        /// If the `scrollView` is not at the top, then do nothing.
        /// Additionally, dismissal is not allowed.
        ///
        /// If the `scrollView` is at the top or beyond, but is decelerating,
        /// this means that it reached to the top as the result of momentum from
        /// a swipe. In these cases, in order to retain the "card" effect, we
        /// move the `rootView` and the `scrollView`'s contents to make it
        /// appear as if the entire presented card is shifting down.
        ///
        /// Lastly, if the `scrollView` is at the top or beyond and isn't
        /// decelerating, then that means that the user is panning from top to
        /// bottom and has no more space to scroll within the `scrollView`.
        /// The pan gesture which controls the dismissal is allowed to take over
        /// now, and the scrollView's natural bounce is stopped.

        if offset > 0 {
            scrollView.bounces = true
            isDismissEnabled = false
        } else {
            if scrollView.isDecelerating {
                rootView.transform = CGAffineTransform(translationX: 0, y: -offset)
                scrollView.subviews.forEach {
                    $0.transform = CGAffineTransform(translationX: 0, y: offset)
                }
            } else {
                scrollView.bounces = false
                isDismissEnabled = true
            }
        }
    }

}
