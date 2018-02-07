//
//  RNNSwizzles.m
//  ReactNativeNavigation
//
//  Created by Leo Natan (Wix) on 1/17/18.
//  Copyright © 2018 artal. All rights reserved.
//

#import "RNNSwizzles.h"
@import ObjectiveC;
@import UIKit;

@implementation RNNSwizzles

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_10_3
static id (*__SWZ_initWithEventDispatcher_orig)(id self, SEL _cmd, id eventDispatcher);

- (id)__swz_initWithEventDispatcher:(id)eventDispatcher
{
	id returnValue = __SWZ_initWithEventDispatcher_orig(self, _cmd, eventDispatcher);
	
	if (@available(iOS 11.0, *)) {
		[(UIScrollView*)[returnValue valueForKey:@"scrollView"] setContentInsetAdjustmentBehavior:UIScrollViewContentInsetAdjustmentAlways];
	}
	
	return returnValue;
}
#endif

+ (void)applySwizzles
{
#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_10_3
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
#endif
}

@end


#if defined(__IPHONE_OS_VERSION_MAX_ALLOWED) && __IPHONE_OS_VERSION_MAX_ALLOWED >= 110000 /* __IPHONE_11_0 */

@interface UIView (OS10SafeAreaSupport) @end

@implementation UIView (OS10SafeAreaSupport)

- (UIEdgeInsets)_ln_safeAreaInsets
{
	static NSString* const b64 = @"X3ZpZXdDb250cm9sbGVyRm9yQW5jZXN0b3I=";
	UIViewController* vc = [self valueForKey:[[NSString alloc] initWithData:[[NSData alloc] initWithBase64EncodedString:b64 options:0] encoding:NSUTF8StringEncoding]];
	if(vc == nil)
	{
		return UIEdgeInsetsZero;
	}
	
	CGRect myFrameInVCView = [vc.view convertRect:self.bounds fromView:self];
	
	UIEdgeInsets rv = UIEdgeInsetsZero;
	rv.top = CGRectIntersection(myFrameInVCView, CGRectMake(0, 0, vc.view.bounds.size.width, vc.topLayoutGuide.length)).size.height;
	rv.bottom = CGRectIntersection(myFrameInVCView, CGRectMake(0, vc.view.bounds.size.height - vc.bottomLayoutGuide.length, vc.view.bounds.size.width, vc.bottomLayoutGuide.length)).size.height;
	
	return rv;
}

- (void)_ln_triggerSafeAreaInsetsDidChange
{
	if([self respondsToSelector:@selector(safeAreaInsetsDidChange)])
	{
		[self performSelector:@selector(safeAreaInsetsDidChange)];
	}
}

- (void)_ln_layoutSubviews
{
	[self _ln_triggerSafeAreaInsetsDidChange];
	
	struct objc_super super = {.receiver = self, .super_class = class_getSuperclass(object_getClass(self))};
	void (*super_class)(struct objc_super*, SEL) = (void*)objc_msgSendSuper;
	super_class(&super, _cmd);
}

- (void)_ln_setFrame:(CGRect)frame
{
	[self _ln_triggerSafeAreaInsetsDidChange];
	
	struct objc_super super = {.receiver = self, .super_class = class_getSuperclass(object_getClass(self))};
	void (*super_class)(struct objc_super*, SEL, CGRect) = (void*)objc_msgSendSuper;
	super_class(&super, _cmd, frame);
}

- (void)_ln_setCenter:(CGPoint)center
{
	[self _ln_triggerSafeAreaInsetsDidChange];
	
	struct objc_super super = {.receiver = self, .super_class = class_getSuperclass(object_getClass(self))};
	void (*super_class)(struct objc_super*, SEL, CGPoint) = (void*)objc_msgSendSuper;
	super_class(&super, _cmd, center);
}

- (void)_ln_setBounds:(CGRect)bounds
{
	[self _ln_triggerSafeAreaInsetsDidChange];
	
	struct objc_super super = {.receiver = self, .super_class = class_getSuperclass(object_getClass(self))};
	void (*super_class)(struct objc_super*, SEL, CGRect) = (void*)objc_msgSendSuper;
	super_class(&super, _cmd, bounds);
}

+ (void)load
{
	static dispatch_once_t onceToken;
	dispatch_once(&onceToken, ^{
		if(NSProcessInfo.processInfo.operatingSystemVersion.majorVersion < 11)
		{
			Class cls = NSClassFromString(@"RCTSafeAreaView");
			if(cls == NULL)
			{
				return;
			}
			
			Method m = class_getInstanceMethod([UIView class], @selector(_ln_safeAreaInsets));
			if(NO == class_addMethod(cls, @selector(safeAreaInsets), method_getImplementation(m), method_getTypeEncoding(m)))
			{
				return;
			}
			
			m = class_getInstanceMethod([UIView class], @selector(_ln_layoutSubviews));
			if(NO == class_addMethod(cls, @selector(layoutSubviews), method_getImplementation(m), method_getTypeEncoding(m)))
			{
				return;
			}
			
			m = class_getInstanceMethod([UIView class], @selector(_ln_setFrame:));
			if(NO == class_addMethod(cls, @selector(setFrame:), method_getImplementation(m), method_getTypeEncoding(m)))
			{
				return;
			}
			
			m = class_getInstanceMethod([UIView class], @selector(_ln_setCenter:));
			if(NO == class_addMethod(cls, @selector(setCenter:), method_getImplementation(m), method_getTypeEncoding(m)))
			{
				return;
			}
			
			m = class_getInstanceMethod([UIView class], @selector(_ln_setBounds:));
			if(NO == class_addMethod(cls, @selector(setBounds:), method_getImplementation(m), method_getTypeEncoding(m)))
			{
				return;
			}
		}
	});
}

@end

#endif
