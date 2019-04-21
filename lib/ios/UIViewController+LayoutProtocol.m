
#import "UIViewController+LayoutProtocol.h"
#import <objc/runtime.h>

@implementation UIViewController (LayoutProtocol)

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
						   creator:(id<RNNRootViewCreator>)creator
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

- (RNNNavigationOptions *)resolveOptions {
	return [(RNNNavigationOptions *)[self.options mergeInOptions:self.getCurrentChild.resolveOptions.copy] withDefault:self.defaultOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[self.presenter mergeOptions:options currentOptions:self.options defaultOptions:self.defaultOptions];
	[((UIViewController *)self.parentViewController) mergeOptions:options];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[self.options overrideOptions:options];
}

- (void)renderTreeAndWait:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
		dispatch_group_t group = dispatch_group_create();
		for (UIViewController* childViewController in self.childViewControllers) {
			dispatch_group_enter(group);
			dispatch_async(dispatch_get_main_queue(), ^{
				[childViewController renderTreeAndWait:wait perform:^{
					dispatch_group_leave(group);
				}];
			});
		}
		
		dispatch_group_enter(group);
		[self.presenter renderComponents:self.resolveOptions perform:^{
			dispatch_group_leave(group);
		}];
		dispatch_group_wait(group, DISPATCH_TIME_FOREVER);
		
		dispatch_async(dispatch_get_main_queue(), ^{
			readyBlock();
		});
	});
}

- (UIViewController *)getCurrentChild {
	return nil;
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

- (id<RNNRootViewCreator>)creator {
	return objc_getAssociatedObject(self, @selector(creator));
}

- (void)setCreator:(id<RNNRootViewCreator>)creator {
	objc_setAssociatedObject(self, @selector(creator), creator, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}



@end
