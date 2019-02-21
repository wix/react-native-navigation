//
//  RoundedView.swift
//  DeckTransition
//
//  Created by Harshil Shah on 05/08/17.
//  Copyright © 2017 Harshil Shah. All rights reserved.
//

import UIKit

final class RoundedView: UIView {
    
    // MARK: - Public variables
    
    public var cornerRadius = Constants.cornerRadius {
        didSet {
            leftCorner.cornerRadius = cornerRadius
            rightCorner.cornerRadius = cornerRadius
        }
    }
    
    // MARK: - Private variables
    
    private let leftCorner = CornerView()
    private let rightCorner = CornerView()
    
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
        leftCorner.corner = .topLeft
        rightCorner.corner = .topRight
        
        leftCorner.cornerRadius = cornerRadius
        rightCorner.cornerRadius = cornerRadius
        
        leftCorner.translatesAutoresizingMaskIntoConstraints = false
        rightCorner.translatesAutoresizingMaskIntoConstraints = false
        
        addSubview(leftCorner)
        addSubview(rightCorner)
    }
    
    // MARK: - UIView methods
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        leftCorner.frame = bounds
        rightCorner.frame = bounds
    }
    
}
