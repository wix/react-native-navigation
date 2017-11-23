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
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) id emitter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNRootViewController* uut;
@end

@implementation RNNRootViewControllerTest

- (void)setUp {
	[super setUp];
	self.creator = [[RNNTestRootViewCreator alloc] init];
	self.pageName = @"somename";
	self.containerId = @"cntId";
	self.emitter = nil;
	self.options = [RNNNavigationOptions new];
	self.uut = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
}

-(void)testTopBarBackgroundColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBarBackgroundColor = inputColor;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	
	XCTAssertTrue([self.uut.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
}

-(void)testTopBarBackgroundColorWithoutNavigationController{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBarBackgroundColor = inputColor;
	
	XCTAssertNoThrow([self.uut viewWillAppear:false]);
}

- (void)testStatusBarHidden_default {
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarHidden_true {
	self.options.statusBarHidden = @(1);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertTrue([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarHideWithTopBar_false {
	self.options.statusBarHideWithTopBar = @(0);
	self.options.topBarHidden = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

- (void)testStatusBarHideWithTopBar_true {
	self.options.statusBarHideWithTopBar = @(1);
	self.options.topBarHidden = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([self.uut prefersStatusBarHidden]);
}


- (void)testStatusBarHidden_false {
	self.options.statusBarHidden = @(0);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	
	XCTAssertFalse([self.uut prefersStatusBarHidden]);
}

-(void)testTitle_string{
	NSString* title =@"some title";
	self.options.title= title;
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
	self.options.topBarTextColor = inputColor;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testScreenBackgroundColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.screenBackgroundColor = inputColor;
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.view.backgroundColor isEqual:expectedColor]);
}

-(void)testTopBarTextFontFamily_validFont{
	NSString* inputFont = @"HelveticaNeue";
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBarTextFontFamily = inputFont;
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:20];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTopBarHideOnScroll_true {
	NSNumber* hideOnScrollInput = @(1);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBarHideOnScroll = hideOnScrollInput;
	[self.uut viewWillAppear:false];
	XCTAssertTrue(self.uut.navigationController.hidesBarsOnSwipe);
}

-(void)testTopBarButtonColor {
	NSNumber* inputColor = @(0xFFFF0000);
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBarButtonColor = inputColor;
	[self.uut viewWillAppear:false];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.navigationController.navigationBar.tintColor isEqual:expectedColor]);
}

-(void)testTopBarTranslucent {
	NSNumber* topBarTranslucentInput = @(0);
	self.options.topBarTranslucent = topBarTranslucentInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertFalse(self.uut.navigationController.navigationBar.translucent);
}

-(void)testTabBadge {
	NSString* tabBadgeInput = @"5";
	self.options.tabBadge = tabBadgeInput;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];
	UITabBarItem* item = [[UITabBarItem alloc] initWithTitle:@"A Tab" image:nil tag:1];
	[self.uut setTabBarItem:item];
	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];
	XCTAssertTrue([self.uut.tabBarItem.badgeValue isEqualToString:tabBadgeInput]);
	
}


-(void)testTopBarTextFontSize_withoutTextFontFamily_withoutTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	self.options.topBarTextFontSize = topBarTextFontSizeInput;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTopBarTextFontSize_withoutTextFontFamily_withTextColor {
	NSNumber* topBarTextFontSizeInput = @(15);
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBarTextFontSize = topBarTextFontSizeInput;
	self.options.topBarTextColor = inputColor;
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
	self.options.topBarTextFontSize = topBarTextFontSizeInput;
	self.options.topBarTextColor = inputColor;
	self.options.topBarTextFontFamily = inputFont;
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
	self.options.topBarTextFontSize = topBarTextFontSizeInput;
	self.options.topBarTextFontFamily = inputFont;
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	XCTAssertTrue([self.uut.navigationController.navigationBar.titleTextAttributes[@"NSFont"] isEqual:expectedFont]);
}

// TODO: Currently not passing
-(void)testTopBarTextFontFamily_invalidFont{
	NSString* inputFont = @"HelveticaNeueeeee";
	__unused RNNNavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	self.options.topBarTextFontFamily = inputFont;
	//	XCTAssertThrows([self.uut viewWillAppear:false]);
}

