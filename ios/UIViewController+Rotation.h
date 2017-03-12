//
//  UIViewController+Rotation.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 05/03/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIViewController (Rotation)


-(void)setRotation:(NSDictionary*)style;
-(UIInterfaceOrientationMask)supportedControllerOrientations;

@end
