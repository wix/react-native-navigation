
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
	if ([self respondsToSelector:@selector(setViewControllers:)]) {
		[self performSelector:@selector(setViewControllers:) withObject:childViewControllers];
	}
	self.presenter = presenter;
    [self.presenter bindViewController:self];
	[self.presenter applyOptionsOnInit:self.resolveOptions];

	return self;
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
    [self.options overrideOptions:options];
    [self.presenter mergeOptions:options resolvedOptions:self.resolveOptions];
    [self.parentViewController mergeChildOptions:options];
}

- (void)mergeChildOptions:(RNNNavigationOptions *)options {
    [self.presenter mergeOptions:options resolvedOptions:self.resolveOptions];
	[self.parentViewController mergeChildOptions:options];
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

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	UIInterfaceOrientationMask interfaceOrientationMask = self.presenter ? [self.presenter getOrientation:[self resolveOptions]] : [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	return interfaceOrientationMask;
}

- (void)render {
    if (!self.waitForRender) {
        [self readyForPresentation];
    }
    
    [self.presentedComponentViewController setReactViewReadyCallback:^{
        [self.presenter renderComponents:self.resolveOptionsWithDefault perform:^{
            [self readyForPresentation];
        }];
    }];
    
    [self.presentedComponentViewController render];
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

- (void)onChildWillAppear {
	[self.presenter applyOptions:self.resolveOptions];
	[self.parentViewController onChildWillAppear];
}

- (void)componentDidAppear {
    [self.presenter componentDidAppear];
    [self.parentViewController componentDidAppear];
}

- (void)componentDidDisappear {
    [self.presenter componentDidDisappear];
    [self.parentViewController componentDidDisappear];
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if (parent) {
		[self.presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
	}
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
