//
//  UIViewController+DeckTransitionViewControllerProtocol.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

extension UITabBarController: DeckTransitionViewControllerProtocol {
    
    /// The view controller representing the selected tab is assumed to contain
    /// the `UIScrollView` to be tracked
    public var childViewControllerForDeck: UIViewController? {
        return self.selectedViewController
    }
    
}

extension UINavigationController: DeckTransitionViewControllerProtocol {
    
    /// The view controller at the top of the navigation stack is assumed to
    /// contain the `UIScrollView` to be tracked
    public var childViewControllerForDeck: UIViewController? {
        return self.topViewController
    }
    
}
