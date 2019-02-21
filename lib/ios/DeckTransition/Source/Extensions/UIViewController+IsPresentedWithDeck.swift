//
//  UIViewController+IsPresentedWithDeck.swift
//  DeckTransition
//
//  Created by Harshil Shah on 06/12/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

extension UIViewController {
    
    /// A Boolean value indicating whether the view controller is presented
    /// using Deck.
    var isPresentedWithDeck: Bool {
        return transitioningDelegate is DeckTransitioningDelegate
            && modalPresentationStyle == .custom
            && presentingViewController != nil
    }
    
}
