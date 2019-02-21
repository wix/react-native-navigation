//
//  CornerView.swift
//  DeckTransition
//
//  Created by Harshil Shah on 17/09/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

final class CornerView: UIView {
    
    // MARK: - Public variables
    
    var cornerRadius: CGFloat {
        get { return cornerLayer.radius }
        set { cornerLayer.radius = newValue }
    }
    
    var corner: Corner? {
        get { return cornerLayer.corner }
        set { cornerLayer.corner = newValue }
    }
    
    // MARK: - Private variables
    
    private var cornerLayer: CornerLayer {
        return layer as! CornerLayer
    }
    
    // MARK: - Layer override
    
    override class var layerClass: AnyClass {
        return CornerLayer.self
    }
    
    // MARK: - Initializers
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setup()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setup()
    }
    
    private func setup() {
        cornerLayer.fillColor = UIColor.black.cgColor
    }
    
}

private final class CornerLayer: CAShapeLayer {
    
    // MARK: - Public variables
    
    override var frame: CGRect {
        didSet { setNeedsDisplay() }
    }
    
    @NSManaged var radius: CGFloat
    
    var corner: Corner? {
        didSet { setNeedsDisplay() }
    }
    
    // MARK: - Private variables
    
    private static let radiusKey = "radius"
    
    // MARK: - Animation overrides
    
    override static func needsDisplay(forKey key: String) -> Bool {
        guard key == radiusKey else {
            return super.needsDisplay(forKey: key)
        }
        
        return true
    }
    
    override func action(forKey event: String) -> CAAction? {
        /// As best as I can tell, the only way to get all the properties
        /// related to the ongoing transition is to just copy them from the
        /// animation that is created for any random animatable property
        ///
        /// https://stackoverflow.com/questions/14192816/create-a-custom-animatable-property
        
        guard event == CornerLayer.radiusKey,
              let action = super.action(forKey: "backgroundColor") as? CAAnimation
        else {
            return super.action(forKey: event)
        }
        
        let animation = CABasicAnimation(keyPath: CornerLayer.radiusKey)
        animation.fromValue = presentation()?.value(forKey: event) ?? radius
        animation.duration = action.duration
        animation.speed = action.speed
        animation.timeOffset = action.timeOffset
        animation.repeatCount = action.repeatCount
        animation.repeatDuration = action.repeatDuration
        animation.autoreverses = action.autoreverses
        animation.fillMode = action.fillMode
        animation.timingFunction = action.timingFunction
        animation.delegate = action.delegate
        return animation
    }
    
    // MARK: - CALayer methods
    
    override func display() {
        self.path = currentPath()
    }
    
    // MARK: - Private methods
    
    private func currentPath() -> CGPath? {
        guard let corner = corner else {
            return nil
        }
        
        let side = presentation()?.radius ?? radius
        let size = CGSize(width: side, height: side)
        let targetRect = CGRect(withCorner: corner, at: bounds.getCorner(corner), size: size)
        
        /// Each corner's curved path is created by:
        ///
        /// 1. Moving to the actual corner point
        /// 2. Drawing a line along the x-axis towards the center by the same
        ///    amount as the required corner radius
        /// 3. Drawing a quadrant of the required radius with its center as one
        ///    unit along both x and y axes from the corner towards the center
        /// 4. Drawing a line back to the corner point
        let path: UIBezierPath = {
            switch corner {
                
            case .topLeft:
                let tempPath = UIBezierPath()
                tempPath.move(to: targetRect.topLeft)
                tempPath.addLine(to: targetRect.topRight)
                tempPath.addArc(withCenter: targetRect.bottomRight, radius: side, startAngle: .pi * 3/2, endAngle: .pi, clockwise: false)
                tempPath.addLine(to: targetRect.topLeft)
                return tempPath
                
            case .topRight:
                let tempPath = UIBezierPath()
                tempPath.move(to: targetRect.topRight)
                tempPath.addLine(to: targetRect.topLeft)
                tempPath.addArc(withCenter: targetRect.bottomLeft, radius: side, startAngle: .pi * 3/2, endAngle: 0, clockwise: true)
                tempPath.addLine(to: targetRect.topRight)
                return tempPath
                
            case .bottomLeft:
                let tempPath = UIBezierPath()
                tempPath.move(to: targetRect.bottomLeft)
                tempPath.addLine(to: targetRect.bottomRight)
                tempPath.addArc(withCenter: targetRect.topRight, radius: side, startAngle: .pi/2, endAngle: .pi, clockwise: true)
                tempPath.addLine(to: targetRect.bottomLeft)
                return tempPath
                
            case .bottomRight:
                let tempPath = UIBezierPath()
                tempPath.move(to: targetRect.bottomRight)
                tempPath.addLine(to: targetRect.bottomLeft)
                tempPath.addArc(withCenter: targetRect.topLeft, radius: side, startAngle: .pi/2, endAngle: 0, clockwise: false)
                tempPath.addLine(to: targetRect.bottomRight)
                return tempPath
                
            }
        }()
        
        return path.cgPath
    }
    
}
