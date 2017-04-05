#import "RCCNavigationController.h"
#import "RCCViewController.h"
#import "RCCManager.h"
#import <React/RCTEventDispatcher.h>
#import <React/RCTConvert.h>
#import <objc/runtime.h>
#import "RCCTitleViewHelper.h"
#import "UIViewController+Rotation.h"
#import "RCTHelpers.h"

@implementation RCCNavigationController

NSString *const NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY = @"RCCNavigationController.CALLBACK_ASSOCIATED_KEY";
NSString *const NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY = @"RCCNavigationController.CALLBACK_ASSOCIATED_ID";


-(UIInterfaceOrientationMask)supportedInterfaceOrientations {
  return [self supportedControllerOrientations];
}

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge
{
  NSString *component = props[@"component"];
  if (!component) return nil;
  
  NSDictionary *passProps = props[@"passProps"];
  NSDictionary *navigatorStyle = props[@"style"];
  
  RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:globalProps bridge:bridge];
  if (!viewController) return nil;
  
  NSArray *leftButtons = props[@"leftButtons"];
  if (leftButtons)
  {
    [self setButtons:leftButtons viewController:viewController side:@"left" animated:NO];
  }
  
  NSArray *rightButtons = props[@"rightButtons"];
  if (rightButtons)
  {
    [self setButtons:rightButtons viewController:viewController side:@"right" animated:NO];
  }
  
  self = [super initWithRootViewController:viewController];
  if (!self) return nil;
  self.delegate = self;
  
  self.navigationBar.translucent = NO; // default
  
  [self processTitleView:viewController
                   props:props
                   style:navigatorStyle];
  

  [self setRotation:props];
  
  return self;
}


- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge
{
  BOOL animated = actionParams[@"animated"] ? [actionParams[@"animated"] boolValue] : YES;
  
  // push
  if ([performAction isEqualToString:@"push"])
  {
    NSString *component = actionParams[@"component"];
    if (!component) return;
    
    NSDictionary *passProps = actionParams[@"passProps"];
    NSDictionary *navigatorStyle = actionParams[@"style"];
    
    // merge the navigatorStyle of our parent
    if ([self.topViewController isKindOfClass:[RCCViewController class]])
    {
      RCCViewController *parent = (RCCViewController*)self.topViewController;
      NSMutableDictionary *mergedStyle = [NSMutableDictionary dictionaryWithDictionary:parent.navigatorStyle];
      
      // there are a few styles that we don't want to remember from our parent (they should be local)
      
      NSArray <NSString *> *localStyleKeys = @[
          @"navBarHidden",
          @"statusBarHidden",
          @"navBarHideOnScroll",
          @"drawUnderNavBar",
          @"drawUnderTabBar",
          @"statusBarBlur",
          @"navBarBlur",
          @"navBarTranslucent",
          @"statusBarHideWithNavBar",
          @"autoAdjustScrollViewInsets",
          @"statusBarTextColorSchemeSingleScreen",
		  @"disabledBackGesture",
          @"navBarButtonBorderRadius",
          @"navBarButtonBorderWidth",
          @"navBarButtonBorderColor",
          @"navBarButtonPadding",
          @"navBarButtonLeftBorderRadius",
          @"navBarButtonLeftBorderWidth",
          @"navBarButtonLeftBorderColor",
          @"navBarButtonLeftPadding",
          @"navBarButtonRightBorderRadius",
          @"navBarButtonRightBorderWidth",
          @"navBarButtonRightBorderColor",
          @"navBarButtonRightPadding"
      ];
      
      [localStyleKeys enumerateObjectsUsingBlock:^(NSString * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [mergedStyle removeObjectForKey:obj];
      }];
      
      [[RCTHelpers textAttributeKeys] enumerateObjectsUsingBlock:^(NSString * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        
        NSString *key = [obj stringByReplacingCharactersInRange:NSMakeRange(0, 1) withString:[obj substringToIndex:1].capitalizedString];
        [mergedStyle removeObjectForKey:[NSString stringWithFormat:@"navBarButtonLeft%@", key]];
        [mergedStyle removeObjectForKey:[NSString stringWithFormat:@"navBarButtonRight%@", key]];
      }];
      
      [mergedStyle addEntriesFromDictionary:navigatorStyle];
      navigatorStyle = mergedStyle;
    }
    
    RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:nil bridge:bridge];
    
    [self processTitleView:viewController
                     props:actionParams
                     style:navigatorStyle];
    
    NSString *backButtonTitle = actionParams[@"backButtonTitle"];
    if (backButtonTitle)
    {
      UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithTitle:backButtonTitle
                                                                   style:UIBarButtonItemStylePlain
                                                                  target:nil
                                                                  action:nil];
      
      self.topViewController.navigationItem.backBarButtonItem = backItem;
    }
    else
    {
      self.topViewController.navigationItem.backBarButtonItem = nil;
    }
    
    NSNumber *backButtonHidden = actionParams[@"backButtonHidden"];
    BOOL backButtonHiddenBool = backButtonHidden ? [backButtonHidden boolValue] : NO;
    if (backButtonHiddenBool)
    {
      viewController.navigationItem.hidesBackButton = YES;
    }
    
    NSArray *leftButtons = actionParams[@"leftButtons"];
    if (leftButtons)
    {
      [self setButtons:leftButtons viewController:viewController side:@"left" animated:NO];
    }
    
    NSArray *rightButtons = actionParams[@"rightButtons"];
    if (rightButtons)
    {
      [self setButtons:rightButtons viewController:viewController side:@"right" animated:NO];
    }
    
    [self pushViewController:viewController animated:animated];
    return;
  }
  
  // pop
  if ([performAction isEqualToString:@"pop"])
  {
    [self popViewControllerAnimated:animated];
    return;
  }
  
  // popToRoot
  if ([performAction isEqualToString:@"popToRoot"])
  {
    [self popToRootViewControllerAnimated:animated];
    return;
  }
  
  // resetTo
  if ([performAction isEqualToString:@"resetTo"])
  {
    NSString *component = actionParams[@"component"];
    if (!component) return;
    
    NSDictionary *passProps = actionParams[@"passProps"];
    NSDictionary *navigatorStyle = actionParams[@"style"];
    
    RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:nil bridge:bridge];
    
    [self processTitleView:viewController
                     props:actionParams
                     style:navigatorStyle];
    NSArray *leftButtons = actionParams[@"leftButtons"];
    if (leftButtons)
    {
      [self setButtons:leftButtons viewController:viewController side:@"left" animated:NO];
    }
    
    NSArray *rightButtons = actionParams[@"rightButtons"];
    if (rightButtons)
    {
      [self setButtons:rightButtons viewController:viewController side:@"right" animated:NO];
    }
    
    BOOL animated = actionParams[@"animated"] ? [actionParams[@"animated"] boolValue] : YES;
    
    [self setViewControllers:@[viewController] animated:animated];
    return;
  }
  
  // setButtons
  if ([performAction isEqualToString:@"setButtons"])
  {
    NSArray *buttons = actionParams[@"buttons"];
    BOOL animated = actionParams[@"animated"] ? [actionParams[@"animated"] boolValue] : YES;
    NSString *side = actionParams[@"side"] ? actionParams[@"side"] : @"left";
    
    [self setButtons:buttons viewController:self.topViewController side:side animated:animated];
    return;
  }
  
  // setTitle
  if ([performAction isEqualToString:@"setTitle"] || [performAction isEqualToString:@"setTitleImage"])
  {
    NSDictionary *navigatorStyle = actionParams[@"style"];
    [self processTitleView:self.topViewController
                     props:actionParams
                     style:navigatorStyle];
    return;
  }
  
  // toggleNavBar
  if ([performAction isEqualToString:@"setHidden"]) {
    NSNumber *animated = actionParams[@"animated"];
    BOOL animatedBool = animated ? [animated boolValue] : YES;
    
    NSNumber *setHidden = actionParams[@"hidden"];
    BOOL isHiddenBool = setHidden ? [setHidden boolValue] : NO;
    
    RCCViewController *topViewController = ((RCCViewController*)self.topViewController);
    topViewController.navigatorStyle[@"navBarHidden"] = setHidden;
    [topViewController setNavBarVisibilityChange:animatedBool];
    
  }
    
    // setStyle
    if ([performAction isEqualToString:@"setStyle"])
    {
        
        NSDictionary *navigatorStyle = actionParams;
        
        // merge the navigatorStyle of our parent
        if ([self.topViewController isKindOfClass:[RCCViewController class]])
        {
            RCCViewController *parent = (RCCViewController*)self.topViewController;
            NSMutableDictionary *mergedStyle = [NSMutableDictionary dictionaryWithDictionary:parent.navigatorStyle];
            
            // there are a few styles that we don't want to remember from our parent (they should be local)
            [mergedStyle setValuesForKeysWithDictionary:navigatorStyle];
            navigatorStyle = mergedStyle;
            
            parent.navigatorStyle = navigatorStyle;
            
            [parent setStyleOnInit];
            [parent updateStyle];
        }
    }
}

-(void)onButtonPress:(UIBarButtonItem*)barButtonItem
{
  NSString *callbackId = objc_getAssociatedObject(barButtonItem, &NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY);
  if (!callbackId) return;
  NSString *buttonId = objc_getAssociatedObject(barButtonItem, &NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY);
  [[[RCCManager sharedInstance] getBridge].eventDispatcher sendAppEventWithName:callbackId body:@
   {
     @"type": @"NavBarButtonPress",
     @"id": buttonId ? buttonId : [NSNull null]
   }];
}

