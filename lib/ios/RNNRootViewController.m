
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>


@interface RNNRootViewController()
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) NSString* containerName;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic) BOOL _statusBarHidden;

@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter {
	self = [super init];
	self.containerId = containerId;
	self.containerName = name;
	self.navigationOptions = options;
	self.eventEmitter = eventEmitter;
	self.view = [creator createRootView:self.containerName rootViewId:self.containerId];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	
	
	return self;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.navigationOptions applyOn:self];
}

- (BOOL)prefersStatusBarHidden {
	return [self.navigationOptions.statusBarHidden boolValue]; // || self.navigationController.isNavigationBarHidden;
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendContainerDidAppear:self.containerId];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendContainerDidDisappear:self.containerId];
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
