//
//  RNNNavigationController.m
//  ReactNativeNavigation
//
//  Created by Yogev Ben David on 24/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import "RNNNavigationController.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return [self.childViewControllers.lastObject supportedInterfaceOrientations];
}

@end
