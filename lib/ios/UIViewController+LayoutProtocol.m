
#import "UIViewController+LayoutProtocol.h"
#import <objc/runtime.h>

@implementation UIViewController (LayoutProtocol)

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
						   creator:(id<RNNComponentViewCreator>)creator
						   options:(RNNNavigationOptions *)options
					defaultOptions:(RNNNavigationOptions *)defaultOptions
						 presenter:(RNNBasePresenter *)presenter
					  eventEmitter:(RNNEventEmitter *)eventEmitter
			  childViewControllers:(NSArray *)childViewControllers {
	self = [self init];
	self.options = options;
	self.defaultOptions = defaultOptions;
	self.layoutInfo = layoutInfo;
	self.creator = creator;
    self.eventEmitter = eventEmitter;
    self.presenter = presenter;
    [self.presenter bindViewController:self];
    self.extendedLayoutIncludesOpaqueBars = YES;
    [self loadChildren:childViewControllers];
    [self.presenter applyOptionsOnInit:self.resolveOptions];

	return self;
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
    [self.options overrideOptions:options];
    [self.presenter mergeOptions:options resolvedOptions:self.resolveOptions];
    [self.parentViewController mergeChildOptions:options child:self];
}

- (void)mergeChildOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    [self.presenter mergeOptions:options resolvedOptions:self.resolveOptions];
    [self.parentViewController mergeChildOptions:options child:child];
}

- (RNNNavigationOptions *)resolveOptions {
    return (RNNNavigationOptions *) [self.options mergeInOptions:self.getCurrentChild.resolveOptions.copy];
}

- (RNNNavigationOptions *)resolveOptionsWithDefault {
    return [(RNNNavigationOptions *) [self.options mergeInOptions:self.getCurrentChild.resolveOptions.copy] withDefault:self.defaultOptions];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}

- (UINavigationController *)stack {
    if ([self isKindOfClass:UINavigationController.class]) {
        return (UINavigationController *)self;
    } else {
        return self.navigationController;
    }
}

- (void)render {
    if (!self.waitForRender) {
        [self readyForPresentation];
    }
    
    [self.getCurrentChild setReactViewReadyCallback:^{
        [self.presenter renderComponents:self.resolveOptionsWithDefault perform:^{
            [self readyForPresentation];
        }];
    }];
    
    [self.getCurrentChild render];
}

- (void)loadChildren:(NSArray *)children {
    if (!self.isChildViewControllersLoaded && [self respondsToSelector:@selector(setViewControllers:)]) {
        self.isChildViewControllersLoaded = YES;
        [self performSelector:@selector(setViewControllers:) withObject:children];
    }
}

- (void)readyForPresentation {
    if (self.reactViewReadyCallback) {
        self.reactViewReadyCallback();
        self.reactViewReadyCallback = nil;
    }
    
    [self.parentViewController readyForPresentation];
}

- (UIViewController *)getCurrentChild {
    for (UIViewController* childViewController in self.childViewControllers.reverseObjectEnumerator.allObjects) {
        if (childViewController.layoutInfo) {
            return childViewController;
        }
    }
    
    return nil;
}

- (void)destroy {
    [self destroyReactView];
    [self.presentedViewController destroy];
    
    for (UIViewController* child in self.childViewControllers) {
        [child destroy];
    }
}

- (void)destroyReactView {
    
}

- (UIViewController *)presentedComponentViewController {
    UIViewController* currentChild = self.getCurrentChild;
    return currentChild ? currentChild.presentedComponentViewController : self;
}

- (UIViewController *)topMostViewController {
    if (self.parentViewController) {
        return [self.parentViewController topMostViewController];
    } else
        return self;
}

- (UIViewController *)findViewController:(UIViewController *)child {
    if (self == child) return child;
    
    for (UIViewController* childController in self.childViewControllers) {
        UIViewController* fromChild = [childController findViewController:child];
        if (fromChild) return childController;
    }
    
    return nil;
}

