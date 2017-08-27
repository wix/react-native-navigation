
#import "RNNRootViewController.h"
#import <React/RCTConvert.h>
#import "RNNNavigationButtons.h"

@interface RNNRootViewController()
@property (nonatomic, strong) NSString* containerName;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

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
	
	self.navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:self];
	
	return self;
}

-(void)viewWillAppear:(BOOL)animated{
	[super viewWillAppear:animated];
	[self.navigationOptions applyOn:self];
	[self applyNavigationButtons];
}

- (BOOL)prefersStatusBarHidden {
	if ([self.navigationOptions.statusBarHidden boolValue]) {
		return YES;
	} else if ([self.navigationOptions.statusBarHideWithTopBar boolValue]) {
		return self.navigationController.isNavigationBarHidden;
	}
	return NO;
}

- (BOOL)hidesBottomBarWhenPushed
{
	if (self.navigationOptions.tabBarHidden) {
		return [self.navigationOptions.tabBarHidden boolValue];
	}
	return NO;
}

-(void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];
	[self.eventEmitter sendContainerDidAppear:self.containerId];
}

-(void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	[self.eventEmitter sendContainerDidDisappear:self.containerId];
}

-(void) applyNavigationButtons{
	[self.navigationButtons applyLeftButtons:self.navigationOptions.leftButtons rightButtons:self.navigationOptions.rightButtons];
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
