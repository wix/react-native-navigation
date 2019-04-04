//
//  InteractivePopGestureDelegate.m
//  ReactNativeNavigation
//
//  Created by Arman Dezfuli-Arjomandi on 1/10/19.
//  Copyright © 2019 Wix. All rights reserved.
//

#import "InteractivePopGestureDelegate.h"

@implementation InteractivePopGestureDelegate

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
	if (self.navigationController.navigationBarHidden && self.navigationController.viewControllers.count > 1) {
		return YES;
	} else if (!self.navigationController.navigationBarHidden && self.originalDelegate == nil) {
		return YES;
	} else {
		return [self.originalDelegate gestureRecognizer:gestureRecognizer shouldReceiveTouch:touch];
	}
}

- (BOOL)respondsToSelector:(SEL)aSelector {
	if (aSelector == @selector(gestureRecognizer:shouldReceiveTouch:)) {
		return YES;
	} else {
		return [self.originalDelegate respondsToSelector:aSelector];
	}
}

- (id)forwardingTargetForSelector:(SEL)aSelector {
	return self.originalDelegate;
}

@end
