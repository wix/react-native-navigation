#import "RNNLayoutManager.h"
#import "RNNLayoutProtocol.h"
#import "UIViewController+LayoutProtocol.h"

@interface RNNLayoutManager ()

@property(nonatomic, strong) NSHashTable<UIViewController *> *pendingViewControllers;

@end

@implementation RNNLayoutManager

- (instancetype)init {
    if (self = [super init]) {
        _pendingViewControllers = [NSHashTable weakObjectsHashTable];
    }
    return self;
}

- (void)addPendingViewController:(UIViewController *)vc {
    if (!vc) {
        return;
    }
    [self.pendingViewControllers addObject:vc];
}

- (void)removePendingViewController:(UIViewController *)vc {
    if (!vc) {
        return;
    }
    [self.pendingViewControllers removeObject:vc];
}

- (UIViewController *)findComponentForId:(NSString *)componentId {
    for (UIViewController *vc in self.pendingViewControllers) {
        UIViewController *result = [self findChildComponentForParent:vc forId:componentId];
        if (result) {
            return result;
        }
    }

    for (UIWindow *window in UIApplication.sharedApplication.windows) {
        UIViewController *result = [self findChildComponentForParent:window.rootViewController
                                                               forId:componentId];
        if (result) {
            return result;
        }
    }

    return nil;
}

- (UIViewController *)findChildComponentForParent:(UIViewController *)parentViewController
                                            forId:(NSString *)componentId {
    if ([parentViewController.layoutInfo.componentId isEqualToString:componentId]) {
        return parentViewController;
    }

    if (parentViewController.presentedViewController) {
        UIViewController *modalResult =
            [self findChildComponentForParent:parentViewController.presentedViewController
                                        forId:componentId];
        if (modalResult) {
            return modalResult;
        }
    }

    for (UIViewController *childVC in parentViewController.childViewControllers) {
        UIViewController *result = [self findChildComponentForParent:childVC forId:componentId];
        if (result) {
            return result;
        }
    }

    if ([parentViewController respondsToSelector:@selector(pendingChildViewControllers)]) {
        NSArray *pendingChildVCs = [parentViewController pendingChildViewControllers];
        for (UIViewController *childVC in pendingChildVCs) {
            UIViewController *result = [self findChildComponentForParent:childVC forId:componentId];
            if (result) {
                return result;
            }
        }
    }

    return nil;
}

@end
