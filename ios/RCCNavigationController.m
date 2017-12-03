#import "RCCNavigationController.h"
#import "RCCViewController.h"
#import "RCCManager.h"
#import <React/RCTEventDispatcher.h>
#import <React/RCTUIManager.h>
#if __has_include(<React/RCTUIManagerUtils.h>)
#import <React/RCTUIManagerUtils.h>
#endif
#import <React/RCTConvert.h>
#import <React/RCTRootView.h>
#import <objc/runtime.h>
#import "RCCTitleViewHelper.h"
#import "RCCCustomBarButtonItem.h"
#import "UIViewController+Rotation.h"
#import "RCTHelpers.h"
#import "RCTConvert+UIBarButtonSystemItem.h"

@implementation RCCNavigationController
{
  BOOL _transitioning;
  NSMutableArray *_queuedViewControllers;
}

NSString const *CALLBACK_ASSOCIATED_KEY = @"RCCNavigationController.CALLBACK_ASSOCIATED_KEY";
NSString const *CALLBACK_ASSOCIATED_ID = @"RCCNavigationController.CALLBACK_ASSOCIATED_ID";


-(UIInterfaceOrientationMask)supportedInterfaceOrientations {
  return [self supportedControllerOrientations];
}

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge
{
  _queuedViewControllers = [NSMutableArray new];
  
  NSString *component = props[@"component"];
  if (!component) return nil;
  
  NSDictionary *passProps = props[@"passProps"];
  NSDictionary *navigatorStyle = props[@"style"];
  
  RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:globalProps bridge:bridge];
  if (!viewController) return nil;
  viewController.controllerId = props[@"id"];
  
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
    
    NSMutableDictionary *passProps = [actionParams[@"passProps"] mutableCopy];
    passProps[GLOBAL_SCREEN_ACTION_COMMAND_TYPE] = COMMAND_TYPE_PUSH;
    passProps[GLOBAL_SCREEN_ACTION_TIMESTAMP] = actionParams[GLOBAL_SCREEN_ACTION_TIMESTAMP];
    NSDictionary *navigatorStyle = actionParams[@"style"];
    
    NSNumber *keepStyleAcrossPush = [[RCCManager sharedInstance] getAppStyle][@"keepStyleAcrossPush"];
    BOOL keepStyleAcrossPushBool = keepStyleAcrossPush ? [keepStyleAcrossPush boolValue] : YES;
    
    if (keepStyleAcrossPushBool) {
      
      if ([self.topViewController isKindOfClass:[RCCViewController class]])
      {
        RCCViewController *parent = (RCCViewController*)self.topViewController;
        NSMutableDictionary *mergedStyle = [NSMutableDictionary dictionaryWithDictionary:parent.navigatorStyle];
        
        // there are a few styles that we don't want to remember from our parent (they should be local)
        [mergedStyle removeObjectForKey:@"navBarHidden"];
        [mergedStyle removeObjectForKey:@"statusBarHidden"];
        [mergedStyle removeObjectForKey:@"navBarHideOnScroll"];
        [mergedStyle removeObjectForKey:@"drawUnderNavBar"];
        [mergedStyle removeObjectForKey:@"drawUnderTabBar"];
        [mergedStyle removeObjectForKey:@"statusBarBlur"];
        [mergedStyle removeObjectForKey:@"navBarBlur"];
        [mergedStyle removeObjectForKey:@"navBarTranslucent"];
        [mergedStyle removeObjectForKey:@"statusBarHideWithNavBar"];
        [mergedStyle removeObjectForKey:@"autoAdjustScrollViewInsets"];
        [mergedStyle removeObjectForKey:@"statusBarTextColorSchemeSingleScreen"];
        [mergedStyle removeObjectForKey:@"disabledBackGesture"];
        [mergedStyle removeObjectForKey:@"disabledSimultaneousGesture"];
        [mergedStyle removeObjectForKey:@"navBarCustomView"];
        [mergedStyle removeObjectForKey:@"navBarComponentAlignment"];
        
        [mergedStyle addEntriesFromDictionary:navigatorStyle];
        navigatorStyle = mergedStyle;
      }
    }
    
    RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:nil bridge:bridge];
    viewController.controllerId = passProps[@"screenInstanceID"];
    
    [self processTitleView:viewController
                     props:actionParams
                     style:navigatorStyle];
    
    NSString *backButtonTitle = actionParams[@"backButtonTitle"];
    if (!backButtonTitle) {
      NSNumber *hideBackButtonTitle = [[RCCManager sharedInstance] getAppStyle][@"hideBackButtonTitle"];
      BOOL hideBackButtonTitleBool = hideBackButtonTitle ? [hideBackButtonTitle boolValue] : NO;
      backButtonTitle = hideBackButtonTitleBool ? @"" : backButtonTitle;
    }
    
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

    NSArray *previewActions = actionParams[@"previewActions"];
    NSString *previewViewID = actionParams[@"previewViewID"];
    if (previewViewID) {
      if ([self.topViewController isKindOfClass:[RCCViewController class]])
      {
        RCCViewController *topViewController = ((RCCViewController*)self.topViewController);
        viewController.previewActions = previewActions;
        viewController.previewCommit = actionParams[@"previewCommit"] ? [actionParams[@"previewCommit"] boolValue] : YES;
        NSNumber *previewHeight = actionParams[@"previewHeight"];
        if (previewHeight) {
          viewController.preferredContentSize = CGSizeMake(viewController.view.frame.size.width, [previewHeight floatValue]);
        }
        if (topViewController.traitCollection.forceTouchCapability == UIForceTouchCapabilityAvailable)
        {
          dispatch_async(RCTGetUIManagerQueue(), ^{
            [bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
              UIView *view = viewRegistry[previewViewID];
              topViewController.previewView = view;
              [topViewController registerForPreviewingWithDelegate:(id)topViewController sourceView:view];
            }];
          });
          topViewController.previewController = viewController;
        }
        return;
      }
    }
    
    NSString *animationType = actionParams[@"animationType"];
    if ([animationType isEqualToString:@"fade"])
    {
      CATransition *transition = [CATransition animation];
      transition.duration = 0.25;
      transition.type = kCATransitionFade;
      
      [self.view.layer addAnimation:transition forKey:kCATransition];
      [self pushViewController:viewController animated:NO];
    }
    else
    {
      [self pushViewController:viewController animated:animated];
    }
    return;
  }
  
  // pop
  if ([performAction isEqualToString:@"pop"])
  {
    NSString *animationType = actionParams[@"animationType"];
    if ([animationType isEqualToString:@"fade"])
    {
      CATransition *transition = [CATransition animation];
      transition.duration = 0.25;
      transition.type = kCATransitionFade;
      
      [self.view.layer addAnimation:transition forKey:kCATransition];
      [self popViewControllerAnimated:NO];
    }
    else
    {
      [self popViewControllerAnimated:animated];
    }
    return;
  }
  
  // popToRoot
  if ([performAction isEqualToString:@"popToRoot"])
  {
    NSString *animationType = actionParams[@"animationType"];
    if ([animationType isEqualToString:@"fade"])
    {
      CATransition *transition = [CATransition animation];
      transition.duration = 0.25;
      transition.type = kCATransitionFade;
      
      [self.view.layer addAnimation:transition forKey:kCATransition];
      [self popToRootViewControllerAnimated:NO];
    }
    else
    {
      [self popToRootViewControllerAnimated:animated];
    }
    return;
  }
  
  // resetTo
  if ([performAction isEqualToString:@"resetTo"])
  {
    NSString *component = actionParams[@"component"];
    if (!component) return;
    
    NSMutableDictionary *passProps = [actionParams[@"passProps"] mutableCopy];
    passProps[@"commantType"] = @"resetTo";
    NSDictionary *navigatorStyle = actionParams[@"style"];
    
    RCCViewController *viewController = [[RCCViewController alloc] initWithComponent:component passProps:passProps navigatorStyle:navigatorStyle globalProps:nil bridge:bridge];
    viewController.controllerId = passProps[@"screenInstanceID"];
    
    viewController.navigationItem.hidesBackButton = YES;
    
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
    
    NSString *animationType = actionParams[@"animationType"];
    if ([animationType isEqualToString:@"fade"])
    {
      CATransition *transition = [CATransition animation];
      transition.duration = 0.25;
      transition.type = kCATransitionFade;
      
      [self.view.layer addAnimation:transition forKey:kCATransition];
      [self setViewControllers:@[viewController] animated:NO];
    }
    else
    {
      [self setViewControllers:@[viewController] animated:animated];
    }
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
  NSString *callbackId = objc_getAssociatedObject(barButtonItem, &CALLBACK_ASSOCIATED_KEY);
  if (!callbackId) return;
  NSString *buttonId = objc_getAssociatedObject(barButtonItem, &CALLBACK_ASSOCIATED_ID);
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
    NSString *__nullable component = button[@"component"];
    NSString *__nullable systemItemName = button[@"systemItem"];
    UIBarButtonSystemItem systemItem = [RCTConvert UIBarButtonSystemItem:systemItemName];

    UIBarButtonItem *barButtonItem;
    if (iconImage)
    {
      barButtonItem = [[UIBarButtonItem alloc] initWithImage:iconImage style:UIBarButtonItemStylePlain target:self action:@selector(onButtonPress:)];
    }
    else if (title)
    {
      barButtonItem = [[UIBarButtonItem alloc] initWithTitle:title style:UIBarButtonItemStylePlain target:self action:@selector(onButtonPress:)];

      NSMutableDictionary *buttonTextAttributes = [RCTHelpers textAttributesFromDictionary:button withPrefix:@"button"];
      if (buttonTextAttributes.allKeys.count > 0) {
        [barButtonItem setTitleTextAttributes:buttonTextAttributes forState:UIControlStateNormal];
      }
    }
    else if (component) {
      RCTBridge *bridge = [[RCCManager sharedInstance] getBridge];
      barButtonItem = [[RCCCustomBarButtonItem alloc] initWithComponentName:component passProps:button[@"passProps"] bridge:bridge];
    }
    else if (systemItemName) {
      barButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:systemItem target:self action:@selector(onButtonPress:)];
    }
    else continue;
    objc_setAssociatedObject(barButtonItem, &CALLBACK_ASSOCIATED_KEY, button[@"onPress"], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    [barButtonItems addObject:barButtonItem];
    
    NSString *buttonId = button[@"id"];
    if (buttonId)
    {
      objc_setAssociatedObject(barButtonItem, &CALLBACK_ASSOCIATED_ID, buttonId, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    
    NSNumber *disabled = button[@"disabled"];
    BOOL disabledBool = disabled ? [disabled boolValue] : NO;
    if (disabledBool) {
      [barButtonItem setEnabled:NO];
    }
    
    NSNumber *disableIconTintString = button[@"disableIconTint"];
    BOOL disableIconTint = disableIconTintString ? [disableIconTintString boolValue] : NO;
    if (disableIconTint) {
      [barButtonItem setImage:[barButtonItem.image imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
    }
    
    if ([viewController isKindOfClass:[RCCViewController class]]) {
      RCCViewController *rccViewController = ((RCCViewController*)viewController);
      NSDictionary *navigatorStyle = rccViewController.navigatorStyle;
      id disabledButtonColor = navigatorStyle[@"disabledButtonColor"];
      if (disabledButtonColor) {
        UIColor *color = [RCTConvert UIColor:disabledButtonColor];
        [barButtonItem setTitleTextAttributes:@{NSForegroundColorAttributeName : color} forState:UIControlStateDisabled];
      }
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
  BOOL isSetSubtitleBool = props[@"isSetSubtitle"] ? [props[@"isSetSubtitle"] boolValue] : NO;
  RCCTitleViewHelper *titleViewHelper = [[RCCTitleViewHelper alloc] init:viewController
                                                    navigationController:self
                                                                   title:props[@"title"]
                                                                subtitle:props[@"subtitle"]
                                                          titleImageData:props[@"titleImage"]
                                                           isSetSubtitle:isSetSubtitleBool];
  
  [titleViewHelper setup:style];
  
}

- (UIStatusBarStyle)preferredStatusBarStyle {
  return [self.topViewController preferredStatusBarStyle];
}

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
  if(_transitioning)
  {
    NSDictionary *pushDetails =@{ @"viewController": viewController, @"animated": @(animated) };
    [_queuedViewControllers addObject:pushDetails];
    
    return;
  }
  
  _transitioning = YES;
  
  [super pushViewController:viewController animated:animated];
}


#pragma mark - UINavigationControllerDelegate


-(void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
  [viewController setNeedsStatusBarAppearanceUpdate];
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
  dispatch_async(dispatch_get_main_queue(), ^{
    _transitioning = NO;
    if ([_queuedViewControllers count] > 0) {
      NSDictionary *toPushDetails = [_queuedViewControllers firstObject];
      [_queuedViewControllers removeObjectAtIndex:0];
      [self pushViewController:toPushDetails[@"viewController"] animated:[toPushDetails[@"animated"] boolValue]];
    }
  });
}


@end
