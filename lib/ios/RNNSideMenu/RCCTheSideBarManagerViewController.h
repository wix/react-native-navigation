//
//  DarwerTheSideBarManagerViewController.h
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 22/03/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import "TheSidebarController.h"
#import "RCCDrawerProtocol.h"

typedef NS_ENUM(NSInteger,TheSideBarSide){
    TheSideBarSideNone = 0,
    TheSideBarSideLeft,
    TheSideBarSideRight,
};


@interface RCCTheSideBarManagerViewController : TheSidebarController <RCCDrawerDelegate>


@end
