//
//  DeckSegue.swift
//  DeckTransition
//
//  Created by Harshil Shah on 15/10/16.
//  Copyright © 2016 Harshil Shah. All rights reserved.
//

import UIKit

/// A segue to implement the Deck transition via Storyboards
///
/// To use this, set your segue's class to `DeckSegue`, and its `kind` to
/// `custom`
public final class DeckSegue: UIStoryboardSegue {
    
    var transition: UIViewControllerTransitioningDelegate?
    
    /// Performs the visual transition for the Deck segue.
    public override func perform() {
        transition = DeckTransitioningDelegate()
        destination.transitioningDelegate = transition
        destination.modalPresentationStyle = .custom
        source.present(destination, animated: true, completion: nil)
    }

}
