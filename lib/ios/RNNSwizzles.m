//
//  RNNSwizzles.m
//  ReactNativeNavigation
//
//  Created by Leo Natan (Wix) on 1/17/18.
//  Copyright © 2018 Wix. All rights reserved.
//

#import "RNNSwizzles.h"
@import ObjectiveC;
@import UIKit;

static id (*__SWZ_initWithEventDispatcher_orig)(id self, SEL _cmd, id eventDispatcher);

@implementation RNNSwizzles

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_10_3
- (id)__swz_initWithEventDispatcher:(id)eventDispatcher
{
	id returnValue = __SWZ_initWithEventDispatcher_orig(self, _cmd, eventDispatcher);
	
	if (@available(iOS 11.0, *)) {
		[(UIScrollView*)[returnValue valueForKey:@"scrollView"] setContentInsetAdjustmentBehavior:UIScrollViewContentInsetAdjustmentAlways];
	}
	
	return returnValue;
}

+ (void)load
{
	Class cls = NSClassFromString(@"RCTScrollView");
	if(cls == NULL)
	{
		return;
	}
	Method m1 = class_getInstanceMethod(cls, NSSelectorFromString(@"initWithEventDispatcher:"));
	
	if(m1 == NULL)
	{
		return;
	}
	
	__SWZ_initWithEventDispatcher_orig = (void*)method_getImplementation(m1);
	Method m2 = class_getInstanceMethod([RNNSwizzles class], NSSelectorFromString(@"__swz_initWithEventDispatcher:"));
	method_exchangeImplementations(m1, m2);
}
#endif

@end
