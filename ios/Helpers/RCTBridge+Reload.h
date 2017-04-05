//
//  Bridge.h
//  ReactNativeNavigation
//
//  Created by Simon Mitchell on 02/02/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import <UIKit/UIKit.h>

#if __has_include(<React/RCTDefines.h>)
#import <React/RCTDefines.h>
#elif __has_include("RCTDefines.h")
#import "RCTDefines.h"
#elif __has_include("React/RCTDefines.h")
#import "React/RCTDefines.h"   // Required when used as a Pod in a Swift project
#endif

/**
 * This notification is called when a bridge is reloaded.
 */
RCT_EXTERN NSString *const RCCReloadNotification;

