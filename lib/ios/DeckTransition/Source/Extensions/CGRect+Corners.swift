//
//  CGRect+Corners.swift
//  DeckTransition
//
//  Created by Harshil Shah on 17/09/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import CoreGraphics

extension CGRect {
    
    /// Returns the coordinates of a corner of the rectangle
    ///
    /// **Important: Values are assumed to be in UIKit's coordinate system i.e.
    /// with the origin at the top-left**
    func getCorner(_ corner: Corner) -> CGPoint {
        switch corner {
        case .topLeft: return CGPoint(x: minX, y: minY)
        case .topRight: return CGPoint(x: maxX, y: minY)
        case .bottomLeft: return CGPoint(x: minX, y: maxY)
        case .bottomRight:  return CGPoint(x: maxX, y: maxY)
        }
    }
    
    /// The coordinates of the top left corner of the rectangle in UIKit's
    /// coordinate system
    var topLeft: CGPoint {
        return getCorner(.topLeft)
    }
    
    /// The coordinates of the top right corner of the rectangle in UIKit's
    /// coordinate system
    var topRight: CGPoint {
        return getCorner(.topRight)
    }
    
    /// The coordinates of the bottom left corner of the rectangle in UIKit's
    /// coordinate system
    var bottomLeft: CGPoint {
        return getCorner(.bottomLeft)
    }
    
    /// The coordinates of the bottom right corner of the rectangle in UIKit's
    /// coordinate system
    var bottomRight: CGPoint {
        return getCorner(.bottomRight)
    }
    
}



