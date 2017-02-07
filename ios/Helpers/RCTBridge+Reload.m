//
//  Bridge.m
//  ReactNativeNavigation
//
//  Created by Simon Mitchell on 02/02/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import <React/RCTBridge.h>
#import "RCTBridge+Reload.h"
#import <objc/runtime.h>

NSString *const RCTReloadNotification = @"RCTReloadNotification";

@implementation RCTBridge (Reload)

+ (void)load {
    static dispatch_once_t onceToken;
    // Make sure to only call once
    dispatch_once(&onceToken, ^{
        
        Class class = [self class];
        
        // Replace reload call with _reload
        SEL originalSelector = @selector(reload);
        SEL swizzledSelector = @selector(_reload);
        
        Method originalMethod = class_getInstanceMethod(class, originalSelector);
        Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);
        
        // Swizzle the methods!
        BOOL didAddMethod =
        class_addMethod(class,
                        originalSelector,
                        method_getImplementation(swizzledMethod),
                        method_getTypeEncoding(swizzledMethod));
        
        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledSelector,
                                method_getImplementation(originalMethod),
                                method_getTypeEncoding(originalMethod));
        } else {
            method_exchangeImplementations(originalMethod, swizzledMethod);
        }
    });
}

- (void)_reload {
    // Call [self _reload] because we have swizzled this with `reload` it will call the original RCTBridge's reload function
    [self _reload];
    dispatch_async(dispatch_get_main_queue(), ^{
        [[NSNotificationCenter defaultCenter] postNotificationName:RCTReloadNotification object:self];
    });
}

@end
