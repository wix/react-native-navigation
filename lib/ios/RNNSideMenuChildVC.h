//
//  RNNSideMenuChildVC.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNParentProtocol>

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNParentProtocol> *child;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;

-(instancetype) initWithChild:(UIViewController<RNNParentProtocol>*)child type:(RNNSideMenuChildType)type;

@end
