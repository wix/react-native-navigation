/**
 * Fix for UIRefreshControl spinner flicker with large titles.
 * iOS UIKit internally toggles hidden state during layout changes,
 * causing the spinner to flicker. This swizzles setHidden to block
 * visibility changes while the control is actively refreshing.
 */

#import <UIKit/UIKit.h>
#import <objc/runtime.h>

static char kRNNIsEndingRefreshKey;

@implementation UIRefreshControl (RNNStable)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class class = [self class];
        
        // Swizzle setHidden
        SEL originalSelector = @selector(setHidden:);
        SEL swizzledSelector = @selector(rnn_setHidden:);
        
        Method originalMethod = class_getInstanceMethod(class, originalSelector);
        Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);
        
        BOOL didAddMethod = class_addMethod(class,
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
        
        // Swizzle endRefreshing
        SEL originalEndSelector = @selector(endRefreshing);
        SEL swizzledEndSelector = @selector(rnn_endRefreshing);
        
        Method originalEndMethod = class_getInstanceMethod(class, originalEndSelector);
        Method swizzledEndMethod = class_getInstanceMethod(class, swizzledEndSelector);
        
        didAddMethod = class_addMethod(class,
                                       originalEndSelector,
                                       method_getImplementation(originalEndMethod),
                                       method_getTypeEncoding(originalEndMethod));
        
        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledEndSelector,
                                method_getImplementation(originalEndMethod),
                                method_getTypeEncoding(originalEndMethod));
        } else {
            method_exchangeImplementations(originalEndMethod, swizzledEndMethod);
        }
    });
}

- (BOOL)rnn_isEndingRefresh {
    return [objc_getAssociatedObject(self, &kRNNIsEndingRefreshKey) boolValue];
}

- (void)rnn_setIsEndingRefresh:(BOOL)value {
    objc_setAssociatedObject(self, &kRNNIsEndingRefreshKey, @(value), OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (void)rnn_endRefreshing {
    [self rnn_setIsEndingRefresh:YES];
    [self rnn_endRefreshing];
    [self rnn_setIsEndingRefresh:NO];
}

- (void)rnn_setHidden:(BOOL)hidden {
    if ([self rnn_isEndingRefresh]) {
        [self rnn_setHidden:hidden];
        return;
    }
    
    if (self.isRefreshing) {
        return;
    }
    
    [self rnn_setHidden:hidden];
}

@end
