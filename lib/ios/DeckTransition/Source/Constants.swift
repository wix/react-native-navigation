//
//  Constants.swift
//  DeckTransition
//
//  Created by Harshil Shah on 04/08/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

struct Constants {
    
    /// Default duration for present and dismiss animations when the user hasn't
    /// specified one
    static let defaultAnimationDuration: TimeInterval = 0.3
    
    /// The corner radius applied to the presenting and presented view
    /// controllers's views
    static let cornerRadius: CGFloat = 8
    
    /// The alpha value of the presented view controller's view
    static let alphaForPresentingView: CGFloat = 0.8
    
    /// As best as I can tell using my iPhone and a bunch of iOS UI templates I
    /// came across online, 8 points is the distance between the top edges of
    /// the presented and the presenting views
    static let insetForPresentedView: CGFloat = 8
    
}
