//
//  RNNSideMenuChildVC.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController *child;

-(instancetype) initWithChild:(UIViewController*)child type:(RNNSideMenuChildType)type;

@end
