#import "RCCTabBarController.h"
#import "RCCViewController.h"
#import <React/RCTConvert.h>
#import "RCCManager.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>
#import "UIViewController+Rotation.h"

@interface RCTUIManager ()

- (void)configureNextLayoutAnimation:(NSDictionary *)config
                        withCallback:(RCTResponseSenderBlock)callback
                       errorCallback:(__unused RCTResponseSenderBlock)errorCallback;

@end

@interface RotatableImageButton : UIButton

@end

@implementation RotatableImageButton

- (void)layoutSubviews {
  CGAffineTransform originalTransform = self.imageView.transform;
  self.imageView.transform = CGAffineTransformIdentity;

  [super layoutSubviews];
  self.imageView.transform = originalTransform;
}

@end

@interface RCCTabBarController() <UIViewControllerTransitioningDelegate, UIViewControllerAnimatedTransitioning>

@property (nonatomic, strong) UIViewController *centerTabController;
@property (nonatomic, strong) UIButton *centerButton;
@property (nonatomic, assign) BOOL reverseTransition;

@end

@implementation RCCTabBarController


-(UIInterfaceOrientationMask)supportedInterfaceOrientations {
  return [self supportedControllerOrientations];
}

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController {
  if (self.presentedViewController != nil) {
    [self dismissViewControllerAnimated:YES completion:nil];
  }

  id queue = [[RCCManager sharedInstance].getBridge uiManager].methodQueue;
  dispatch_async(queue, ^{
    [[[RCCManager sharedInstance].getBridge uiManager] configureNextLayoutAnimation:nil withCallback:^(NSArray* arr){} errorCallback:^(NSArray* arr){}];
  });

  if (tabBarController.selectedIndex != [tabBarController.viewControllers indexOfObject:viewController]) {
    NSDictionary *body = @{
                           @"selectedTabIndex": @([tabBarController.viewControllers indexOfObject:viewController]),
                           @"unselectedTabIndex": @(tabBarController.selectedIndex)
                           };
    [RCCTabBarController sendScreenTabChangedEvent:viewController body:body];

    [[[RCCManager sharedInstance] getBridge].eventDispatcher sendAppEventWithName:@"bottomTabSelected" body:body];
  } else {
    [RCCTabBarController sendScreenTabPressedEvent:viewController body:nil];
  }



  return YES;
}

