//
//  InteractivePopGestureDelegate.h
//  ReactNativeNavigation
//
//  Created by Arman Dezfuli-Arjomandi on 1/10/19.
//  Copyright © 2019 Wix. All rights reserved.
//
//

// This file is adapted from the following StackOverflow answer:
// https://stackoverflow.com/questions/24710258/no-swipe-back-when-hiding-navigation-bar-in-uinavigationcontroller/41895151#41895151

#import <UIKit/UIKit.h>

@interface InteractivePopGestureDelegate : NSObject <UIGestureRecognizerDelegate>

@property (nonatomic, weak) UINavigationController *navigationController;
@property (nonatomic, weak) id<UIGestureRecognizerDelegate> originalDelegate;

@end
