//#import "RCCDrawerController.h"
//#import "RCCViewController.h"
//#import "MMExampleDrawerVisualStateManager.h"
//#import "RCCDrawerHelper.h"
//#import "RCTConvert.h"
//#import "RCCManagerModule.h"
//
//#define RCCDRAWERCONTROLLER_ANIMATION_DURATION 0.33f
//
//
//@implementation RCCDrawerController
//
//@synthesize drawerStyle = _drawerStyle;
//
//
//
//- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge
//{
//    
//    self.drawerStyle = props[@"style"];
//    
//    // center
//    if ([children count] < 1) return nil;
//    UIViewController *centerViewController = [RCCViewController controllerWithLayout:children[0] globalProps:globalProps bridge:bridge];
//    
//    // left
//    UIViewController *leftViewController = nil;
//    NSString *componentLeft = props[@"componentLeft"];
//    NSDictionary *passPropsLeft = props[@"passPropsLeft"];
//    if (componentLeft) leftViewController = [[RCCViewController alloc] initWithComponent:componentLeft passProps:passPropsLeft navigatorStyle:nil globalProps:globalProps bridge:bridge];
//    
//    // right
//    UIViewController *rightViewController = nil;
//    NSString *componentRight = props[@"componentRight"];
//    NSDictionary *passPropsRight = props[@"passPropsRight"];
//    if (componentRight) rightViewController = [[RCCViewController alloc] initWithComponent:componentRight passProps:passPropsRight navigatorStyle:nil globalProps:globalProps bridge:bridge];
//    
//    self = [super initWithCenterViewController:centerViewController
//                      leftDrawerViewController:leftViewController
//                     rightDrawerViewController:rightViewController];
//    
//    [self setAnimationTypeWithName:props[@"animationType"]];
//    
//    // default is all MMOpenDrawerGestureModeAll and MMCloseDrawerGestureModeAll
//    self.openDrawerGestureModeMask = MMOpenDrawerGestureModeAll;
//    self.closeDrawerGestureModeMask = MMCloseDrawerGestureModeAll;
//    
//    NSNumber *disableOpenGesture = props[@"disableOpenGesture"];
//    if ([disableOpenGesture boolValue]) {
//        self.openDrawerGestureModeMask = MMOpenDrawerGestureModeNone;
//    }
//    
//    [self setStyle];
//    
//    [self setDrawerVisualStateBlock:^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
//        MMDrawerControllerDrawerVisualStateBlock block;
//        block = [[MMExampleDrawerVisualStateManager sharedManager] drawerVisualStateBlockForDrawerSide:drawerSide];
//        if (block) {
//            block(drawerController, drawerSide, percentVisible);
//        }
//    }];
//    
//    [self setGestureStartBlock:^(MMDrawerController *drawerController, UIGestureRecognizer *gesture) {
//        [RCCManagerModule cancelAllRCCViewControllerReactTouches];
//     }];
//                                               
//    self.view.backgroundColor = [UIColor clearColor];
//    
//    if (!self) return nil;
//    return self;
//}
//
//
//-(void)setStyle {
//    
//    if (self.drawerStyle[@"drawerShadow"]) {
//        self.showsShadow = ([self.drawerStyle[@"drawerShadow"] boolValue]) ? YES : NO;
//    }
//    
//    NSNumber *leftDrawerWidth = self.drawerStyle[@"leftDrawerWidth"];
//    if (leftDrawerWidth) {
//        self.maximumLeftDrawerWidth = self.view.bounds.size.width * MIN(1, (leftDrawerWidth.floatValue/100.0));
//    }
//    
//    NSNumber *rightDrawerWidth = self.drawerStyle[@"rightDrawerWidth"];
//    if (rightDrawerWidth) {
//        self.maximumRightDrawerWidth = self.view.bounds.size.width * MIN(1, (rightDrawerWidth.floatValue/100.0));
//    }
//    
//    NSString *contentOverlayColor = self.drawerStyle[@"contentOverlayColor"];
//    if (contentOverlayColor)
//    {
//        UIColor *color = contentOverlayColor != (id)[NSNull null] ? [RCTConvert UIColor:contentOverlayColor] : nil;
//        [self setCenterOverlayColor:color];
//    }
//}
//
//
//- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge
//{
//    MMDrawerSide side = MMDrawerSideLeft;
//    if ([actionParams[@"side"] isEqualToString:@"right"]) side = MMDrawerSideRight;
//    BOOL animated = actionParams[@"animated"] ? [actionParams[@"animated"] boolValue] : YES;
//    
//    // open
//    if ([performAction isEqualToString:@"open"])
//    {
//        [self openDrawerSide:side animated:animated completion:nil];
//        return;
//    }
//    
//    // close
//    if ([performAction isEqualToString:@"close"])
//    {
//        if (self.openSide == side) {
//            [self closeDrawerAnimated:animated completion:nil];
//        }
//        
//        return;
//    }
//    
//    // toggle
//    if ([performAction isEqualToString:@"toggle"])
//    {
//        [super toggleDrawerSide:side animated:animated completion:nil];
//        return;
//    }
//    
//    // setStyle
//    if ([performAction isEqualToString:@"setStyle"])
//    {
//        if (actionParams[@"animationType"]) {
//            NSString *animationTypeString = actionParams[@"animationType"];
//            [self setAnimationTypeWithName:animationTypeString];
//        }
//        return;
//    }
//    
//}
//
//-(void)setAnimationTypeWithName:(NSString*)animationTypeName {
//    MMDrawerAnimationType animationType = MMDrawerAnimationTypeNone;
//    
//    if ([animationTypeName isEqualToString:@"door"]) animationType = MMDrawerAnimationTypeSwingingDoor;
//    else if ([animationTypeName isEqualToString:@"parallax"]) animationType = MMDrawerAnimationTypeParallax;
//    else if ([animationTypeName isEqualToString:@"slide"]) animationType = MMDrawerAnimationTypeSlide;
//    else if ([animationTypeName isEqualToString:@"slide-and-scale"]) animationType = MMDrawerAnimationTypeSlideAndScale;
//    
//    [MMExampleDrawerVisualStateManager sharedManager].leftDrawerAnimationType = animationType;
//    [MMExampleDrawerVisualStateManager sharedManager].rightDrawerAnimationType = animationType;
//}
//
//
//@end
