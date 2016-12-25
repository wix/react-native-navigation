#import "RCCTabBarController.h"
#import "RCCViewController.h"
#import "RCTConvert.h"
#import "RCCManager.h"
#import "RCCImageHelper.h"
#import "RCTImageLoader.h"

@implementation RCCTabBarController

CGFloat const DEFAULT_ICON_WIDTH = 50.0f;
CGFloat const DEFAULT_ICON_HEIGHT = 50.0f;

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
    if (selectedIcon) iconImageSelected = [RCTConvert UIImage:selectedIcon];

    viewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:title image:iconImage tag:0];
    viewController.tabBarItem.accessibilityIdentifier = tabItemLayout[@"props"][@"testID"];
    viewController.tabBarItem.selectedImage = iconImageSelected;
    
    if (buttonColor)
    {
      [viewController.tabBarItem setTitleTextAttributes:
       @{NSForegroundColorAttributeName : buttonColor} forState:UIControlStateNormal];
    }
    
    if (selectedButtonColor)
    {
      [viewController.tabBarItem setTitleTextAttributes:
       @{NSForegroundColorAttributeName : selectedButtonColor} forState:UIControlStateSelected];
    }
    
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
    
    // download and set remote icons
    id remoteIcon = tabItemLayout[@"props"][@"remoteIcon"];
    id remoteSelectedIcon = tabItemLayout[@"props"][@"remoteSelectedIcon"];
    Boolean hasSelectedIcon = remoteSelectedIcon && ![remoteSelectedIcon isKindOfClass:[NSNull class]];
    
    if (remoteIcon) {
      [self setTabBarItemImageFromRemoteServer:remoteIcon isSelectedImage:false hasSelectedIcon:hasSelectedIcon toViewController:viewController orTabIndex:0 bridge:bridge];
    }
    
    if (remoteSelectedIcon) {
      [self setTabBarItemImageFromRemoteServer:remoteSelectedIcon isSelectedImage:true hasSelectedIcon:hasSelectedIcon toViewController:viewController orTabIndex:0 bridge:bridge];
    }
    
    // add the viewController
    [viewControllers addObject:viewController];
  }

  // replace the tabs
  self.viewControllers = viewControllers;
  
  // set selected tab index
  NSUInteger *selectedIndex = [props[@"selectedIndex"] unsignedIntegerValue];
  if (selectedIndex && selectedIndex < viewControllers.count) {
    [self setSelectedIndex:selectedIndex];
  }

  return self;
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
  
    // set icons
    if ([performAction isEqualToString:@"setIcons"])
    {
      NSNumber *tabIndex = actionParams[@"tabIndex"];
      
      if (!tabIndex) {
        return;
      }
      
      int iTabIndex = [tabIndex integerValue];
      
      // TODO: set local icons
      
      // download and set remote icons
      id remoteIcon = actionParams[@"remoteIcon"];
      id remoteSelectedIcon = actionParams[@"remoteSelectedIcon"];
      Boolean hasSelectedIcon = remoteSelectedIcon && ![remoteSelectedIcon isKindOfClass:[NSNull class]];
      
      if (remoteIcon) {
        [self setTabBarItemImageFromRemoteServer:remoteIcon isSelectedImage:false hasSelectedIcon:hasSelectedIcon toViewController:nil orTabIndex:iTabIndex bridge:bridge];
      }
      
      if (remoteSelectedIcon) {
        [self setTabBarItemImageFromRemoteServer:remoteSelectedIcon isSelectedImage:true hasSelectedIcon:hasSelectedIcon toViewController:nil orTabIndex:iTabIndex bridge:bridge];
      }
    }
}

- (void)setTabBarItemImageFromRemoteServer:(NSDictionary*)remoteIcon isSelectedImage:(Boolean)isSelected hasSelectedIcon:(Boolean)hasSelectedIcon toViewController:(UIViewController *)currentViewController orTabIndex:(NSInteger)tabIndex bridge:(RCTBridge *)bridge
{
  UIViewController *viewController = nil;
  if (currentViewController) {
    viewController = currentViewController;
  } else if ([self.viewControllers count] > tabIndex) {
    viewController = [self.viewControllers objectAtIndex:tabIndex];
  }
  
  if (viewController == nil) {
    return;
  }
  
  if (remoteIcon && [remoteIcon isKindOfClass:[NSDictionary class]] && remoteIcon[@"uri"]) {
    
    NSURL *imageUrl = [NSURL URLWithString:remoteIcon[@"uri"]];
    NSURLRequest *imageUrlRequest = [[NSURLRequest alloc] initWithURL:imageUrl];
    CGFloat imageWidth = remoteIcon[@"width"] ? (CGFloat)[remoteIcon[@"width"] floatValue] : DEFAULT_ICON_WIDTH;
    CGFloat imageHeight = remoteIcon[@"height"] ? (CGFloat)[remoteIcon[@"height"] floatValue] : DEFAULT_ICON_HEIGHT;
    CGFloat imageScale = remoteIcon[@"scale"] ? (CGFloat)[remoteIcon[@"scale"] floatValue] : [UIScreen mainScreen].scale;
    CGFloat imageRadius = remoteIcon[@"radius"] ? (CGFloat)[remoteIcon[@"radius"] floatValue] : 0;
    CGSize imageSize = CGSizeMake(imageWidth, imageHeight);
    
    void (^iconLoaderCompletionBlock) (NSError*, UIImage*) = ^(NSError *error, UIImage *loadedImage) {
      
      if (error) {
        dispatch_async(dispatch_get_main_queue(), ^{
          NSLog(@"remoteIcon %@ download failed: %@", remoteIcon[@"uri"], [error localizedDescription]);
          [viewController.view setNeedsDisplay];
        });
        return;
      }
      
      if (imageRadius) {
        loadedImage = [RCCImageHelper roundedImageWithRadius:imageRadius usingImage:loadedImage];
      }
      
      loadedImage = [UIImage imageWithCGImage:loadedImage.CGImage scale:imageScale orientation:loadedImage.imageOrientation];
      loadedImage = [loadedImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
      
      dispatch_async(dispatch_get_main_queue(), ^{
        if (isSelected) {
          [viewController.tabBarItem setSelectedImage:loadedImage];
        } else {
          [viewController.tabBarItem setImage:loadedImage];
          if (!hasSelectedIcon) {
            [viewController.tabBarItem setSelectedImage:loadedImage];
          }
        }
        [viewController.view setNeedsDisplay];
      });
      
    };
    
    [bridge.imageLoader loadImageWithURLRequest:imageUrlRequest size:imageSize scale:1.0f clipped:false resizeMode:RCTResizeModeCenter progressBlock:nil partialLoadBlock:nil completionBlock:iconLoaderCompletionBlock];
    
  }
}

@end
