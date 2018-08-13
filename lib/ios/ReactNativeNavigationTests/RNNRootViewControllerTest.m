#import <XCTest/XCTest.h>
#import "RNNRootViewController.h"
#import "RNNReactRootViewCreator.h"
#import "RNNTestRootViewCreator.h"
#import <React/RCTConvert.h>
#import "RNNNavigationOptions.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNUIBarButtonItem.h"


@interface RNNRootViewController (EmbedInTabBar)
- (void)embedInTabBarController;
@end

@implementation RNNRootViewController (EmbedInTabBar)

- (void)embedInTabBarController {
	RNNTabBarController* tabVC = [[RNNTabBarController alloc] init];
	tabVC.viewControllers = @[self];
	[self viewWillAppear:false];
}

@end

@interface RNNRootViewControllerTest : XCTestCase

@property (nonatomic, strong) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) NSString* pageName;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) id emitter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNRootViewController* uut;
@end

@implementation RNNRootViewControllerTest

- (void)setUp {
	[super setUp];
	self.creator = [[RNNTestRootViewCreator alloc] init];
	self.pageName = @"somename";
	self.componentId = @"cntId";
	self.emitter = nil;
	self.options = [[RNNNavigationOptions alloc] initWithDict:@{}];
	self.uut = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withComponentId:self.componentId rootViewCreator:self.creator eventEmitter:self.emitter isExternalComponent:NO];
}

-(void)testTopBarBackgroundColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBar.background.color = inputColor;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];

	XCTAssertTrue([self.uut.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
}

-(void)testTopBarBackgroundColorWithoutNavigationController{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBar.background.color = inputColor;

	XCTAssertNoThrow([self.uut viewWillAppear:false]);
}

- (void)testStatusBarHidden_default {
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarVisible_false {
	self.options.statusBar.visible = @(0);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarVisible_true {
	self.options.statusBar.visible = @(1);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarHideWithTopBar_false {
	self.options.statusBar.hideWithTopBar = @(0);
	self.options.topBar.visible = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarHideWithTopBar_true {
	self.options.statusBar.hideWithTopBar = @(1);
	self.options.topBar.visible = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([self.uut prefersStatusBarHidden]);
}

-(void)testTitle_string{
	NSString* title =@"some title";
	self.options.topBar.title.text = title;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];

	[self.uut viewWillAppear:false];
	XCTAssertTrue([self.uut.navigationItem.title isEqual:title]);
}

-(void)testTitle_default{
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];

	[self.uut viewWillAppear:false];
	XCTAssertNil(self.uut.navigationItem.title);
}

-(void)testTopBarTextColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBar.title.color = inputColor;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testbackgroundColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.layout.backgroundColor = inputColor;
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.view.backgroundColor isEqual:expectedColor]);
}

-(void)testPopGestureEnabled_true{
	NSNumber* popGestureEnabled = @(1);
	self.options.popGesture = popGestureEnabled;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertTrue(self.uut.navigationController.interactivePopGestureRecognizer.enabled);
}

-(void)testPopGestureEnabled_false{
	NSNumber* popGestureEnabled = @(0);
	self.options.popGesture = popGestureEnabled;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertFalse(self.uut.navigationController.interactivePopGestureRecognizer.enabled);
}

-(void)testTopBarTextFontFamily_validFont{
	NSString* inputFont = @"HelveticaNeue";
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBar.title.fontFamily = inputFont;
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:17];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTopBarHideOnScroll_true {
	NSNumber* hideOnScrollInput = @(1);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBar.hideOnScroll = hideOnScrollInput;
	[self.uut viewWillAppear:false];
	XCTAssertTrue(self.uut.navigationController.hidesBarsOnSwipe);
}

-(void)testTopBarTranslucent {
	NSNumber* topBarTranslucentInput = @(0);
	self.options.topBar.translucent = topBarTranslucentInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertFalse(self.uut.navigationController.navigationBar.translucent);
}

-(void)testTabBadge {
	NSString* tabBadgeInput = @"5";
	self.options.bottomTab.badge = tabBadgeInput;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];
	UITabBarItem* item = [[UITabBarItem alloc] initWithTitle:@"A Tab" image:nil tag:1];
	[self.uut setTabBarItem:item];
	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];
	XCTAssertTrue([self.uut.tabBarItem.badgeValue isEqualToString:tabBadgeInput]);

}

