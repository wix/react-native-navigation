#import "RNNSplitViewController.h"

@interface RNNSplitViewController()
@property (nonatomic) BOOL _optionsApplied;
@property (nonatomic, copy) void (^rotationBlock)(void);
@end

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

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	readyBlock();
}

- (void)mergeOptions:(RNNOptions *)options {
	[self.options mergeOptions:options];
}

@end
