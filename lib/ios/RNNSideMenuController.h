//
//  RNNSideMenuController.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RNNSideMenuChildVC.h"
#import "MMDrawerController.h"
#import "RNNRootViewProtocol.h"

@interface RNNSideMenuController : UIViewController <RNNRootViewProtocol>

@property (readonly) RNNSideMenuChildVC *center;
@property (readonly) RNNSideMenuChildVC *left;
@property (readonly) RNNSideMenuChildVC *right;
@property (readonly) MMDrawerController *sideMenu;

-(instancetype)initWithControllers:(NSArray*)controllers;

-(void)showSideMenu:(MMDrawerSide)side animated:(BOOL)animated;
-(void)hideSideMenu:(MMDrawerSide)side animated:(BOOL)animated;

@end