- (UIImage *)image:(UIImage*)image withColor:(UIColor *)color1
{
  UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
  CGContextRef context = UIGraphicsGetCurrentContext();
  CGContextTranslateCTM(context, 0, image.size.height);
  CGContextScaleCTM(context, 1.0, -1.0);
  CGContextSetBlendMode(context, kCGBlendModeNormal);
  CGRect rect = CGRectMake(0, 0, image.size.width, image.size.height);
  CGContextClipToMask(context, rect, image.CGImage);
  [color1 setFill];
  CGContextFillRect(context, rect);
  UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
  UIGraphicsEndImageContext();
  return newImage;
}

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge
{
  self = [super init];
  if (!self) return nil;

  self.delegate = self;

  self.tabBar.translucent = YES; // default

  UIColor *buttonColor = nil;
  UIColor *selectedButtonColor = nil;
  NSDictionary *tabsStyle = props[@"style"];
  if (tabsStyle)
  {
    NSString *tabBarButtonColor = tabsStyle[@"tabBarButtonColor"];
    if (tabBarButtonColor)
    {
      UIColor *color = tabBarButtonColor != (id)[NSNull null] ? [RCTConvert UIColor:tabBarButtonColor] : nil;
      self.tabBar.tintColor = color;
      buttonColor = color;
      selectedButtonColor = color;
    }

    NSString *tabBarSelectedButtonColor = tabsStyle[@"tabBarSelectedButtonColor"];
    if (tabBarSelectedButtonColor)
    {
      UIColor *color = tabBarSelectedButtonColor != (id)[NSNull null] ? [RCTConvert UIColor:tabBarSelectedButtonColor] : nil;
      self.tabBar.tintColor = color;
      selectedButtonColor = color;
    }

    NSString *tabBarBackgroundColor = tabsStyle[@"tabBarBackgroundColor"];
    if (tabBarBackgroundColor)
    {
      UIColor *color = tabBarBackgroundColor != (id)[NSNull null] ? [RCTConvert UIColor:tabBarBackgroundColor] : nil;
      self.tabBar.barTintColor = color;
    }

    NSString *tabBarTranslucent = tabsStyle[@"tabBarTranslucent"];
    if (tabBarTranslucent)
    {
      self.tabBar.translucent = [tabBarTranslucent boolValue] ? YES : NO;
    }

    // Insert special center tab
    id tabBarCenterIcon = tabsStyle[@"tabBarCenterIcon"];
    if (tabBarCenterIcon)
    {
      UIImage *centerIcon = [RCTConvert UIImage:tabBarCenterIcon];
      centerIcon = [[self image:centerIcon withColor:selectedButtonColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];

      self.centerButton = [RotatableImageButton buttonWithType:UIButtonTypeCustom];
      self.centerButton.clipsToBounds = YES;
      self.centerButton.imageView.contentMode = UIViewContentModeCenter;
      UIColor *tabBarColor = tabBarBackgroundColor != (id)[NSNull null] ? [RCTConvert UIColor:tabBarBackgroundColor] : nil;
      self.centerButton.backgroundColor = tabBarColor;
      self.centerButton.frame = CGRectMake(0, 0, self.tabBar.bounds.size.width/children.count, self.tabBar.bounds.size.height);
      [self.centerButton setImage:centerIcon forState:UIControlStateNormal];

      id tabBarSelectedCenterIcon = tabsStyle[@"tabBarSelectedCenterIcon"];
      if (tabBarSelectedCenterIcon)
      {
        UIImage *selectedCenterIcon = [RCTConvert UIImage:tabBarSelectedCenterIcon];
        selectedCenterIcon = [[self image:selectedCenterIcon withColor:selectedButtonColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        [self.centerButton setImage:selectedCenterIcon forState:UIControlStateHighlighted];
      }
      else
      {
        UIImage *selectedCenterIcon = [[self image:centerIcon withColor:selectedButtonColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        [self.centerButton setImage:selectedCenterIcon forState:UIControlStateHighlighted];
      }

      CGFloat heightDifference = centerIcon.size.height - self.tabBar.frame.size.height;
      if (heightDifference < 0)
         self.centerButton.center = self.tabBar.center;
      else
      {
       CGPoint center = self.tabBar.center;
       center.y = center.y - heightDifference/2.0;
       self.centerButton.center = center;
      }

      [self.centerButton addTarget:self action:@selector(touchUpInsideCenterButton:) forControlEvents:UIControlEventTouchUpInside];

      [self.view addSubview:self.centerButton];
    }
  }

  NSMutableArray *viewControllers = [NSMutableArray array];

  // go over all the tab bar items
  for (NSDictionary *tabItemLayout in children)
  {
    // make sure the layout is valid
    if (![tabItemLayout[@"type"] isEqualToString:@"TabBarControllerIOS.Item"]) continue;
    if (!tabItemLayout[@"props"]) continue;

    // get the view controller inside
    if (!tabItemLayout[@"children"]) continue;
    if (![tabItemLayout[@"children"] isKindOfClass:[NSArray class]]) continue;
    if ([tabItemLayout[@"children"] count] < 1) continue;
    NSDictionary *childLayout = tabItemLayout[@"children"][0];
    UIViewController *viewController = [RCCViewController controllerWithLayout:childLayout globalProps:globalProps bridge:bridge];
    if (!viewController) continue;

    NSString *tabBarButtonColor = tabItemLayout[@"props"][@"tabBarButtonColor"];
    if (tabBarButtonColor)
    {
      UIColor *color = tabBarButtonColor != (id)[NSNull null] ? [RCTConvert UIColor:tabBarButtonColor] : nil;
      buttonColor = color;
      selectedButtonColor = color;
    }

    // create the tab icon and title
    NSString *title = tabItemLayout[@"props"][@"title"];
    UIImage *iconImage = nil;
    id icon = tabItemLayout[@"props"][@"icon"];
    if (icon)
    {
      iconImage = [RCTConvert UIImage:icon];
      if (buttonColor)
      {
        iconImage = [[self image:iconImage withColor:buttonColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
      }
    }
    UIImage *iconImageSelected = nil;
    id selectedIcon = tabItemLayout[@"props"][@"selectedIcon"];
    if (selectedIcon) {
      iconImageSelected = [RCTConvert UIImage:selectedIcon];
    } else {
      iconImageSelected = [RCTConvert UIImage:icon];
    }

    viewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:title image:iconImage tag:0];
    viewController.tabBarItem.accessibilityIdentifier = tabItemLayout[@"props"][@"testID"];
    viewController.tabBarItem.selectedImage = iconImageSelected;

    id imageInsets = tabItemLayout[@"props"][@"iconInsets"];
    if (imageInsets && imageInsets != (id)[NSNull null])
    {
      id topInset = imageInsets[@"top"];
      id leftInset = imageInsets[@"left"];
      id bottomInset = imageInsets[@"bottom"];
      id rightInset = imageInsets[@"right"];

      CGFloat top = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:topInset] : 0;
      CGFloat left = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:leftInset] : 0;
      CGFloat bottom = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:bottomInset] : 0;
      CGFloat right = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:rightInset] : 0;

      viewController.tabBarItem.imageInsets = UIEdgeInsetsMake(top, left, bottom, right);
    }

    NSMutableDictionary *unselectedAttributes = [RCTHelpers textAttributesFromDictionary:tabsStyle withPrefix:@"tabBarText" baseFont:[UIFont systemFontOfSize:10]];
    if (!unselectedAttributes[NSForegroundColorAttributeName] && buttonColor) {
      unselectedAttributes[NSForegroundColorAttributeName] = buttonColor;
    }

    [viewController.tabBarItem setTitleTextAttributes:unselectedAttributes forState:UIControlStateNormal]
    ;

    NSMutableDictionary *selectedAttributes = [RCTHelpers textAttributesFromDictionary:tabsStyle withPrefix:@"tabBarSelectedText" baseFont:[UIFont systemFontOfSize:10]];
    if (!selectedAttributes[NSForegroundColorAttributeName] && selectedButtonColor) {
      selectedAttributes[NSForegroundColorAttributeName] = selectedButtonColor;
    }

    [viewController.tabBarItem setTitleTextAttributes:selectedAttributes forState:UIControlStateSelected];
    // create badge
    NSObject *badge = tabItemLayout[@"props"][@"badge"];
    if (badge == nil || [badge isEqual:[NSNull null]])
    {
      viewController.tabBarItem.badgeValue = nil;
    }
    else
    {
      viewController.tabBarItem.badgeValue = [NSString stringWithFormat:@"%@", badge];
    }

    [viewControllers addObject:viewController];
  }

  if (self.centerTabController == nil) {
    self.centerTabController = viewControllers[2];
    UIViewController *dummyViewController = [[UIViewController alloc] init];
    [viewControllers replaceObjectAtIndex:2 withObject:dummyViewController];
  }

  // replace the tabs
  self.viewControllers = viewControllers;

  [self setRotation:props];

  return self;
}

-(void)touchUpInsideCenterButton:(UIButton *)sender
{
  self.centerTabController.modalPresentationStyle = UIModalPresentationCustom;
  self.centerTabController.transitioningDelegate = self;

  if (!self.reverseTransition) {
    [self presentViewController:self.centerTabController animated:YES completion:nil];
  } else {
    [self dismissViewControllerAnimated:YES completion:nil];
  }
}

- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge completion:(void (^)(void))completion
{
  if ([performAction isEqualToString:@"setBadge"])
  {
    UIViewController *viewController = nil;
    NSNumber *tabIndex = actionParams[@"tabIndex"];
    if (tabIndex)
    {
      int i = (int)[tabIndex integerValue];

      if ([self.viewControllers count] > i)
      {
        viewController = [self.viewControllers objectAtIndex:i];
      }
    }
    NSString *contentId = actionParams[@"contentId"];
    NSString *contentType = actionParams[@"contentType"];
    if (contentId && contentType)
    {
      viewController = [[RCCManager sharedInstance] getControllerWithId:contentId componentType:contentType];
    }

    if (viewController)
    {
      NSObject *badge = actionParams[@"badge"];

      if (badge == nil || [badge isEqual:[NSNull null]])
      {
        viewController.tabBarItem.badgeValue = nil;
      }
      else
      {
        viewController.tabBarItem.badgeValue = [NSString stringWithFormat:@"%@", badge];
      }
    }
  }

  if ([performAction isEqualToString:@"switchTo"])
  {
    UIViewController *viewController = nil;
    NSNumber *tabIndex = actionParams[@"tabIndex"];
    if (tabIndex)
    {
      int i = (int)[tabIndex integerValue];

      if ([self.viewControllers count] > i)
      {
        viewController = [self.viewControllers objectAtIndex:i];
      }
    }
    NSString *contentId = actionParams[@"contentId"];
    NSString *contentType = actionParams[@"contentType"];
    if (contentId && contentType)
    {
      viewController = [[RCCManager sharedInstance] getControllerWithId:contentId componentType:contentType];
    }

    if (viewController)
    {
      [self setSelectedViewController:viewController];
    }
  }

  if ([performAction isEqualToString:@"setTabButton"])
  {
    UIViewController *viewController = nil;
    NSNumber *tabIndex = actionParams[@"tabIndex"];
    if (tabIndex)
    {
      int i = (int)[tabIndex integerValue];

      if ([self.viewControllers count] > i)
      {
        viewController = [self.viewControllers objectAtIndex:i];
      }
    }
    NSString *contentId = actionParams[@"contentId"];
    NSString *contentType = actionParams[@"contentType"];
    if (contentId && contentType)
    {
      viewController = [[RCCManager sharedInstance] getControllerWithId:contentId componentType:contentType];
    }

    if (viewController)
    {
      UIImage *iconImage = nil;
      id icon = actionParams[@"icon"];
      if (icon && icon != (id)[NSNull null])
      {
        iconImage = [RCTConvert UIImage:icon];
        iconImage = [[self image:iconImage withColor:self.tabBar.tintColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        viewController.tabBarItem.image = iconImage;

      }
      UIImage *iconImageSelected = nil;
      id selectedIcon = actionParams[@"selectedIcon"];
      if (selectedIcon && selectedIcon != (id)[NSNull null])
      {
        iconImageSelected = [RCTConvert UIImage:selectedIcon];
        viewController.tabBarItem.selectedImage = iconImageSelected;
      }
    }
  }

  if ([performAction isEqualToString:@"setTabBarHidden"])
  {
    BOOL hidden = [actionParams[@"hidden"] boolValue];
    [UIView animateWithDuration: ([actionParams[@"animated"] boolValue] ? 0.45 : 0)
                          delay: 0
         usingSpringWithDamping: 0.75
          initialSpringVelocity: 0
                        options: (hidden ? UIViewAnimationOptionCurveEaseIn : UIViewAnimationOptionCurveEaseOut)
                     animations:^()
     {
       self.tabBar.transform = hidden ? CGAffineTransformMakeTranslation(0, self.tabBar.frame.size.height) : CGAffineTransformIdentity;
     }
                     completion:^(BOOL finished)
     {
       if (completion != nil)
       {
         completion();
       }
     }];
    return;
  }
  else if (completion != nil)
  {
    completion();
  }
}

+(void)sendScreenTabChangedEvent:(UIViewController*)viewController body:(NSDictionary*)body{
  [RCCTabBarController sendTabEvent:@"bottomTabSelected" controller:viewController body:body];
}

+(void)sendScreenTabPressedEvent:(UIViewController*)viewController body:(NSDictionary*)body{
  [RCCTabBarController sendTabEvent:@"bottomTabReselected" controller:viewController body:body];
}

+(void)sendTabEvent:(NSString *)event controller:(UIViewController*)viewController body:(NSDictionary*)body{
  if ([viewController.view isKindOfClass:[RCTRootView class]]){
    RCTRootView *rootView = (RCTRootView *)viewController.view;

    if (rootView.appProperties && rootView.appProperties[@"navigatorEventID"]) {
      NSString *navigatorID = rootView.appProperties[@"navigatorID"];
      NSString *screenInstanceID = rootView.appProperties[@"screenInstanceID"];


      NSMutableDictionary *screenDict = [NSMutableDictionary dictionaryWithDictionary:@
                                         {
                                           @"id": event,
                                           @"navigatorID": navigatorID,
                                           @"screenInstanceID": screenInstanceID
                                         }];


      if (body) {
        [screenDict addEntriesFromDictionary:body];
      }

      [[[RCCManager sharedInstance] getBridge].eventDispatcher sendAppEventWithName:rootView.appProperties[@"navigatorEventID"] body:screenDict];
    }
  }

  if ([viewController isKindOfClass:[UINavigationController class]]) {
    UINavigationController *navigationController = (UINavigationController*)viewController;
    UIViewController *topViewController = [navigationController topViewController];
    [RCCTabBarController sendTabEvent:event controller:topViewController body:body];
  }
}

// MARK: UIViewControllerTransitioningDelegate

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
  if (presented == self.centerTabController) {
    return self;
  }
  return nil;
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
  if (dismissed == self.centerTabController) {
    return self;
  }
  return nil;
}

// MARK: UIViewControllerAnimatedTransitioning

- (NSTimeInterval)transitionDuration:(id<UIViewControllerContextTransitioning>)transitionContext {
  return 0.2;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
  UIViewController* toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
  UIViewController* fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];

  [transitionContext containerView].clipsToBounds = YES;

  if (!self.reverseTransition)
  {
    self.centerButton.imageView.transform = CGAffineTransformRotate(self.centerButton.imageView.transform, -M_PI * 0.25);

    [[transitionContext containerView] addSubview:toViewController.view];

    toViewController.view.alpha = 0.25;

    CGRect containerRect = [transitionContext containerView].frame;
    containerRect.size.height -= self.tabBar.bounds.size.height+1;
    [transitionContext containerView].frame = containerRect;

    CGRect toRect = self.view.frame;
    toRect.origin.y += toRect.size.height+1;
    toViewController.view.frame = toRect;

    [UIView animateWithDuration:[self transitionDuration:transitionContext] animations:^{
      toViewController.view.frame = [transitionContext containerView].bounds;
      toViewController.view.alpha = 1;
    } completion:^(BOOL finished) {
      [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
      self.reverseTransition = !self.reverseTransition;
    }];
  } else {
    [UIView animateWithDuration:[self transitionDuration:transitionContext] animations:^{
      self.centerButton.imageView.transform = CGAffineTransformIdentity;
      fromViewController.view.alpha = 0;
    } completion:^(BOOL finished) {
      [fromViewController.view removeFromSuperview];
      [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
      self.reverseTransition = !self.reverseTransition;
    }];
  }
}

@end
