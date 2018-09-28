#import "RNNSplitViewController.h"

@implementation RNNSplitViewController

-(instancetype)initWithOptions:(RNNSplitViewOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter {
	self = [super init];
	self.componentId = componentId;
	self.options = options;
	self.eventEmitter = eventEmitter;
	self.creator = creator;

	self.navigationController.delegate = self;

	return self;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.options applyOn:self];
}

- (UIViewController *)getLeafViewController {
	return self;
}

- (void)performOnChildLoad:(RNNNavigationOptions *)childOptions {
	RNNNavigationOptions* combinedOptions = [_presenter presentWithChildOptions:childOptions on:self];
	if ([self.parentViewController respondsToSelector:@selector(performOnChildLoad:)]) {
		[self.parentViewController performSelector:@selector(performOnChildLoad:) withObject:combinedOptions];
	}
}

- (void)performOnChildWillAppear:(RNNNavigationOptions *)childOptions {
	RNNNavigationOptions* combinedOptions = [_presenter presentWithChildOptions:childOptions on:self];
	if ([self.parentViewController respondsToSelector:@selector(performOnChildWillAppear:)]) {
		[self.parentViewController performSelector:@selector(performOnChildWillAppear:) withObject:combinedOptions];
	}
}

@end