-(void)testTopBarTransparent_BOOL_True {
	NSNumber* topBarTransparentInput = @(1);
	self.options.topBar.transparent = topBarTransparentInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIView* transparentView = [self.uut.navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
	XCTAssertTrue(transparentView);
	XCTAssertTrue([NSStringFromCGRect(transparentView.frame) isEqual: NSStringFromCGRect(CGRectZero)]);
}

-(void)testTopBarTransparent_BOOL_false {
	NSNumber* topBarTransparentInput = @(0);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBar.transparent = topBarTransparentInput;
	[self.uut viewWillAppear:false];
	UIView* transparentView = [self.uut.navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
	XCTAssertFalse(transparentView);
}


-(void)testStoreOriginalTopBarImages {

}


-(void)testTopBarLargeTitle_default {
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertEqual(self.uut.navigationItem.largeTitleDisplayMode,  UINavigationItemLargeTitleDisplayModeNever);
}
-(void)testTopBarLargeTitle_true {
	self.options.topBar.largeTitle.visible = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertEqual(self.uut.navigationItem.largeTitleDisplayMode, UINavigationItemLargeTitleDisplayModeAlways);
}
-(void)testTopBarLargeTitle_false {
	self.options.topBar.largeTitle.visible  = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertEqual(self.uut.navigationItem.largeTitleDisplayMode, UINavigationItemLargeTitleDisplayModeNever);
}


-(void)testTopBarLargeTitleFontSize_withoutTextFontFamily_withoutTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	self.options.topBar.largeTitle.fontSize = topBarTextFontSizeInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTopBarLargeTitleFontSize_withoutTextFontFamily_withTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBar.largeTitle.fontSize = topBarTextFontSizeInput;
	self.options.topBar.largeTitle.color = inputColor;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTopBarLargeTitleFontSize_withTextFontFamily_withTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSNumber* inputColor = @(0xFFFF0000);
	NSString* inputFont = @"HelveticaNeue";
	self.options.topBar.largeTitle.fontSize = topBarTextFontSizeInput;
	self.options.topBar.largeTitle.color = inputColor;
	self.options.topBar.largeTitle.fontFamily = inputFont;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTopBarLargeTitleFontSize_withTextFontFamily_withoutTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSString* inputFont = @"HelveticaNeue";
	self.options.topBar.largeTitle.fontSize = topBarTextFontSizeInput;
	self.options.topBar.largeTitle.fontFamily = inputFont;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.largeTitleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}


-(void)testTopBarTextFontSize_withoutTextFontFamily_withoutTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	self.options.topBar.title.fontSize = topBarTextFontSizeInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTopBarTextFontSize_withoutTextFontFamily_withTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBar.title.fontSize = topBarTextFontSizeInput;
	self.options.topBar.title.color = inputColor;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTopBarTextFontSize_withTextFontFamily_withTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSNumber* inputColor = @(0xFFFF0000);
	NSString* inputFont = @"HelveticaNeue";
	self.options.topBar.title.fontSize = topBarTextFontSizeInput;
	self.options.topBar.title.color = inputColor;
	self.options.topBar.title.fontFamily = inputFont;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTopBarTextFontSize_withTextFontFamily_withoutTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSString* inputFont = @"HelveticaNeue";
	self.options.topBar.title.fontSize = topBarTextFontSizeInput;
	self.options.topBar.title.fontFamily = inputFont;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

// TODO: Currently not passing
-(void)testTopBarTextFontFamily_invalidFont{
	NSString* inputFont = @"HelveticaNeueeeee";
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBar.title.fontFamily = inputFont;
	//	XCTAssertThrows([self.uut viewWillAppear:false]);
}

-(void)testOrientation_portrait {
	NSArray* supportedOrientations = @[@"portrait"];
	self.options.layout.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskPortrait;
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_portraitString {
	NSString* supportedOrientation = @"portrait";
	self.options.layout.orientation = supportedOrientation;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = (UIInterfaceOrientationMaskPortrait);
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_portraitAndLandscape {
	NSArray* supportedOrientations = @[@"portrait", @"landscape"];
	self.options.layout.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = (UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskLandscape);
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_all {
	NSArray* supportedOrientations = @[@"all"];
	self.options.layout.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskAll;
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_default {
	NSString* supportedOrientations = @"default";
	self.options.layout.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}


-(void)testOrientationTabsController_portrait {
	NSArray* supportedOrientations = @[@"portrait"];
	self.options.layout.orientation = supportedOrientations;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];

	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];

	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskPortrait;
	XCTAssertTrue(self.uut.tabBarController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientationTabsController_portraitAndLandscape {
	NSArray* supportedOrientations = @[@"portrait", @"landscape"];
	self.options.layout.orientation = supportedOrientations;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];

	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];

	UIInterfaceOrientationMask expectedOrientation = (UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskLandscape);
	XCTAssertTrue(self.uut.tabBarController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientationTabsController_all {
	NSArray* supportedOrientations = @[@"all"];
	self.options.layout.orientation = supportedOrientations;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];

	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];

	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskAll;
	XCTAssertTrue(self.uut.tabBarController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testRightButtonsWithTitle_withoutStyle {
	self.options.topBar.rightButtons = @[@{@"id": @"testId", @"text": @"test"}];
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	RNNUIBarButtonItem* button = (RNNUIBarButtonItem*)[nav.topViewController.navigationItem.rightBarButtonItems objectAtIndex:0];
	NSString* expectedButtonId = @"testId";
	NSString* expectedTitle = @"test";
	XCTAssertTrue([button.buttonId isEqualToString:expectedButtonId]);
	XCTAssertTrue([button.title isEqualToString:expectedTitle]);
	XCTAssertTrue(button.enabled);
}

-(void)testRightButtonsWithTitle_withStyle {
	NSNumber* inputColor = @(0xFFFF0000);

	self.options.topBar.rightButtons = @[@{@"id": @"testId", @"text": @"test", @"enabled": @false, @"buttonColor": inputColor, @"buttonFontSize": @22, @"buttonFontWeight": @"800"}];
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	RNNUIBarButtonItem* button = (RNNUIBarButtonItem*)[nav.topViewController.navigationItem.rightBarButtonItems objectAtIndex:0];
	NSString* expectedButtonId = @"testId";
	NSString* expectedTitle = @"test";
	XCTAssertTrue([button.buttonId isEqualToString:expectedButtonId]);
	XCTAssertTrue([button.title isEqualToString:expectedTitle]);
	XCTAssertFalse(button.enabled);

	//TODO: Determine how to tests buttonColor,buttonFontSize and buttonFontWeight?
}


-(void)testLeftButtonsWithTitle_withoutStyle {
	self.options.topBar.leftButtons = @[@{@"id": @"testId", @"text": @"test"}];
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	RNNUIBarButtonItem* button = (RNNUIBarButtonItem*)[nav.topViewController.navigationItem.leftBarButtonItems objectAtIndex:0];
	NSString* expectedButtonId = @"testId";
	NSString* expectedTitle = @"test";
	XCTAssertTrue([button.buttonId isEqualToString:expectedButtonId]);
	XCTAssertTrue([button.title isEqualToString:expectedTitle]);
	XCTAssertTrue(button.enabled);
}

-(void)testLeftButtonsWithTitle_withStyle {
	NSNumber* inputColor = @(0xFFFF0000);

	self.options.topBar.leftButtons = @[@{@"id": @"testId", @"text": @"test", @"enabled": @false, @"buttonColor": inputColor, @"buttonFontSize": @22, @"buttonFontWeight": @"800"}];
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	RNNUIBarButtonItem* button = (RNNUIBarButtonItem*)[nav.topViewController.navigationItem.leftBarButtonItems objectAtIndex:0];
	NSString* expectedButtonId = @"testId";
	NSString* expectedTitle = @"test";
	XCTAssertTrue([button.buttonId isEqualToString:expectedButtonId]);
	XCTAssertTrue([button.title isEqualToString:expectedTitle]);
	XCTAssertFalse(button.enabled);

	//TODO: Determine how to tests buttonColor,buttonFontSize and buttonFontWeight?
}

-(void)testTopBarNoBorderOn {
	NSNumber* topBarNoBorderInput = @(1);
	self.options.topBar.noBorder = topBarNoBorderInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil(self.uut.navigationController.navigationBar.shadowImage);
}

-(void)testTopBarNoBorderOff {
	NSNumber* topBarNoBorderInput = @(0);
	self.options.topBar.noBorder = topBarNoBorderInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil(self.uut.navigationController.navigationBar.shadowImage);
}

-(void)testStatusBarBlurOn {
	NSNumber* statusBarBlurInput = @(1);
	self.options.statusBar.blur = statusBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil([self.uut.view viewWithTag:BLUR_STATUS_TAG]);
}

-(void)testStatusBarBlurOff {
	NSNumber* statusBarBlurInput = @(0);
	self.options.statusBar.blur = statusBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.view viewWithTag:BLUR_STATUS_TAG]);
}

- (void)testTabBarHidden_default {
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut hidesBottomBarWhenPushed]);
}


- (void)testTabBarHidden_true {
	self.options.bottomTabs.visible = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([self.uut hidesBottomBarWhenPushed]);
}

- (void)testTabBarHidden_false {
	self.options.bottomTabs.visible = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut hidesBottomBarWhenPushed]);
}

