#import "RCCViewController.h"
#import "RCCNavigationController.h"
#import "RCCTabBarController.h"
#import "RCCDrawerController.h"
#import "RCCTheSideBarManagerViewController.h"
#import <React/RCTRootView.h>
#import "RCCManager.h"
#import <React/RCTConvert.h>
#import <React/RCTEventDispatcher.h>
#import "RCCExternalViewControllerProtocol.h"
#import "RCTHelpers.h"
#import "RCCTitleViewHelper.h"
#import "RCCCustomTitleView.h"


NSString* const RCCViewControllerCancelReactTouchesNotification = @"RCCViewControllerCancelReactTouchesNotification";

const NSInteger BLUR_STATUS_TAG = 78264801;
const NSInteger BLUR_NAVBAR_TAG = 78264802;
const NSInteger TRANSPARENT_NAVBAR_TAG = 78264803;

@interface RCCViewController() <UIGestureRecognizerDelegate, UIViewControllerPreviewingDelegate>
@property (nonatomic) BOOL _hidesBottomBarWhenPushed;
@property (nonatomic) BOOL _statusBarHideWithNavBar;
@property (nonatomic) BOOL _statusBarHidden;
@property (nonatomic) BOOL _statusBarTextColorSchemeLight;
@property (nonatomic, strong) NSDictionary *originalNavBarImages;
@property (nonatomic, strong) UIImageView *navBarHairlineImageView;
@property (nonatomic, weak) id <UIGestureRecognizerDelegate> originalInteractivePopGestureDelegate;
@end

@implementation RCCViewController

-(UIImageView *)navBarHairlineImageView {
  if (!_navBarHairlineImageView) {
    _navBarHairlineImageView = [self findHairlineImageViewUnder:self.navigationController.navigationBar];
  }
  return _navBarHairlineImageView;
}

+ (UIViewController*)controllerWithLayout:(NSDictionary *)layout globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge
{
  UIViewController* controller = nil;
  if (!layout) return nil;
  
  // get props
  if (!layout[@"props"]) return nil;
  if (![layout[@"props"] isKindOfClass:[NSDictionary class]]) return nil;
  NSDictionary *props = layout[@"props"];
  
  // get children
  if (!layout[@"children"]) return nil;
  if (![layout[@"children"] isKindOfClass:[NSArray class]]) return nil;
  NSArray *children = layout[@"children"];
  
  // create according to type
  NSString *type = layout[@"type"];
  if (!type) return nil;
  
  // regular view controller
  if ([type isEqualToString:@"ViewControllerIOS"])
  {
    controller = [[RCCViewController alloc] initWithProps:props children:children globalProps:globalProps bridge:bridge];
  }
  
  // navigation controller
  if ([type isEqualToString:@"NavigationControllerIOS"])
  {
    controller = [[RCCNavigationController alloc] initWithProps:props children:children globalProps:globalProps bridge:bridge];
  }
  
  // tab bar controller
  if ([type isEqualToString:@"TabBarControllerIOS"])
  {
    controller = [[RCCTabBarController alloc] initWithProps:props children:children globalProps:globalProps bridge:bridge];
  }
  
  // side menu controller
  if ([type isEqualToString:@"DrawerControllerIOS"])
  {
    NSString *drawerType = props[@"type"];
    
    if ([drawerType isEqualToString:@"TheSideBar"]) {
      
      controller = [[RCCTheSideBarManagerViewController alloc] initWithProps:props children:children globalProps:globalProps bridge:bridge];
    }
    else {
      controller = [[RCCDrawerController alloc] initWithProps:props children:children globalProps:globalProps bridge:bridge];
    }
  }
  
  // register the controller if we have an id
  NSString *componentId = props[@"id"];
  if (controller && componentId)
  {
    [[RCCManager sharedInstance] registerController:controller componentId:componentId componentType:type];
    
    if([controller isKindOfClass:[RCCViewController class]])
    {
      ((RCCViewController*)controller).controllerId = componentId;
    }
  }
  
  // set background image at root level
  NSString *rootBackgroundImageName = props[@"style"][@"rootBackgroundImageName"];
  if (rootBackgroundImageName) {
    UIImage *image = [UIImage imageNamed: rootBackgroundImageName];
    UIImageView *imageView = [[UIImageView alloc] initWithImage:image];
    [controller.view insertSubview:imageView atIndex:0];
  }
  
  return controller;
}

-(NSDictionary*)addCommandTypeAndTimestampIfExists:(NSDictionary*)globalProps passProps:(NSDictionary*)passProps {
  NSMutableDictionary *modifiedPassProps = [NSMutableDictionary dictionaryWithDictionary:passProps];
  
  NSString *commandType = globalProps[GLOBAL_SCREEN_ACTION_COMMAND_TYPE];
  if (commandType) {
    modifiedPassProps[GLOBAL_SCREEN_ACTION_COMMAND_TYPE] = commandType;
  }
  
  NSString *timestamp = globalProps[GLOBAL_SCREEN_ACTION_TIMESTAMP];
  if (timestamp) {
    modifiedPassProps[GLOBAL_SCREEN_ACTION_TIMESTAMP] = timestamp;
  }
  return modifiedPassProps;
}



- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge
{
  NSString *component = props[@"component"];
  if (!component) return nil;
  
  NSDictionary *passProps = props[@"passProps"];
  NSDictionary *navigatorStyle = props[@"style"];
  
  NSMutableDictionary *mergedProps = [NSMutableDictionary dictionaryWithDictionary:globalProps];
  [mergedProps addEntriesFromDictionary:passProps];
  
  RCTRootView *reactView = [[RCTRootView alloc] initWithBridge:bridge moduleName:component initialProperties:mergedProps];
  if (!reactView) return nil;
  
  self = [super init];
  if (!self) return nil;
  
  [self commonInit:reactView navigatorStyle:navigatorStyle props:props];
  
  self.navigationController.interactivePopGestureRecognizer.delegate = self;
  
  return self;
}

- (instancetype)initWithComponent:(NSString *)component passProps:(NSDictionary *)passProps navigatorStyle:(NSDictionary*)navigatorStyle globalProps:(NSDictionary *)globalProps bridge:(RCTBridge *)bridge
{
  NSMutableDictionary *mergedProps = [NSMutableDictionary dictionaryWithDictionary:globalProps];
  [mergedProps addEntriesFromDictionary:passProps];
  
  RCTRootView *reactView = [[RCTRootView alloc] initWithBridge:bridge moduleName:component initialProperties:mergedProps];
  if (!reactView) return nil;
  
  self = [super init];
  if (!self) return nil;
  
  NSDictionary *modifiedPassProps = [self addCommandTypeAndTimestampIfExists:globalProps passProps:passProps];

  [self commonInit:reactView navigatorStyle:navigatorStyle props:modifiedPassProps];
  
  return self;
}

- (void)commonInit:(RCTRootView*)reactView navigatorStyle:(NSDictionary*)navigatorStyle props:(NSDictionary*)props
{
  self.view = reactView;
  
  self.edgesForExtendedLayout = UIRectEdgeNone; // default
  self.automaticallyAdjustsScrollViewInsets = NO; // default
  
  self.navigatorStyle = [NSMutableDictionary dictionaryWithDictionary:[[RCCManager sharedInstance] getAppStyle]];
  [self.navigatorStyle addEntriesFromDictionary:navigatorStyle];

  
  [self setStyleOnInit];
  
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onRNReload) name:RCTJavaScriptWillStartLoadingNotification object:nil];
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onCancelReactTouches) name:RCCViewControllerCancelReactTouchesNotification object:nil];
  
  self.commandType = props[GLOBAL_SCREEN_ACTION_COMMAND_TYPE];
  self.timestamp = props[GLOBAL_SCREEN_ACTION_TIMESTAMP];
  
  
  // In order to support 3rd party native ViewControllers, we support passing a class name as a prop named `ExternalNativeScreenClass`
  // In this case, we create an instance and add it as a child ViewController which preserves the VC lifecycle.
  // In case some props are necessary in the native ViewController, the ExternalNativeScreenProps can be used to pass them
  [self addExternalVCIfNecessary:props];
}

- (void)dealloc
{
  [[NSNotificationCenter defaultCenter] removeObserver:self];
  self.view = nil;
}

-(void)onRNReload
{
  [[NSNotificationCenter defaultCenter] removeObserver:self];
  self.view = nil;
}

-(void)onCancelReactTouches
{
  if ([self.view isKindOfClass:[RCTRootView class]]){
    [(RCTRootView*)self.view cancelTouches];
  }
}

- (void)sendScreenChangedEvent:(NSString *)eventName
{
  if ([self.view isKindOfClass:[RCTRootView class]]){
    
    RCTRootView *rootView = (RCTRootView *)self.view;
    
    if (rootView.appProperties && rootView.appProperties[@"navigatorEventID"]) {
      
      [[[RCCManager sharedInstance] getBridge].eventDispatcher sendAppEventWithName:rootView.appProperties[@"navigatorEventID"] body:@
       {
         @"type": @"ScreenChangedEvent",
         @"id": eventName
       }];
    }
  }
}

- (void)sendGlobalScreenEvent:(NSString *)eventName endTimestampString:(NSString *)endTimestampStr shouldReset:(BOOL)shouldReset {
  
  if (!self.commandType) return;
  
  if ([self.view isKindOfClass:[RCTRootView class]]){
    NSString *screenName = [((RCTRootView*)self.view) moduleName];
    
    [[[RCCManager sharedInstance] getBridge].eventDispatcher sendAppEventWithName:eventName body:@
     {
       @"commandType": self.commandType ? self.commandType : @"",
       @"screen": screenName ? screenName : @"",
       @"startTime": self.timestamp ? self.timestamp : @"",
       @"endTime": endTimestampStr ? endTimestampStr : @""
     }];
    
    if (shouldReset) {
      self.commandType = nil;
      self.timestamp = nil;
    }
  }
}


