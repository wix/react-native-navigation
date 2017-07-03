//
//  RCCKYDrawerController.m
//  ReactNativeNavigation
//
//  Created by Egor Khmelev on 30/06/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import "RCCKYDrawerController.h"
#import "RCCViewController.h"
#import <React/RCTConvert.h>

@interface RCCKYDrawerController ()

@end

@implementation RCCKYDrawerController

@synthesize overlayButton = _overlayButton, drawerStyle = _drawerStyle;

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge {
    if ([children count] < 1) return nil;
    
    UIViewController *centerVC = [RCCViewController controllerWithLayout:children[0] globalProps:props bridge:bridge];
    UIViewController *leftVC = nil;
    UIViewController *rightVC = nil;
    
    // left
    NSString *componentLeft = props[@"componentLeft"];
    if (componentLeft)  {
        leftVC = [[RCCViewController alloc] initWithComponent:componentLeft passProps:props[@"passPropsLeft"] navigatorStyle:nil globalProps:props bridge:bridge];
    }
    
    // right
    NSString *componentRight = props[@"componentRight"];
    if (componentRight) {
        rightVC = [[RCCViewController alloc] initWithComponent:componentRight passProps:props[@"passPropsRight"] navigatorStyle:nil globalProps:props bridge:bridge];
    }
    
    self = [super init];
    if (!self) return nil;
    
    self.mainViewController = centerVC;
    
    if (rightVC) {
        self.drawerDirection = KYDrawerControllerDrawerDirectionRight;
        self.drawerViewController = rightVC;
    } else {
        self.drawerViewController = leftVC;
    }
    
    self.drawerStyle = props[@"style"];
    [self setStyleWithProps:self.drawerStyle];
    
    return self;
    
}

- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge {
    // open
    if ([performAction isEqualToString:@"open"])
    {
        if (self.drawerState != KYDrawerControllerDrawerStateOpened) {
            [self setDrawerState:KYDrawerControllerDrawerStateOpened animated:YES];
        }
        return;
    }
    
    // close
    if ([performAction isEqualToString:@"close"])
    {
        if (self.drawerState != KYDrawerControllerDrawerStateClosed) {
            [self setDrawerState:KYDrawerControllerDrawerStateClosed animated:YES];
        }
        return;
    }
    
    // toggle
    if ([performAction isEqualToString:@"toggle"])
    {
        if (self.drawerState == KYDrawerControllerDrawerStateOpened) {
            [self setDrawerState:KYDrawerControllerDrawerStateClosed animated:YES];
        } else {
            [self setDrawerState: KYDrawerControllerDrawerStateOpened animated:YES];
        }
        return;
    }

    // setStyle
    if ([performAction isEqualToString:@"setStyle"])
    {
        [self setStyleWithProps:actionParams];
        return;
    }
    
}

- (void)setStyleWithProps:(NSDictionary *)styleProps {
    if (styleProps[@"drawerWidth"]) {
        self.drawerWidth = (CGFloat) [RCTConvert float:styleProps[@"drawerWidth"]];
    }
    
    if (styleProps[@"drawerAnimationDuration"]) {
        self.drawerAnimationDuration = (NSTimeInterval) ([RCTConvert float:styleProps[@"drawerAnimationDuration"]] / 1000.f);
    }
    
    if (styleProps[@"containerViewMaxAlpha"]) {
        self.containerViewMaxAlpha = (CGFloat) [RCTConvert float:styleProps[@"containerViewMaxAlpha"]];
    }

    if (styleProps[@"screenEdgePanGestreEnabled"]) {
        self.screenEdgePanGestreEnabled = [RCTConvert BOOL:styleProps[@"screenEdgePanGestreEnabled"]];
    }
}


@end
