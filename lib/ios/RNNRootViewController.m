
#import "RNNRootViewController.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) NSString* containerName;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic) BOOL _statusBarHidden;
@end

@implementation RNNRootViewController

-(instancetype)initWithNode:(RNNLayoutNode*)node rootViewCreator:(id<RNNRootViewCreator>)creator eventEmitter:(RNNEventEmitter*)eventEmitter {
	self = [super init];
	self.containerId = node.nodeId;
	self.containerName = node.data[@"name"];
	self.eventEmitter = eventEmitter;
	
	self.view = [creator createRootView:self.containerName rootViewId:self.containerId];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	
	self.navigationItem.title = node.data[@"navigationOptions"][@"title"];
	self._statusBarHidden = [(NSNumber*)node.data[@"navigationOptions"][@"statusBarHidden"] boolValue];
	
	return self;
}

- (BOOL)prefersStatusBarHidden {
	return self._statusBarHidden; // || self.navigationController.isNavigationBarHidden;
}


-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendContainerStart:self.containerId];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendContainerStop:self.containerId];
}

/**
 *	fix for #877, #878
 */
-(void)onJsReload {
	[self cleanReactLeftovers];
}

/**
 * fix for #880
 */
-(void)dealloc {
	[self cleanReactLeftovers];
}

-(void)cleanReactLeftovers {
	[[NSNotificationCenter defaultCenter] removeObserver:self];
	[[NSNotificationCenter defaultCenter] removeObserver:self.view];
	self.view = nil;
}

@end
