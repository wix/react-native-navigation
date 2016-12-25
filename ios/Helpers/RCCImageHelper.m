//
//  RCCImageHelper.m
//  ReactNativeNavigation
//
//  Created by Ramil Zaynetdinov on 13.12.16.
//  Copyright © 2016 artal. All rights reserved.
//

#import "RCCImageHelper.h"

@implementation RCCImageHelper

+ (UIImage *)roundedImageWithRadius:(float)radius usingImage:(UIImage *)image
{
    CGFloat imageScale = image.scale ?: [UIScreen mainScreen].scale;
    
    UIImageView *imageView = [[UIImageView alloc] initWithImage:image];
    
    UIGraphicsBeginImageContextWithOptions(imageView.bounds.size, NO, imageScale);
    
    [[UIBezierPath bezierPathWithRoundedRect:imageView.bounds
                                cornerRadius:radius] addClip];
    
    [image drawInRect:imageView.bounds];
    
    imageView.image = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return imageView.image;
}

@end