-(void)testTopBarBlur_default {
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}

-(void)testTopBarBlur_false {
	NSNumber* topBarBlurInput = @(0);
	self.options.topBar.blur = topBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}

-(void)testTopBarBlur_true {
	NSNumber* topBarBlurInput = @(1);
	self.options.topBar.blur = topBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}

-(void)testBackgroundImage {
	UIImage* backgroundImage = [[UIImage alloc] init];
	self.options.backgroundImage = backgroundImage;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([[(UIImageView*)self.uut.view.subviews[0] image] isEqual:backgroundImage]);
}

-(void)testRootBackgroundImage {
	UIImage* rootBackgroundImage = [[UIImage alloc] init];
	self.options.rootBackgroundImage = rootBackgroundImage;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertTrue([[(UIImageView*)self.uut.navigationController.view.subviews[0] image] isEqual:rootBackgroundImage]);
}

-(void)testTopBarDrawUnder_true {
	self.options.topBar.drawBehind = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue(self.uut.edgesForExtendedLayout & UIRectEdgeTop);
}

-(void)testTopBarDrawUnder_false {
	self.options.topBar.drawBehind = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse(self.uut.edgesForExtendedLayout & UIRectEdgeTop);
}

-(void)testBottomTabsDrawUnder_true {
	self.options.bottomTabs.drawBehind = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue(self.uut.edgesForExtendedLayout & UIRectEdgeBottom);
}

