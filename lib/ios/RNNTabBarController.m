//
//  RNNTabBarController.m
//  ReactNativeNavigation
//
//  Created by Yogev Ben David on 24/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import "RNNTabBarController.h"

@implementation RNNTabBarController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return [self.selectedViewController supportedInterfaceOrientations];
}

@end
