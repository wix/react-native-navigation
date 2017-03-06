//
//  UIViewController+Rotation.m
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 05/03/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import "UIViewController+Rotation.h"
#import <objc/runtime.h>

static NSString *const ORIENTATION =            @"orientation";

static NSString *const ORIENTATION_PORTRAIT =   @"portrait";
static NSString *const ORIENTATION_LANDSCAPE =  @"landscape";
static NSString *const ORIENTATION_AUTO =       @"auto";      // defualt


@interface UIViewController (Rotation)

@property (nonatomic, strong) NSString *orientation;

@end

@implementation UIViewController (Rotation)


-(NSString*)orientation {
    return objc_getAssociatedObject(self, @selector(orientation));
}

-(void)setOrientation:(NSString*)newOrientation {
    objc_setAssociatedObject(self, @selector(orientation), newOrientation, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}


#pragma mark - Public API


-(void)setRotation:(NSDictionary*)props {
    NSString *orientation = props[@"style"][ORIENTATION];
    if (!orientation) {
        orientation = props[@"appStyle"][ORIENTATION];
    }
    if (orientation) {
        self.orientation = orientation;
    }
}


-(UIInterfaceOrientationMask)supportedControllerOrientations {
    if ([self.orientation isEqualToString:ORIENTATION_PORTRAIT]) {
        return UIInterfaceOrientationMaskPortrait;
    }
    else if ([self.orientation isEqualToString:ORIENTATION_LANDSCAPE]) {
        return UIInterfaceOrientationMaskLandscape;
    }
    
    return UIInterfaceOrientationMaskAll;
}


@end
