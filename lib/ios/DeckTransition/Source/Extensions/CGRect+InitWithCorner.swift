//
//  CGRect+InitWithCorner.swift
//  DeckTransition
//
//  Created by Harshil Shah on 17/09/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import CoreGraphics

extension CGRect {
    
    /// Initializes a rectangle using the coordinate of one of its corners and
    /// its size
    ///
    /// **Important: Values are assumed to be in UIKit's coordinate system i.e.
    /// with the origin at the top-left**
    ///
    /// - Parameters:
    ///   - corner: The corner of the rectangle whose location is known
    ///   - cornerPoint: The coordinate of the aforementioned corner
    ///   - size: The size of the rectangle to be created
    init(withCorner corner: Corner, at cornerLocation: CGPoint, size: CGSize) {
        
        /// For the left corners, the origin's x-value is the same as that of
        /// the corner. For right corners, the origin's x-value is calculated by
        /// shifting the corner left by the width of the rectangle
        let xOrigin: CGFloat = {
            switch corner {
            case .topLeft, .bottomLeft:
                return cornerLocation.x
            case .topRight, .bottomRight:
                return cornerLocation.x - size.width
            }
        }()
        
        /// For the top corners, the origin's y-value is the same as that of
        /// the corner. For bottom corners, the origin's y-value is calculated
        /// by shifting the corner up by the height of the rectangle
        let yOrigin: CGFloat = {
            switch corner {
            case .topLeft, .topRight:
                return cornerLocation.y
            case .bottomLeft, .bottomRight:
                return cornerLocation.y - size.height
            }
        }()
        
        let origin = CGPoint(x: xOrigin, y: yOrigin)
        
        self.init(origin: origin, size: size)
    }
    
}



