//
//  ScrollViewDetector.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

/// An encapsulation for the logic used to parse a view controller hierarchy
/// and detect the scroll view to be tracked for the swipe-to-dismiss gesture
final class ScrollViewDetector {

    // MARK: - Public variables

    weak var scrollView: UIScrollView?

    // MARK: - Initializers

    init(withViewController viewController: UIViewController) {
        let topViewController = getVisibleViewController(fromViewController: viewController)
        self.scrollView = getScrollView(fromViewController: topViewController)
    }

    // MARK: - Private methods

    /// Returns the view controller whose `view` should be searched for the
    /// `UIScrollView` to track.
    ///
    /// - Parameter viewController: The view controller from which the search
    ///   should begin.
    /// - Returns: The view controller whose `view` must be searched.
    private func getVisibleViewController(fromViewController viewController: UIViewController) -> UIViewController {
        guard let deckViewController = viewController as? DeckTransitionViewControllerProtocol,
              let childViewController = deckViewController.childViewControllerForDeck as? UIViewController
        else {
            return viewController
        }

        return getVisibleViewController(fromViewController: childViewController)
    }

    private func findChildScrollView(in view: UIView) -> UIScrollView? {
        if let scroll = view as? UIScrollView { return scroll }

        for subview in view.subviews {
            if let scrollView = subview as? UIScrollView {
                return scrollView
            }

            if let scrollView = findChildScrollView(in: subview) {
                return scrollView
            }
        }

        return nil
    }

    /// Returns the `UIScrollView` which should be tracked.
    ///
    /// - Parameter viewController: The view controller whose view hierarchy
    ///   must be searched.
    /// - Returns: The scrollView specified in the
    ///   `DeckTransitionViewControllerProtocol` implementation if one exists,
    ///   failing which the lowermost `UIScrollView` in the view's top level
    ///   subviews, or nil if one isn't found.
    private func getScrollView(fromViewController viewController: UIViewController) -> UIScrollView? {
        if let deckViewController = viewController as? DeckTransitionViewControllerProtocol,
           let scrollView = deckViewController.scrollViewForDeck {
            return scrollView
        }

        if let scrollView = viewController.view as? UIScrollView {
            return scrollView
        }

        return findChildScrollView(in: viewController.view)
    }

}
