//
//  RNNSideMenuChildVC.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNRootViewProtocol>

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNRootViewProtocol> *child;

-(instancetype) initWithChild:(UIViewController<RNNRootViewProtocol>*)child type:(RNNSideMenuChildType)type;

@end
