
#import "RNNRootViewController.h"
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
	[self optionsUpdated];
}

- (void)optionsUpdated {
}

- (void)mergeOptions:(NSDictionary *)options {
	[self.options mergeIfEmptyWith:options];
}

-(BOOL)isCustomTransitioned {
	return false;
}

@end