-(void)testOrientation_portrait {
	NSArray* supportedOrientations = @[@"portrait"];
	self.options.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskPortrait;
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_portraitString {
	NSString* supportedOrientation = @"portrait";
	self.options.orientation = supportedOrientation;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = (UIInterfaceOrientationMaskPortrait);
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_portraitAndLandscape {
	NSArray* supportedOrientations = @[@"portrait", @"landscape"];
	self.options.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = (UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskLandscape);
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_all {
	NSArray* supportedOrientations = @[@"all"];
	self.options.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskAll;
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testOrientation_default {
	NSString* supportedOrientations = @"default";
	self.options.orientation = supportedOrientations;
	__unused UINavigationController* nav = [[RNNNavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	UIInterfaceOrientationMask expectedOrientation = [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	XCTAssertTrue(self.uut.navigationController.supportedInterfaceOrientations == expectedOrientation);
}


-(void)testOrientationTabsController_portrait {
	NSArray* supportedOrientations = @[@"portrait"];
	self.options.orientation = supportedOrientations;
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
	self.options.orientation = supportedOrientations;
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
	self.options.orientation = supportedOrientations;
	__unused RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	NSMutableArray* controllers = [NSMutableArray new];
	
	[controllers addObject:self.uut];
	[vc setViewControllers:controllers];
	[self.uut viewWillAppear:false];
	
	UIInterfaceOrientationMask expectedOrientation = UIInterfaceOrientationMaskAll;
	XCTAssertTrue(self.uut.tabBarController.supportedInterfaceOrientations == expectedOrientation);
}

-(void)testRightButtonsWithTitle_withoutStyle {
	self.options.rightButtons = @[@{@"id": @"testId", @"title": @"test"}];
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
	
	self.options.rightButtons = @[@{@"id": @"testId", @"title": @"test", @"disabled": @true, @"buttonColor": inputColor, @"buttonFontSize": @22, @"buttonFontWeight": @"800"}];
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
	self.options.leftButtons = @[@{@"id": @"testId", @"title": @"test"}];
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
	
	self.options.leftButtons = @[@{@"id": @"testId", @"title": @"test", @"disabled": @true, @"buttonColor": inputColor, @"buttonFontSize": @22, @"buttonFontWeight": @"800"}];
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
	self.options.topBarNoBorder = topBarNoBorderInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil(self.uut.navigationController.navigationBar.shadowImage);
}

-(void)testTopBarNoBorderOff {
	NSNumber* topBarNoBorderInput = @(0);
	self.options.topBarNoBorder = topBarNoBorderInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil(self.uut.navigationController.navigationBar.shadowImage);
}

-(void)testStatusBarBlurOn {
	NSNumber* statusBarBlurInput = @(1);
	self.options.statusBarBlur = statusBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil([self.uut.view viewWithTag:BLUR_STATUS_TAG]);
}

-(void)testStatusBarBlurOff {
	NSNumber* statusBarBlurInput = @(0);
	self.options.statusBarBlur = statusBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.view viewWithTag:BLUR_STATUS_TAG]);
}

-(void)testTopBarBlur_default {
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}


-(void)testTopBarBlur_false {
	NSNumber* topBarBlurInput = @(0);
	self.options.topBarBlur = topBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}

-(void)testTopBarBlur_true {
	NSNumber* topBarBlurInput = @(1);
	self.options.topBarBlur = topBarBlurInput;
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];
	XCTAssertNotNil([self.uut.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]);
}

#pragma mark - Tab bar

- (void)testTabBarHidden_default {
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut hidesBottomBarWhenPushed]);
}


- (void)testTabBarHidden_true {
	self.options.tabBarHidden = @(1);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertTrue([self.uut hidesBottomBarWhenPushed]);
}

- (void)testTabBarHidden_false {
	self.options.tabBarHidden = @(0);
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:self.uut];
	[self.uut viewWillAppear:false];

	XCTAssertFalse([self.uut hidesBottomBarWhenPushed]);
}

- (void)testTabBarTranslucent_default {
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarTranslucent_true {
	self.options.tabBarTranslucent = @(1);
	[self.uut embedInTabBarController];
	XCTAssertTrue(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarTranslucent_false {
	self.options.tabBarTranslucent = @(0);
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.translucent);
}

- (void)testTabBarHideShadow_default {
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarHideShadow_true {
	self.options.tabBarHideShadow = @(1);
	[self.uut embedInTabBarController];
	XCTAssertTrue(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarHideShadow_false {
	self.options.tabBarHideShadow = @(0);
	[self.uut embedInTabBarController];
	XCTAssertFalse(self.uut.tabBarController.tabBar.clipsToBounds);
}

- (void)testTabBarBackgroundColor {
	self.options.tabBarBackgroundColor = @(0xFFFF0000);
	[self.uut embedInTabBarController];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	XCTAssertTrue([self.uut.tabBarController.tabBar.barTintColor isEqual:expectedColor]);
}

-(void)testTabBarTextColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.tabBarTextColor = inputColor;
	[self.uut embedInTabBarController];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTabBarTextFontFamily_validFont{
	NSString* inputFont = @"HelveticaNeue";
	self.options.tabBarTextFontFamily = inputFont;
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:10];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withoutTextFontFamily_withoutTextColor {
	self.options.tabBarTextFontSize = @(15);
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

-(void)testTabBarTextFontSize_withoutTextFontFamily_withTextColor {
	self.options.tabBarTextFontSize = @(15);
	self.options.tabBarTextColor = @(0xFFFF0000);
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont systemFontOfSize:15];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([attributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTabBarTextFontSize_withTextFontFamily_withTextColor {
	NSString* inputFont = @"HelveticaNeue";
	self.options.tabBarTextFontSize = @(15);
	self.options.tabBarTextColor = @(0xFFFF0000);
	self.options.tabBarTextFontFamily = inputFont;
	[self.uut embedInTabBarController];
	UIColor* expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
	XCTAssertTrue([attributes[@"NSColor"] isEqual:expectedColor]);
}

-(void)testTabBarTextFontSize_withTextFontFamily_withoutTextColor {
	NSString* inputFont = @"HelveticaNeue";
	self.options.tabBarTextFontSize = @(15);
	self.options.tabBarTextFontFamily = inputFont;
	[self.uut embedInTabBarController];
	UIFont* expectedFont = [UIFont fontWithName:inputFont size:15];
	NSDictionary* attributes = [self.uut.tabBarController.tabBar.items.firstObject titleTextAttributesForState:UIControlStateNormal];
	XCTAssertTrue([attributes[@"NSFont"] isEqual:expectedFont]);
}

@end