-(void)setButtons:(NSArray*)buttons viewController:(UIViewController*)viewController side:(NSString*)side animated:(BOOL)animated
{
  NSMutableArray *barButtonItems = [NSMutableArray new];
  for (NSDictionary *button in buttons)
  {
    NSString *title = button[@"title"];
    UIImage *iconImage = nil;
    id icon = button[@"icon"];
    if (icon) iconImage = [RCTConvert UIImage:icon];
    
    BOOL showTitleAndIcon = button[@"showAsAction"] && [button[@"showAsAction"] isEqualToString:@"withText"];
    
    UIBarButtonItem *barButtonItem;
    if (iconImage && !showTitleAndIcon)
    {
      barButtonItem = [[UIBarButtonItem alloc] initWithImage:iconImage style:UIBarButtonItemStylePlain target:self action:@selector(onButtonPress:)];
    }
    else if (iconImage && showTitleAndIcon && title) {
      
      UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
      
      [button setImage:[iconImage imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
      [button setTitle:title forState:UIControlStateNormal];
      [button addTarget:self action:@selector(onButtonPress:) forControlEvents:UIControlEventTouchUpInside];
    
      barButtonItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    }
    else if (title)
    {
      barButtonItem = [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStylePlain target:self action:@selector(onButtonPress:)];
    }
    else continue;
    
    [RCTHelpers styleNavigationItem:barButtonItem inViewController:viewController side:side];
    if (barButtonItem.customView) {
      [barButtonItem.customView sizeToFit];
    }
    
    if (barButtonItem.customView && [barButtonItem.customView isKindOfClass:[UIButton class]]) {
      objc_setAssociatedObject(barButtonItem.customView, &NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY, button[@"onPress"], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    } else {
      objc_setAssociatedObject(barButtonItem, &NAVIGATION_ITEM_CALLBACK_ID_ASSOCIATED_KEY, button[@"onPress"], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    
    [barButtonItems addObject:barButtonItem];
    
    NSString *buttonId = button[@"id"];
    if (buttonId)
    {
      if (barButtonItem.customView && [barButtonItem.customView isKindOfClass:[UIButton class]]) {
        objc_setAssociatedObject(barButtonItem.customView, &NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY, buttonId, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
      } else {
        objc_setAssociatedObject(barButtonItem, &NAVIGATION_ITEM_BUTTON_ID_ASSOCIATED_KEY, buttonId, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
      }
    }
    
    NSNumber *disabled = button[@"disabled"];
    BOOL disabledBool = disabled ? [disabled boolValue] : NO;
    if (disabledBool) {
      [barButtonItem setEnabled:NO];
    }
    
    NSNumber *disableIconTintString = button[@"disableIconTint"];
    BOOL disableIconTint = disableIconTintString ? [disableIconTintString boolValue] : NO;
    if (disableIconTint && barButtonItem.customView && [barButtonItem.customView isKindOfClass:[UIButton class]]) {
      UIButton *button = (UIButton *)barButtonItem.customView;
      [button setImage:[[button imageForState:UIControlStateNormal] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] forState:UIControlStateNormal];
    }
    else if (disableIconTint) {
      [barButtonItem setImage:[barButtonItem.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    }
    
    NSString *testID = button[@"testID"];
    if (testID)
    {
      barButtonItem.accessibilityIdentifier = testID;
    }
  }
  
  if ([side isEqualToString:@"left"])
  {
    [viewController.navigationItem setLeftBarButtonItems:barButtonItems animated:animated];
  }
  
  if ([side isEqualToString:@"right"])
  {
    [viewController.navigationItem setRightBarButtonItems:barButtonItems animated:animated];
  }
}


-(void)processTitleView:(UIViewController*)viewController
                  props:(NSDictionary*)props
                  style:(NSDictionary*)style
{
  
  RCCTitleViewHelper *titleViewHelper = [[RCCTitleViewHelper alloc] init:viewController
                                                    navigationController:self
                                                                   title:props[@"title"]
                                                                subtitle:props[@"subtitle"]
                                                          titleImageData:props[@"titleImage"]];
  
  [titleViewHelper setup:style];
  
}

- (UIStatusBarStyle)preferredStatusBarStyle {
  if (self.presentedViewController && self.presentedViewController.isBeingDismissed) {
    return [self.topViewController preferredStatusBarStyle];
  } else {
    return [self.visibleViewController preferredStatusBarStyle];
  }
}


#pragma mark - UINavigationControllerDelegate


-(void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
  [viewController setNeedsStatusBarAppearanceUpdate];
}

- (void)viewWillAppear:(BOOL)animated
{
  [super viewWillAppear:animated];
  [self setNeedsStatusBarAppearanceUpdate];
}
@end