-(void)testBottomTabsDrawUnder_false {
	self.options.bottomTabs.drawBehind = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse(self.uut.edgesForExtendedLayout & UIRectEdgeBottom);
}

#pragma mark BottomTabs

- (void)testTabBarTranslucent_default {
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarTranslucent_true {
	self.options.bottomTabs.translucent = @(1);
	[self.uut embedInTabBarController];
	XCTAssertTrue(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarTranslucent_false {
	self.options.bottomTabs.translucent = @(0);
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarHideShadow_default {
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarHideShadow_true {
	self.options.bottomTabs.hideShadow = @(1);
	[self.uut embedInTabBarController];
	XCTAssertTrue(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarHideShadow_false {
	self.options.bottomTabs.hideShadow = @(0);
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarBackgroundColor {
	self.options.bottomTabs.backgroundColor = @(0xFFFF0000);
	[self.uut embedInTabBarController];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.tabBarController.tabBar.barTintColor isEqual:expectedColor]);
}

-(void)testTabBarTextFontFamily_validFont{
	NSString* inputFont = @"HelveticaNeue";
	self.options.bottomTab.fontFamily = inputFont;
	self.options.bottomTab.text = @"Tab 1";
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:10];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withoutTextFontFamily_withoutTextColor {
	self.options.bottomTab.fontSize = @(15);
	self.options.bottomTab.text = @"Tab 1";
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withoutTextFontFamily {
	self.options.bottomTab.fontSize = @(15);
	self.options.bottomTab.text = @"Tab 1";
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withTextFontFamily_withTextColor {
	NSString* inputFont = @"HelveticaNeue";
	self.options.bottomTab.text = @"Tab 1";
	self.options.bottomTab.fontSize = @(15);
	self.options.bottomTab.fontFamily = inputFont;
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withTextFontFamily_withoutTextColor {
	NSString* inputFont = @"HelveticaNeue";
	self.options.bottomTab.text = @"Tab 1";
	self.options.bottomTab.fontSize = @(15);
	self.options.bottomTab.fontFamily = inputFont;
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

@end
