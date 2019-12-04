
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
    [self.presenter boundViewController:self];
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
    UIViewController* firstChildViewController = self.childViewControllers.lastObject;
    RNNNavigationOptions* resolvedOptions = self.resolveOptions;
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
		dispatch_group_t group = dispatch_group_create();
		dispatch_group_enter(group);
        dispatch_async(dispatch_get_main_queue(), ^{
            [firstChildViewController setReactViewReadyCallback:^{
                dispatch_group_leave(group);
            }];
            [firstChildViewController render];
        });
		
		dispatch_group_enter(group);
		[self.presenter renderComponents:resolvedOptions perform:^{
			dispatch_group_leave(group);
		}];
		dispatch_group_wait(group, DISPATCH_TIME_FOREVER);
		
		dispatch_async(dispatch_get_main_queue(), ^{
            [self readyForPresentation];
		});
	});
}

- (void)readyForPresentation {
    [self.parentViewController readyForPresentation];
    if (self.reactViewReadyCallback) {
        self.reactViewReadyCallback();
        self.reactViewReadyCallback = nil;
    }
}

- (UIViewController *)getCurrentChild {
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
	[((UISplitViewController *)self.parentViewController) onChildWillAppear];
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
    return [objc_getAssociatedObject(self, @selector(waitForRender)) boolValue];
}

- (void)setWaitForRender:(BOOL)waitForRender {
    objc_setAssociatedObject(self, @selector(waitForRender), [NSNumber numberWithBool:waitForRender], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

@end
