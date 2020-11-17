//
//  VIAnimatableImageView.h
//  VIAnimatableImageViewDemo
//
//  Created by Vito on 12/30/14.
//  Copyright (c) 2014 Vito. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 *  Content mode animatable image view.
 */
@interface VICMAImageView : UIView

@property(nonatomic, strong) UIImage *image;

- (instancetype)initWithImage:(UIImage *)image;

// The image presentation size
- (CGSize)presentationImageSize;

@end
