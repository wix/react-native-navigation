//
//  RNNSwizzles.m
//  ReactNativeNavigation
//
//  Created by Leo Natan (Wix) on 1/17/18.
//  Copyright © 2018 Wix. All rights reserved.
//

#import "RNNSwizzles.h"
#import "AnimationObserver.h"
@import ObjectiveC;
@import UIKit;

static id (*__SWZ_initWithEventDispatcher_orig)(id self, SEL _cmd, id eventDispatcher);
static void (*__SWZ_setFrame_orig)(id self, SEL _cmd, CGRect frame);
static void (*__SWZ_didMoveToWindow_orig)(id self, SEL _cmd);

static void __RNN_setFrame_orig(UIScrollView *self, SEL _cmd, CGRect frame) {
    CGPoint originalOffset = self.contentOffset;

    __SWZ_setFrame_orig(self, _cmd, frame);

    UIEdgeInsets contentInset = self.adjustedContentInset;

    CGSize contentSize = self.contentSize;

    // If contentSize has not been measured yet we can't check bounds.
    if (CGSizeEqualToSize(contentSize, CGSizeZero)) {
        self.contentOffset = originalOffset;
    } else {
        // Make sure offset don't exceed bounds. This could happen on screen
        // rotation.
        CGSize boundsSize = self.bounds.size;
        self.contentOffset = CGPointMake(
            MAX(-contentInset.left,
                MIN(contentSize.width - boundsSize.width + contentInset.right, originalOffset.x)),
            MAX(-contentInset.top, MIN(contentSize.height - boundsSize.height + contentInset.bottom,
                                       originalOffset.y)));
    }
}

static void __RNN_didMoveToWindow(UIView *self, SEL _cmd) {
    if (![[AnimationObserver sharedObserver] isAnimating] || !self.window) {
        __SWZ_didMoveToWindow_orig(self, _cmd);
    } else {
        [[AnimationObserver sharedObserver] registerAnimationEndedBlock:^{
          __SWZ_didMoveToWindow_orig(self, _cmd);
        }];
    }
}

@implementation RNNSwizzles

- (id)__swz_initWithEventDispatcher:(id)eventDispatcher {
    id returnValue = __SWZ_initWithEventDispatcher_orig(self, _cmd, eventDispatcher);

    [(UIScrollView *)[returnValue valueForKey:@"scrollView"]
        setContentInsetAdjustmentBehavior:UIScrollViewContentInsetAdjustmentScrollableAxes];

    return returnValue;
}

+ (void)load {
    Class cls = NSClassFromString(@"RCTScrollView");
    if (cls == NULL) {
        return;
    }
    Method m1 = class_getInstanceMethod(cls, NSSelectorFromString(@"initWithEventDispatcher:"));

    if (m1 == NULL) {
        return;
    }

    __SWZ_initWithEventDispatcher_orig = (void *)method_getImplementation(m1);
    Method m2 = class_getInstanceMethod([RNNSwizzles class],
                                        NSSelectorFromString(@"__swz_initWithEventDispatcher:"));
    method_exchangeImplementations(m1, m2);

    cls = NSClassFromString(@"RCTCustomScrollView");
    if (cls == NULL) {
        return;
    }

    m1 = class_getInstanceMethod(cls, @selector(setFrame:));
    __SWZ_setFrame_orig = (void *)method_getImplementation(m1);
    method_setImplementation(m1, (IMP)__RNN_setFrame_orig);

    cls = NSClassFromString(@"RCTBaseTextInputView");
    if (cls == NULL) {
        return;
    }
    Method m4 = class_getInstanceMethod(cls, NSSelectorFromString(@"didMoveToWindow"));
    __SWZ_didMoveToWindow_orig = (void *)method_getImplementation(m4);
    method_setImplementation(m4, (IMP)__RNN_didMoveToWindow);
}

@end