-(BOOL)isDisappearTriggeredFromPop:(NSString *)eventName {

  NSArray *navigationViewControllers = self.navigationController.viewControllers;
  
  if (navigationViewControllers.lastObject == self || [navigationViewControllers indexOfObject:self] == NSNotFound) {
    return YES;
  }
  return NO;
}

- (NSString *)getTimestampString {
  long long milliseconds = (long long)([[NSDate date] timeIntervalSince1970] * 1000.0);
  return [NSString stringWithFormat:@"%lld", milliseconds];
}

// This is walk around for React-Native bug.
// https://github.com/wix/react-native-navigation/issues/1446
//
// Buttons in ScrollView after changing route/pushing/showing modal
// while there is a momentum scroll are not clickable.
// Back to normal after user start scroll with momentum
- (void)_traverseAndCall:(UIView*)view
{
  if([view isKindOfClass:[UIScrollView class]] && ([[(UIScrollView*)view delegate] respondsToSelector:@selector(scrollViewDidEndDecelerating:)]) ) {
    dispatch_async(dispatch_get_main_queue(), ^{
      [[(UIScrollView*)view delegate] scrollViewDidEndDecelerating:(id)view];
    });
  
  }
  
  [view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
    [self _traverseAndCall:obj];
  }];
}

// fix iOS11 safeArea - https://github.com/facebook/react-native/issues/15681
// rnn issue - https://github.com/wix/react-native-navigation/issues/1858
- (void)_traverseAndFixScrollViewSafeArea:(UIView *)view {
#ifdef __IPHONE_11_0
  if ([view isKindOfClass:UIScrollView.class] && [view respondsToSelector:@selector(setContentInsetAdjustmentBehavior:)]) {
    [((UIScrollView*)view) setContentInsetAdjustmentBehavior:UIScrollViewContentInsetAdjustmentNever];
  }
  
  [view.subviews enumerateObjectsUsingBlock:^(__kindof UIView * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
    [self _traverseAndFixScrollViewSafeArea:obj];
  }];
#endif
  
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
  [self sendGlobalScreenEvent:@"didAppear" endTimestampString:[self getTimestampString] shouldReset:YES];
  [self sendScreenChangedEvent:@"didAppear"];
  
}

- (void)viewWillAppear:(BOOL)animated
{
  [super viewWillAppear:animated];
  [self _traverseAndFixScrollViewSafeArea:self.view];
  [self sendGlobalScreenEvent:@"willAppear" endTimestampString:[self getTimestampString] shouldReset:NO];
  [self sendScreenChangedEvent:@"willAppear"];
  [self setStyleOnAppear];
}

- (void)viewDidDisappear:(BOOL)animated
{
  [self _traverseAndCall:self.view];
  [super viewDidDisappear:animated];
  [self sendGlobalScreenEvent:@"didDisappear" endTimestampString:[self getTimestampString] shouldReset:YES];
  [self sendScreenChangedEvent:@"didDisappear"];
}

- (void)viewWillDisappear:(BOOL)animated
{
  [super viewWillDisappear:animated];
  [self sendGlobalScreenEvent:@"willDisappear" endTimestampString:[self getTimestampString] shouldReset:NO];
  [self sendScreenChangedEvent:@"willDisappear"];
  [self setStyleOnDisappear];
}

// most styles should be set here since when we pop a view controller that changed them
// we want to reset the style to what we expect (so we need to reset on every willAppear)
- (void)setStyleOnAppear
{
  [self setStyleOnAppearForViewController:self appeared:false];
}

- (void)updateStyle
{
  [self setStyleOnAppearForViewController:self appeared:true];
}

