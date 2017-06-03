////
////  DarwerTheSideBarManagerViewController.m
////  ReactNativeControllers
////
////  Created by Ran Greenberg on 22/03/2016.
////  Copyright © 2016 artal. All rights reserved.
////
//
//#import "RCCTheSideBarManagerViewController.h"
//#import "RCCViewController.h"
//#import "RCCDrawerHelper.h"
//#import "RCTConvert.h"
//
//
//@interface RCCTheSideBarManagerViewController () <TheSidebarControllerDelegate>
//
//@property (nonatomic) BOOL isOpen;
//
//@property (nonatomic) SidebarTransitionStyle animationStyle;
//
//@property (nonatomic, strong) RCCViewController *leftViewController;
//@property (nonatomic, strong) RCCViewController *rightViewController;
//@property (nonatomic, strong) RCCViewController *centerViewController;
//
//
//@property (nonatomic, strong) UIColor *originalWindowBackgroundColor;
//
//@end
//
//@implementation RCCTheSideBarManagerViewController
//
//@synthesize overlayButton = _overlayButton, drawerStyle = _drawerStyle;
//
//
//-(UIButton*)overlayButton {
//    if (!_overlayButton) {
//        _overlayButton = [RCCDrawerHelper createOverlayButton:self];
//    }
//    return _overlayButton;
//}
//
//- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge {
//    
//    if ([children count] < 1) return nil;
//    
//    UIViewController *centerVC = [RCCViewController controllerWithLayout:children[0] globalProps:props bridge:bridge];
//    UIViewController *leftVC = nil;
//    UIViewController *rightVC = nil;
//    
//    // left
//    NSString *componentLeft = props[@"componentLeft"];
//    if (componentLeft)  {
//        leftVC = [[RCCViewController alloc] initWithComponent:componentLeft passProps:props[@"passPropsLeft"] navigatorStyle:nil globalProps:props bridge:bridge];
//    }
//    
//    // right
//    NSString *componentRight = props[@"componentRight"];
//    if (componentRight) {
//        rightVC = [[RCCViewController alloc] initWithComponent:componentRight passProps:props[@"passPropsRight"] navigatorStyle:nil globalProps:props bridge:bridge];
//    }
//    
//    self = [super initWithContentViewController:centerVC
//                      leftSidebarViewController:leftVC
//                     rightSidebarViewController:rightVC];
//    if (!self) return nil;
//    
//    self.leftViewController = leftVC;
//    self.rightViewController = rightVC;
//    self.centerViewController = centerVC;
//    self.drawerStyle = props[@"style"];
//    self.delegate = self;
//    self.isOpen = NO;
//    [self setAnimationType:props[@"animationType"]];
//    [self setStyle];
//    
//    return self;
//}
//
//
//-(void)viewDidDisappear:(BOOL)animated {
//    [super viewDidDisappear:animated];
//    
//    UIWindow *appDelegateWindow = [[[UIApplication sharedApplication] delegate] window];
//    [appDelegateWindow setBackgroundColor:self.originalWindowBackgroundColor];
//    
//}
//
//
//-(void)setStyle:(TheSideBarSide)side {
//    
//    if(side == TheSideBarSideLeft && !self.leftViewController) return;
//    if(side == TheSideBarSideRight && !self.rightViewController) return;
//    
//    CGRect sideBarFrame = self.view.frame;
//    
//    switch (side) {
//        case TheSideBarSideLeft:
//        {
//            NSNumber *leftDrawerWidth = self.drawerStyle[@"leftDrawerWidth"];
//            if (!leftDrawerWidth) leftDrawerWidth = [NSNumber numberWithInteger:DRAWER_DEFAULT_WIDTH_PERCENTAGE];
//            
//            self.visibleWidth = self.view.bounds.size.width * MIN(1, (leftDrawerWidth.floatValue/100.0));
//            sideBarFrame.size.width = self.view.bounds.size.width * MIN(1, (leftDrawerWidth.floatValue/100.0));
//            self.leftViewController.view.frame = sideBarFrame;
//        }
//            break;
//            
//        case TheSideBarSideRight:
//        {
//            NSNumber *rightDrawerWidth = self.drawerStyle[@"rightDrawerWidth"];
//            if (!rightDrawerWidth) rightDrawerWidth = [NSNumber numberWithInteger:DRAWER_DEFAULT_WIDTH_PERCENTAGE];
//            
//            self.visibleWidth = self.view.bounds.size.width * MIN(1, (rightDrawerWidth.floatValue/100.0));
//            sideBarFrame.size.width = self.view.bounds.size.width * MIN(1, (rightDrawerWidth.floatValue/100.0));
//            sideBarFrame.origin.x = self.view.frame.size.width - self.visibleWidth;
//            self.rightViewController.view.frame = sideBarFrame;
//        }
//            break;
//            
//        default:
//            break;
//    }
//}
//
//
//-(void)setStyle {
//    
//    [self setStyle:TheSideBarSideLeft];
//    [self setStyle:TheSideBarSideRight];
//    
//    NSString *contentOverlayColor = self.drawerStyle[@"contentOverlayColor"];
//    if (contentOverlayColor)
//    {
//        UIColor *color = contentOverlayColor != (id)[NSNull null] ? [RCTConvert UIColor:contentOverlayColor] : nil;
//        self.overlayContentColor = color;
//    }
//    
//    UIImage *backgroundImage = nil;
//    id icon = self.drawerStyle[@"backgroundImage"];
//    UIWindow *appDelegateWindow = [[[UIApplication sharedApplication] delegate] window];
//    self.originalWindowBackgroundColor = appDelegateWindow.backgroundColor;
//    
//    if (icon)
//    {
//        backgroundImage = [RCTConvert UIImage:icon];
//        if (backgroundImage) {
//            backgroundImage = [RCCDrawerHelper imageWithImage:backgroundImage scaledToSize:appDelegateWindow.bounds.size];
//            [appDelegateWindow setBackgroundColor:[UIColor colorWithPatternImage:backgroundImage]];
//        }
//    }
//}
//
//
//-(void)setAnimationType:(NSString*)type {
//    if ([type isEqualToString:@"airbnb"]) self.animationStyle = SidebarTransitionStyleAirbnb;
//    else if ([type isEqualToString:@"facebook"]) self.animationStyle = SidebarTransitionStyleFacebook;
//    else if ([type isEqualToString:@"luvocracy"]) self.animationStyle = SidebarTransitionStyleLuvocracy;
//    else if ([type isEqualToString:@"wunder-list"]) self.animationStyle = SidebarTransitionStyleWunderlist;
//    
//    //    currently unsuported animation types
//    //    else if ([type isEqualToString:@"feedly"]) self.animationStyle = SidebarTransitionStyleFeedly;
//    //    else if ([type isEqualToString:@"flipboard"]) self.animationStyle = SidebarTransitionStyleFlipboard;
//    
//    
//    // default
//    else self.animationStyle = SidebarTransitionStyleAirbnb;
//}
//
//- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge {
//    
//    TheSideBarSide side = TheSideBarSideLeft;
//    
//    if ([actionParams[@"side"] isEqualToString:@"right"]) side = TheSideBarSideRight;
//    
//    // open
//    if ([performAction isEqualToString:@"open"])
//    {
//        [self openSideMenu:side];
//        return;
//    }
//    
//    // close
//    if ([performAction isEqualToString:@"close"])
//    {
//        [self overlayButtonPressed:self.overlayButton];
//        return;
//    }
//    
//    // toggle
//    if ([performAction isEqualToString:@"toggle"])
//    {
//        [self setStyle:side];
//        if (self.isOpen) {
//            [self overlayButtonPressed:self.overlayButton];
//        }
//        else {
//            [self openSideMenu:side];
//        }
//        self.isOpen = !self.isOpen;
//        return;
//    }
//    
//    // setStyle
//    if ([performAction isEqualToString:@"setStyle"])
//    {
//        if (actionParams[@"animationType"]) {
//            NSString *animationTypeString = actionParams[@"animationType"];
//            
//            CGRect leftSideBarFrame = self.leftViewController.view.frame;
//            leftSideBarFrame.origin.x = 0;
//            self.leftViewController.view.frame = leftSideBarFrame;
//            
//            CGRect rightSideBarFrame = self.rightViewController.view.frame;
//            rightSideBarFrame.origin.x = self.view.frame.size.width - self.visibleWidth;
//            self.rightViewController.view.frame = rightSideBarFrame;
//            
//            [self setAnimationType:animationTypeString];
//        }
//        return;
//    }
//}
//
//
//-(void)openSideMenu:(TheSideBarSide)side{
//    
//    RCCDrawerSide drawerSide;
//    
//    switch (side) {
//        case TheSideBarSideLeft:
//        {
//            [self presentLeftSidebarViewControllerWithStyle:self.animationStyle];
//            drawerSide = RCCDrawerSideLeft;
//        }
//            
//            break;
//        case TheSideBarSideRight:
//        {
//            [self presentRightSidebarViewControllerWithStyle:self.animationStyle];
//            drawerSide = RCCDrawerSideRight;
//        }
//            
//            break;
//            
//        default:
//            break;
//    }
//    
//    [RCCDrawerHelper addOverlayButtonToScreen:self.overlayButton contextView:self.view side:drawerSide sideMenuWidth:self.visibleWidth animationDuration:self.animationDuration];
//}
//
//-(void)overlayButtonPressed:(UIButton*)button {
//    [self dismissSidebarViewController];
//    [RCCDrawerHelper overlayButtonPressed:button animationDuration:self.animationDuration];
//    self.isOpen = NO;
//}
//
//
//@end
