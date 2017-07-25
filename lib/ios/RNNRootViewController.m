
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) NSDictionary* nodeData;
@property (nonatomic, strong) NSString* containerName;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic) BOOL _statusBarHidden;
@end

@implementation RNNRootViewController

-(instancetype)initWithName:(NSString*)name withOptions:(NSDictionary*)options withContainerId:(NSString*)containerId rootViewCreator:(id<RNNRootViewCreator>)creator eventEmitter:(RNNEventEmitter*)eventEmitter {
	self = [super init];
	self.containerId = containerId;
	self.nodeData = options;
	self.containerName = name;
	self.eventEmitter = eventEmitter;
	self.view = [creator createRootView:self.containerName rootViewId:self.containerId];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJsReload)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
	
	self.navigationItem.title = options[@"title"];
	self._statusBarHidden = [(NSNumber*)options[@"statusBarHidden"] boolValue];
	
	
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

-(void)viewWillAppear:(BOOL)animated{
	if ([self.nodeData objectForKey:@"topBarBackgroundColor"]) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.nodeData[@"topBarBackgroundColor"]];
		self.navigationController.navigationBar.barTintColor = backgroundColor;
	}

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
