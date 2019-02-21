//
//  DeckTransitionViewControllerProtocol.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

/// A set of methods that vend objects used to customize a DeckTransition
/// presentation's swipe-to-dismiss behaviour.
///
/// The transition has an internal heuristic to determine which `UIScrollView`
/// should be tracked for the swipe-to-dismiss gesture. However that has some
/// edge cases, which can we worked around by making your modal view controller
/// and view controllers presented by or contained within it conform to this
/// protocol.
@objc public protocol DeckTransitionViewControllerProtocol: class {
    
    /// The child view controller which contains the scroll view that should
    /// be tracked for the swipe-to-dismiss gesture.
    ///
    /// The default heuristic for searching the `UIScrollView` to track
    /// traverses only the first level of subviews of the presented view
    /// controller. As a result of this, subviews of any child view controller
    /// are not inspected.
    ///
    /// A container view controller presented using DeckTransition can
    /// implement this variable and return the child view controller which
    /// contains the scroll view to be tracked.
    ///
    /// If this variable is not implemented or is `nil`, then the container view
    /// controller's own view is searched.
    ///
    /// If this variable is implemented and is not `nil`, the container view
    /// controller's own subviews and the value returned in the
    /// `scrollViewForDeck` variable are both ignored, and the search continues
    /// within the child view controller returned here.
    @objc optional var childViewControllerForDeck: UIViewController? { get }
    
    /// The scroll view that should be tracked for Deck's swipe-to-dismiss
    /// gesture.
    ///
    /// The default heuristic for searching the `UIScrollView` to track only
    /// traverses only the first level of subviews of the presented view
    /// controller, returning the lowermost scroll view found.
    ///
    /// This is a similar heuristic to that used in `UINavigationController`
    /// (which to the best of my knowledge, is even more limited and checks only
    /// one view, the lowermost subview of the main view), however it can miss
    /// out on the intended scroll view for more complex view hierarchies.
    /// For those cases, you can implement this variable and return the
    /// `UIScrollView` instance which should be tracked.
    ///
    /// - Note: The value returned in this variable is ignored if the
    ///   `childViewControllerForDeck` variable is also implemented.
    @objc optional var scrollViewForDeck: UIScrollView { get }
    
}