- (CGFloat)getTopBarHeight {
    for(UIViewController * child in [self childViewControllers]) {
        CGFloat childTopBarHeight = [child getTopBarHeight];
        if (childTopBarHeight > 0) return childTopBarHeight;
    }
    
    return 0;
}

- (CGFloat)getBottomTabsHeight {
    for(UIViewController * child in [self childViewControllers]) {
        CGFloat childBottomTabsHeight = [child getBottomTabsHeight];
        if (childBottomTabsHeight > 0) return childBottomTabsHeight;
    }
    
    return 0;
}

- (void)screenPopped {
    
}

- (void)onChildWillAppear {
	[self.presenter applyOptions:self.resolveOptions];
	[self.parentViewController onChildWillAppear];
}

- (void)onChildAddToParent:(UIViewController *)child options:(RNNNavigationOptions *)options {
    [self.parentViewController onChildAddToParent:child options:options];
}

- (void)componentDidAppear {
    [self.presenter componentDidAppear];
    [self.parentViewController componentDidAppear];
}

- (void)componentDidDisappear {
    [self.presenter componentDidDisappear];
    [self.parentViewController componentDidDisappear];
}

#pragma mark getters and setters to associated object

- (RNNNavigationOptions *)options {
	return objc_getAssociatedObject(self, @selector(options));
}

- (void)setOptions:(RNNNavigationOptions *)options {
	objc_setAssociatedObject(self, @selector(options), options, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (RNNNavigationOptions *)defaultOptions {
	return objc_getAssociatedObject(self, @selector(defaultOptions));
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	objc_setAssociatedObject(self, @selector(defaultOptions), defaultOptions, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (RNNLayoutInfo *)layoutInfo {
	return objc_getAssociatedObject(self, @selector(layoutInfo));
}

- (void)setLayoutInfo:(RNNLayoutInfo *)layoutInfo {
	objc_setAssociatedObject(self, @selector(layoutInfo), layoutInfo, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (RNNBasePresenter *)presenter {
	return objc_getAssociatedObject(self, @selector(presenter));
}

- (void)setPresenter:(RNNBasePresenter *)presenter {
	objc_setAssociatedObject(self, @selector(presenter), presenter, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (RNNEventEmitter *)eventEmitter {
	return objc_getAssociatedObject(self, @selector(eventEmitter));
}

- (void)setEventEmitter:(RNNEventEmitter *)eventEmitter {
	objc_setAssociatedObject(self, @selector(eventEmitter), eventEmitter, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (BOOL)isChildViewControllersLoaded {
    return [objc_getAssociatedObject(self, @selector(isChildViewControllersLoaded)) boolValue];
}

- (void)setIsChildViewControllersLoaded:(BOOL)isChildViewControllersLoaded {
    objc_setAssociatedObject(self, @selector(isChildViewControllersLoaded), [NSNumber numberWithBool:isChildViewControllersLoaded], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (id<RNNComponentViewCreator>)creator {
	return objc_getAssociatedObject(self, @selector(creator));
}

- (void)setCreator:(id<RNNComponentViewCreator>)creator {
	objc_setAssociatedObject(self, @selector(creator), creator, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (RNNReactViewReadyCompletionBlock)reactViewReadyCallback {
    return objc_getAssociatedObject(self, @selector(reactViewReadyCallback));
}

- (void)setReactViewReadyCallback:(RNNReactViewReadyCompletionBlock)reactViewReadyCallback {
    objc_setAssociatedObject(self, @selector(reactViewReadyCallback), reactViewReadyCallback, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (BOOL)waitForRender {
    return [objc_getAssociatedObject(self.parentViewController ?: self, @selector(waitForRender)) boolValue];
}

- (void)setWaitForRender:(BOOL)waitForRender {
    objc_setAssociatedObject(self, @selector(waitForRender), [NSNumber numberWithBool:waitForRender], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

@end
