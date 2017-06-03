////
////  RCCDrawerHelper.m
////  ReactNativeControllers
////
////  Created by Ran Greenberg on 07/04/2016.
////  Copyright © 2016 artal. All rights reserved.
////
//
//#import "RCCDrawerHelper.h"
//
//@implementation RCCDrawerHelper
//
//static CGFloat _sideMenuWidth;
//
//+(UIButton*)createOverlayButton:(id)target {
//    UIButton *overlayButton = [[UIButton alloc] init];
//    overlayButton.backgroundColor = [UIColor clearColor];
//    [overlayButton addTarget:target action:@selector(overlayButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
//    
//    return overlayButton;
//}
//
//
//+(void)addOverlayButtonToScreen:(UIButton*)buttonToAdd
//                    contextView:(UIView*)view
//                           side:(RCCDrawerSide)side
//                  sideMenuWidth:(CGFloat)sideMenuWidth
//              animationDuration:(CGFloat)duration {
//    
//    _sideMenuWidth = sideMenuWidth;
//    
//    CGRect buttonFrame = view.bounds;
//    
//    buttonFrame.origin.x = [RCCDrawerHelper overlayButtonX:sideMenuWidth side:side];
//    
//    switch (side) {
//        case RCCDrawerSideLeft:
//        {
//            buttonFrame.origin.x = sideMenuWidth;
//        }
//            
//            break;
//        case RCCDrawerSideRight:
//        {
//            buttonFrame.origin.x = 0;
//        }
//            
//            break;
//            
//        default:
//            break;
//    }
//    
//    buttonFrame.size.width = buttonFrame.size.width - sideMenuWidth;
//    buttonToAdd.frame = buttonFrame;
//    [view addSubview:buttonToAdd];
//}
//
//+(void)overlayButtonPressed:(UIButton*)button animationDuration:(CGFloat)duration {
//    [button removeFromSuperview];
//}
//
//
//+(CGFloat)overlayButtonX:(CGFloat)sideMenuWidth side:(RCCDrawerSide)side {
//    switch (side) {
//        case RCCDrawerSideLeft:
//        {
//            return sideMenuWidth;
//        }
//            
//            break;
//        case RCCDrawerSideRight:
//        {
//            return 0;
//        }
//            
//            break;
//            
//        default:
//            break;
//    }
//}
//
//
//+ (UIImage *)imageWithImage:(UIImage *)image scaledToSize:(CGSize)newSize {
//    //UIGraphicsBeginImageContext(newSize);
//    // In next line, pass 0.0 to use the current device's pixel scaling factor (and thus account for Retina resolution).
//    // Pass 1.0 to force exact pixel size.
//    UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0);
//    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
//    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
//    UIGraphicsEndImageContext();
//    return newImage;
//}
//
//@end
