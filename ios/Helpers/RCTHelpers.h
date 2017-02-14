//
//  RCTHelpers.h
//  ReactNativeControllers
//
//  Created by Artal Druk on 25/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import <Foundation/Foundation.h>

#if __has_include(<React/RCTRootView.h>)
#import <React/RCTRootView.h>
#elif __has_include("RCTRootView.h")
#import "RCTRootView.h"
#elif __has_include("React/RCTRootView.h")
#import "React/RCTRootView.h"   // Required when used as a Pod in a Swift project
#endif

@interface RCTHelpers : NSObject
+(BOOL)removeYellowBox:(RCTRootView*)reactRootView;
@end