-(void)setStyleOnAppearForViewController:(UIViewController*)viewController appeared:(BOOL)appeared
{
  
  NSString *screenBackgroundColor = self.navigatorStyle[@"screenBackgroundColor"];
  if (screenBackgroundColor) {
    
    UIColor *color = screenBackgroundColor != (id)[NSNull null] ? [RCTConvert UIColor:screenBackgroundColor] : nil;
    viewController.view.backgroundColor = color;
  }
  
  NSString *screenBackgroundImageName = self.navigatorStyle[@"screenBackgroundImageName"];
  if (screenBackgroundImageName) {
    
    UIImage *image = [UIImage imageNamed: screenBackgroundImageName];
    viewController.view.layer.contents = (__bridge id _Nullable)(image.CGImage);
  }
  
  NSString *navBarBackgroundColor = self.navigatorStyle[@"navBarBackgroundColor"];
  if (navBarBackgroundColor) {
    
    UIColor *color = navBarBackgroundColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarBackgroundColor] : nil;
    viewController.navigationController.navigationBar.barTintColor = color;
    
  } else {
    viewController.navigationController.navigationBar.barTintColor = nil;
  }
  
  NSMutableDictionary *titleTextAttributes = [RCTHelpers textAttributesFromDictionary:self.navigatorStyle withPrefix:@"navBarText" baseFont:[UIFont boldSystemFontOfSize:17]];
  [self.navigationController.navigationBar setTitleTextAttributes:titleTextAttributes];
  
  NSMutableDictionary *navButtonTextAttributes = [RCTHelpers textAttributesFromDictionary:self.navigatorStyle withPrefix:@"navBarButton"];
  NSMutableDictionary *leftNavButtonTextAttributes = [RCTHelpers textAttributesFromDictionary:self.navigatorStyle withPrefix:@"navBarLeftButton"];
  NSMutableDictionary *rightNavButtonTextAttributes = [RCTHelpers textAttributesFromDictionary:self.navigatorStyle withPrefix:@"navBarRightButton"];
  
  if (
      navButtonTextAttributes.allKeys.count > 0 ||
      leftNavButtonTextAttributes.allKeys.count > 0 ||
      rightNavButtonTextAttributes.allKeys.count > 0
      ) {
    
    for (UIBarButtonItem *item in viewController.navigationItem.leftBarButtonItems) {
      if (leftNavButtonTextAttributes.allKeys.count > 0) {
        NSDictionary *previousAttributes = [item titleTextAttributesForState:UIControlStateNormal];
        NSMutableDictionary *mergedAttributes;

        if (leftNavButtonTextAttributes.allKeys.count > 0) {
          mergedAttributes = [leftNavButtonTextAttributes mutableCopy];
        } else {
          mergedAttributes = [navButtonTextAttributes mutableCopy];
        }

        [mergedAttributes addEntriesFromDictionary:previousAttributes];

        [item setTitleTextAttributes:[mergedAttributes copy] forState:UIControlStateNormal];
        [item setTitleTextAttributes:[mergedAttributes copy] forState:UIControlStateHighlighted];
      }
    }

    for (UIBarButtonItem *item in viewController.navigationItem.rightBarButtonItems) {
      NSDictionary *previousAttributes = [item titleTextAttributesForState:UIControlStateNormal];
      NSMutableDictionary *mergedAttributes;

      if (rightNavButtonTextAttributes.allKeys.count > 0) {
        mergedAttributes = [rightNavButtonTextAttributes mutableCopy];
      } else {
        mergedAttributes = [navButtonTextAttributes mutableCopy];
      }

      [mergedAttributes addEntriesFromDictionary:previousAttributes];

      [item setTitleTextAttributes:[mergedAttributes copy] forState:UIControlStateNormal];
      [item setTitleTextAttributes:[mergedAttributes copy] forState:UIControlStateHighlighted];
    }

    // At the moment, this seems to be the only thing that gets the back button correctly
    [navButtonTextAttributes removeObjectForKey:NSForegroundColorAttributeName];
    [[UIBarButtonItem appearance] setTitleTextAttributes:navButtonTextAttributes forState:UIControlStateNormal];
    [[UIBarButtonItem appearance] setTitleTextAttributes:navButtonTextAttributes forState:UIControlStateHighlighted];
  }
  
  NSString *navBarButtonColor = self.navigatorStyle[@"navBarButtonColor"];
  if (navBarButtonColor) {
    
    UIColor *color = navBarButtonColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarButtonColor] : nil;
    viewController.navigationController.navigationBar.tintColor = color;
    
  } else
  {
    viewController.navigationController.navigationBar.tintColor = nil;
  }
  
  BOOL topBarElevationShadowEnabled = self.navigatorStyle[@"topBarElevationShadowEnabled"] != (id)[NSNull null] ? [RCTConvert CGFloat:self.navigatorStyle[@"topBarElevationShadowEnabled"]] : NO;

  if (topBarElevationShadowEnabled) {
    CGFloat shadowOpacity = self.navigatorStyle[@"topBarShadowOpacity"] != 0 ? [RCTConvert CGFloat:self.navigatorStyle[@"topBarShadowOpacity"]] : 0.2;
    CGFloat shadowOffset = self.navigatorStyle[@"topBarShadowOffset"] != 0 ? [RCTConvert CGFloat:self.navigatorStyle[@"topBarShadowOffset"]] : 3.0;
    CGFloat shadowRadius = self.navigatorStyle[@"topBarShadowRadius"] != 0 ? [RCTConvert CGFloat:self.navigatorStyle[@"topBarShadowRadius"]] : 2.0;
    UIColor *shadowColor = self.navigatorStyle[@"topBarShadowColor"] != (id)[NSNull null] ? [RCTConvert UIColor:self.navigatorStyle[@"topBarShadowColor"]] : UIColor.blackColor;

    viewController.navigationController.navigationBar.layer.shadowOpacity = shadowOpacity;
    viewController.navigationController.navigationBar.layer.shadowColor = shadowColor.CGColor;
    viewController.navigationController.navigationBar.layer.shadowOffset = CGSizeMake(0, shadowOffset);
    viewController.navigationController.navigationBar.layer.shadowRadius = shadowRadius;
  }

  BOOL viewControllerBasedStatusBar = false;
  
  NSObject *viewControllerBasedStatusBarAppearance = [[NSBundle mainBundle] infoDictionary][@"UIViewControllerBasedStatusBarAppearance"];
  if (viewControllerBasedStatusBarAppearance && [viewControllerBasedStatusBarAppearance isKindOfClass:[NSNumber class]]) {
    viewControllerBasedStatusBar = [(NSNumber *)viewControllerBasedStatusBarAppearance boolValue];
  }
  
  NSString *statusBarTextColorSchemeSingleScreen = self.navigatorStyle[@"statusBarTextColorSchemeSingleScreen"];
  NSString *statusBarTextColorScheme = self.navigatorStyle[@"statusBarTextColorScheme"];
  NSString *finalColorScheme = statusBarTextColorSchemeSingleScreen ? : statusBarTextColorScheme;
  
  if (finalColorScheme && [finalColorScheme isEqualToString:@"light"]) {
    
    if (!statusBarTextColorSchemeSingleScreen) {
      viewController.navigationController.navigationBar.barStyle = UIBarStyleBlack;
    }
    
    self._statusBarTextColorSchemeLight = true;
    if (!viewControllerBasedStatusBarAppearance) {
      [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    }
    
    [viewController setNeedsStatusBarAppearanceUpdate];
    
  } else {
    
    if (!statusBarTextColorSchemeSingleScreen) {
      viewController.navigationController.navigationBar.barStyle = UIBarStyleDefault;
    }
    
    self._statusBarTextColorSchemeLight = false;
    
    if (!viewControllerBasedStatusBarAppearance) {
      [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault];
    }
    [viewController setNeedsStatusBarAppearanceUpdate];
  }
  
  NSNumber *tabBarHidden = self.navigatorStyle[@"tabBarHidden"];
  BOOL tabBarHiddenBool = tabBarHidden ? [tabBarHidden boolValue] : NO;
  if (tabBarHiddenBool) {
    UITabBar *tabBar = viewController.tabBarController.tabBar;
    tabBar.transform = CGAffineTransformMakeTranslation(0, tabBar.frame.size.height);
  }

  NSNumber *navBarHidden = self.navigatorStyle[@"navBarHidden"];
  BOOL navBarHiddenBool = navBarHidden ? [navBarHidden boolValue] : NO;
  if (viewController.navigationController.navigationBarHidden != navBarHiddenBool) {
    [viewController.navigationController setNavigationBarHidden:navBarHiddenBool animated:YES];
  }
  
  NSNumber *navBarHideOnScroll = self.navigatorStyle[@"navBarHideOnScroll"];
  BOOL navBarHideOnScrollBool = navBarHideOnScroll ? [navBarHideOnScroll boolValue] : NO;
  if (navBarHideOnScrollBool) {
    viewController.navigationController.hidesBarsOnSwipe = YES;
  } else {
    viewController.navigationController.hidesBarsOnSwipe = NO;
  }
  
  NSNumber *statusBarBlur = self.navigatorStyle[@"statusBarBlur"];
  BOOL statusBarBlurBool = statusBarBlur ? [statusBarBlur boolValue] : NO;
  if (statusBarBlurBool && ![viewController.view viewWithTag:BLUR_STATUS_TAG]) {
    
    UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
    blur.frame = [[UIApplication sharedApplication] statusBarFrame];
    blur.tag = BLUR_STATUS_TAG;
    [viewController.view insertSubview:blur atIndex:0];
  }
  
  NSNumber *navBarBlur = self.navigatorStyle[@"navBarBlur"];
  BOOL navBarBlurBool = navBarBlur ? [navBarBlur boolValue] : NO;
  if (navBarBlurBool) {
    
    if (![viewController.navigationController.navigationBar viewWithTag:BLUR_NAVBAR_TAG]) {
      [self storeOriginalNavBarImages];
      
      [viewController.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
      viewController.navigationController.navigationBar.shadowImage = [UIImage new];
      UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
      CGRect statusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
      blur.frame = CGRectMake(0, -1 * statusBarFrame.size.height, viewController.navigationController.navigationBar.frame.size.width, viewController.navigationController.navigationBar.frame.size.height + statusBarFrame.size.height);
      blur.userInteractionEnabled = NO;
      blur.tag = BLUR_NAVBAR_TAG;
      [viewController.navigationController.navigationBar insertSubview:blur atIndex:0];
      [viewController.navigationController.navigationBar sendSubviewToBack:blur];
    }
    
  } else {
    
    UIView *blur = [viewController.navigationController.navigationBar viewWithTag:BLUR_NAVBAR_TAG];
    if (blur) {
      [blur removeFromSuperview];
      [viewController.navigationController.navigationBar setBackgroundImage:self.originalNavBarImages[@"bgImage"] forBarMetrics:UIBarMetricsDefault];
      viewController.navigationController.navigationBar.shadowImage = self.originalNavBarImages[@"shadowImage"];
      self.originalNavBarImages = nil;
    }
  }
  
  NSNumber *navBarTransparent = self.navigatorStyle[@"navBarTransparent"];
  BOOL navBarTransparentBool = navBarTransparent ? [navBarTransparent boolValue] : NO;
  
  void (^action)() = ^ {
    if (navBarTransparentBool)
    {
      if (![viewController.navigationController.navigationBar viewWithTag:TRANSPARENT_NAVBAR_TAG])
      {
        [self storeOriginalNavBarImages];
        
        [viewController.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
        viewController.navigationController.navigationBar.shadowImage = [UIImage new];
        UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
        transparentView.tag = TRANSPARENT_NAVBAR_TAG;
        [viewController.navigationController.navigationBar insertSubview:transparentView atIndex:0];
      }
    }
    else
    {
      UIView *transparentView = [viewController.navigationController.navigationBar viewWithTag:TRANSPARENT_NAVBAR_TAG];
      if (transparentView)
      {
        [transparentView removeFromSuperview];
        [viewController.navigationController.navigationBar setBackgroundImage:self.originalNavBarImages[@"bgImage"] forBarMetrics:UIBarMetricsDefault];
        viewController.navigationController.navigationBar.shadowImage = self.originalNavBarImages[@"shadowImage"];
        self.originalNavBarImages = nil;
      }
    }
  };
  
  if (!self.transitionCoordinator || self.transitionCoordinator.initiallyInteractive || !navBarTransparentBool || appeared) {
    action();
  } else {
    UIView* backgroundView = [self.navigationController.navigationBar valueForKey:@"backgroundView"];
    CGFloat originalAlpha = backgroundView.alpha;
    backgroundView.alpha = navBarTransparentBool ? 0.0 : 1.0;
    [self.transitionCoordinator animateAlongsideTransition:nil completion:^(id<UIViewControllerTransitionCoordinatorContext>  _Nonnull context) {
      action();
      backgroundView.alpha = originalAlpha;
    }];
  }
  
  NSNumber *autoAdjustsScrollViewInsets = self.navigatorStyle[@"autoAdjustScrollViewInsets"];
  viewController.automaticallyAdjustsScrollViewInsets = autoAdjustsScrollViewInsets ? [autoAdjustsScrollViewInsets boolValue] : false;
  
  NSNumber *navBarTranslucent = self.navigatorStyle[@"navBarTranslucent"];
  BOOL navBarTranslucentBool = navBarTranslucent ? [navBarTranslucent boolValue] : NO;
  if (navBarTranslucentBool || navBarBlurBool) {
    viewController.navigationController.navigationBar.translucent = YES;
  } else {
    viewController.navigationController.navigationBar.translucent = NO;
  }
  
  NSNumber *extendedLayoutIncludesOpaqueBars = self.navigatorStyle[@"extendedLayoutIncludesOpaqueBars"];
  BOOL extendedLayoutIncludesOpaqueBarsBool = extendedLayoutIncludesOpaqueBars ? [extendedLayoutIncludesOpaqueBars boolValue] : NO;
  viewController.extendedLayoutIncludesOpaqueBars = extendedLayoutIncludesOpaqueBarsBool;
  
  NSNumber *drawUnderNavBar = self.navigatorStyle[@"drawUnderNavBar"];
  BOOL drawUnderNavBarBool = drawUnderNavBar ? [drawUnderNavBar boolValue] : NO;
  if (drawUnderNavBarBool) {
    viewController.edgesForExtendedLayout |= UIRectEdgeTop;
  }
  else {
    viewController.edgesForExtendedLayout &= ~UIRectEdgeTop;
  }
  
  NSNumber *drawUnderTabBar = self.navigatorStyle[@"drawUnderTabBar"];
  BOOL drawUnderTabBarBool = drawUnderTabBar ? [drawUnderTabBar boolValue] : NO;
  if (drawUnderTabBarBool) {
    viewController.edgesForExtendedLayout |= UIRectEdgeBottom;
  } else {
    viewController.edgesForExtendedLayout &= ~UIRectEdgeBottom;
  }
  
  NSNumber *removeNavBarBorder = self.navigatorStyle[@"navBarNoBorder"];
  BOOL removeNavBarBorderBool = removeNavBarBorder ? [removeNavBarBorder boolValue] : NO;
  if (removeNavBarBorderBool) {
    self.navBarHairlineImageView.hidden = YES;
  } else {
    self.navBarHairlineImageView.hidden = NO;
  }
  
 //Bug fix: in case there is a interactivePopGestureRecognizer, it prevents react-native from getting touch events on the left screen area that the gesture handles
 //overriding the delegate of the gesture prevents this from happening while keeping the gesture intact (another option was to disable it completely by demand)
 if(self.navigationController.viewControllers.count > 1){
   if (self.navigationController != nil && self.navigationController.interactivePopGestureRecognizer != nil)
   {
     id <UIGestureRecognizerDelegate> interactivePopGestureRecognizer = self.navigationController.interactivePopGestureRecognizer.delegate;
     if (interactivePopGestureRecognizer != nil && interactivePopGestureRecognizer != self)
     {
       self.originalInteractivePopGestureDelegate = interactivePopGestureRecognizer;
       self.navigationController.interactivePopGestureRecognizer.delegate = self;
     }
   }
 }
  
  NSString *navBarCustomView = self.navigatorStyle[@"navBarCustomView"];
  if (navBarCustomView && ![self.navigationItem.titleView isKindOfClass:[RCCCustomTitleView class]]) {
    if ([self.view isKindOfClass:[RCTRootView class]]) {
      
      RCTBridge *bridge = ((RCTRootView*)self.view).bridge;
      
      NSDictionary *initialProps = self.navigatorStyle[@"navBarCustomViewInitialProps"];
      RCTRootView *reactView = [[RCTRootView alloc] initWithBridge:bridge moduleName:navBarCustomView initialProperties:initialProps];
      
      RCCCustomTitleView *titleView = [[RCCCustomTitleView alloc] initWithFrame:self.navigationController.navigationBar.bounds subView:reactView alignment:self.navigatorStyle[@"navBarComponentAlignment"]];
      titleView.backgroundColor = [UIColor clearColor];
      reactView.backgroundColor = [UIColor clearColor];
      
      self.navigationItem.titleView = titleView;
      
      self.navigationItem.titleView.backgroundColor = [UIColor clearColor];
      self.navigationItem.titleView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
      self.navigationItem.titleView.clipsToBounds = YES;
    }
  }
}


-(void)storeOriginalNavBarImages {
  
  NSMutableDictionary *originalNavBarImages = [@{} mutableCopy];
  UIImage *bgImage = [self.navigationController.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault];
  if (bgImage != nil) {
    originalNavBarImages[@"bgImage"] = bgImage;
  }
  UIImage *shadowImage = self.navigationController.navigationBar.shadowImage;
  if (shadowImage != nil) {
    originalNavBarImages[@"shadowImage"] = shadowImage;
  }
  self.originalNavBarImages = originalNavBarImages;
  
}

-(void)setStyleOnDisappear {
  self.navBarHairlineImageView.hidden = NO;
  
  if (self.navigationController != nil && self.navigationController.interactivePopGestureRecognizer != nil && self.originalInteractivePopGestureDelegate != nil)
  {
    self.navigationController.interactivePopGestureRecognizer.delegate = self.originalInteractivePopGestureDelegate;
    self.originalInteractivePopGestureDelegate = nil;
  }
}

// only styles that can't be set on willAppear should be set here
- (void)setStyleOnInit
{
  NSNumber *tabBarHidden = self.navigatorStyle[@"tabBarHidden"];
  BOOL tabBarHiddenBool = tabBarHidden ? [tabBarHidden boolValue] : NO;
  if (tabBarHiddenBool) {
    self._hidesBottomBarWhenPushed = YES;
  } else {
    self._hidesBottomBarWhenPushed = NO;
  }
  
  NSNumber *statusBarHideWithNavBar = self.navigatorStyle[@"statusBarHideWithNavBar"];
  BOOL statusBarHideWithNavBarBool = statusBarHideWithNavBar ? [statusBarHideWithNavBar boolValue] : NO;
  if (statusBarHideWithNavBarBool) {
    self._statusBarHideWithNavBar = YES;
  } else {
    self._statusBarHideWithNavBar = NO;
  }
  
  NSNumber *statusBarHidden = self.navigatorStyle[@"statusBarHidden"];
  BOOL statusBarHiddenBool = statusBarHidden ? [statusBarHidden boolValue] : NO;
  if (statusBarHiddenBool) {
    self._statusBarHidden = YES;
  } else {
    self._statusBarHidden = NO;
  }
}

- (BOOL)hidesBottomBarWhenPushed
{
  if (!self._hidesBottomBarWhenPushed) return NO;
  return (self.navigationController.topViewController == self) && ![(RCCTabBarController*)self.tabBarController tabBarHidden];
}

- (BOOL)prefersStatusBarHidden
{
  if (self._statusBarHidden) {
    return YES;
  }
  
  if (self._statusBarHideWithNavBar) {
    return self.navigationController.isNavigationBarHidden;
  } else {
    return NO;
  }
}

- (void)setNavBarVisibilityChange:(BOOL)animated {
  [self.navigationController setNavigationBarHidden:[self.navigatorStyle[@"navBarHidden"] boolValue] animated:animated];
}


- (UIStatusBarStyle)preferredStatusBarStyle
{
  if (self._statusBarTextColorSchemeLight){
    return UIStatusBarStyleLightContent;
  } else {
    return UIStatusBarStyleDefault;
  }
}

- (UIImageView *)findHairlineImageViewUnder:(UIView *)view {
  if ([view isKindOfClass:UIImageView.class] && view.bounds.size.height <= 1.0) {
    return (UIImageView *)view;
  }
  for (UIView *subview in view.subviews) {
    UIImageView *imageView = [self findHairlineImageViewUnder:subview];
    if (imageView) {
      return imageView;
    }
  }
  return nil;
}

-(void)addExternalVCIfNecessary:(NSDictionary*)props
{
  NSString *externalScreenClass = props[@"externalNativeScreenClass"];
  if (externalScreenClass != nil)
  {
    Class class = NSClassFromString(externalScreenClass);
    if (class != NULL)
    {
      id obj = [[class alloc] init];
      if (obj != nil && [obj isKindOfClass:[UIViewController class]] && [obj conformsToProtocol:@protocol(RCCExternalViewControllerProtocol)])
      {
        ((id <RCCExternalViewControllerProtocol>)obj).controllerDelegate = self;
        [obj setProps:props[@"externalNativeScreenProps"]];
        
        UIViewController *viewController = (UIViewController*)obj;
        [self addChildViewController:viewController];
        viewController.view.frame = self.view.bounds;
        [self.view addSubview:viewController.view];
        [viewController didMoveToParentViewController:self];
      }
      else
      {
        NSLog(@"addExternalVCIfNecessary: could not create instance. Make sure that your class is a UIViewController whihc confirms to RCCExternalViewControllerProtocol");
      }
    }
    else
    {
      NSLog(@"addExternalVCIfNecessary: could not create class from string. Check that the proper class name wass passed in ExternalNativeScreenClass");
    }
  }
}

#pragma mark - Preview Actions

- (void)onActionPress:(NSString *)id {
  if ([self.view isKindOfClass:[RCTRootView class]]) {
    RCTRootView *rootView = (RCTRootView *)self.view;
    if (rootView.appProperties && rootView.appProperties[@"navigatorEventID"]) {
      [[[RCCManager sharedInstance] getBridge].eventDispatcher
       sendAppEventWithName:rootView.appProperties[@"navigatorEventID"]
       body:@{
              @"type": @"PreviewActionPress",
              @"id": id
              }];
    }
  }
}

- (UIPreviewAction *) convertAction:(NSDictionary *)action {
  NSString *actionId = action[@"id"];
  NSString *actionTitle = action[@"title"];
  UIPreviewActionStyle actionStyle = UIPreviewActionStyleDefault;
  if ([action[@"style"] isEqualToString:@"selected"]) {
    actionStyle = UIPreviewActionStyleSelected;
  }
  if ([action[@"style"] isEqualToString:@"destructive"]) {
    actionStyle = UIPreviewActionStyleDestructive;
  }
  return [UIPreviewAction actionWithTitle:actionTitle style:actionStyle handler:^(UIPreviewAction * _Nonnull action, UIViewController * _Nonnull previewViewController) {
    [self onActionPress:actionId];
  }];
}

#pragma mark - NewRelic

- (NSString*) customNewRelicInteractionName
{
  NSString *interactionName = nil;
  
  if (self.view != nil && [self.view isKindOfClass:[RCTRootView class]])
  {
    NSString *moduleName = ((RCTRootView*)self.view).moduleName;
    if(moduleName != nil)
    {
      interactionName = [NSString stringWithFormat:@"RCCViewController: %@", moduleName];
    }
  }
  
  if (interactionName == nil)
  {
    interactionName = [NSString stringWithFormat:@"RCCViewController with title: %@", self.title];
  }
  
  return interactionName;
}

#pragma mark - UIGestureRecognizerDelegate
-(BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
  NSNumber *disabledBackGesture = self.navigatorStyle[@"disabledBackGesture"];
  BOOL disabledBackGestureBool = disabledBackGesture ? [disabledBackGesture boolValue] : NO;
  return !disabledBackGestureBool;
}

-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
  NSNumber *disabledSimultaneousGesture = self.navigatorStyle[@"disabledSimultaneousGesture"];
  BOOL disabledSimultaneousGestureBool = disabledSimultaneousGesture ? [disabledSimultaneousGesture boolValue] : YES; // make default value of disabledSimultaneousGesture is true
  return !disabledSimultaneousGestureBool;
}

#pragma mark - UIViewControllerPreviewingDelegate
- (UIViewController *)previewingContext:(id<UIViewControllerPreviewing>)previewingContext viewControllerForLocation:(CGPoint)location {
  return self.previewController;
}

- (void)previewingContext:(id<UIViewControllerPreviewing>)previewingContext commitViewController:(UIViewController *)viewControllerToCommit {
  if (self.previewController.previewCommit == YES) {
    [self.previewController sendGlobalScreenEvent:@"willCommitPreview" endTimestampString:[self.previewController getTimestampString] shouldReset:YES];
    [self.previewController sendScreenChangedEvent:@"willCommitPreview"];
    [self.navigationController pushViewController:self.previewController animated:false];
  }
}

- (NSArray<id<UIPreviewActionItem>> *)previewActionItems {
  NSMutableArray *actions = [[NSMutableArray alloc] init];
  for (NSDictionary *previewAction in self.previewActions) {
    UIPreviewAction *action = [self convertAction:previewAction];
    NSDictionary *actionActions = previewAction[@"actions"];
    if (actionActions.count > 0) {
      NSMutableArray *group = [[NSMutableArray alloc] init];
      for (NSDictionary *previewGroupAction in actionActions) {
        [group addObject:[self convertAction:previewGroupAction]];
      }
      UIPreviewActionGroup *actionGroup = [UIPreviewActionGroup actionGroupWithTitle:action.title style:UIPreviewActionStyleDefault actions:group];
      [actions addObject:actionGroup];
    } else {
      [actions addObject:action];
    }
  }
  return actions;
}

@end
