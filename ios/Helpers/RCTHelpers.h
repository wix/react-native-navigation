//
//  RCTHelpers.h
//  ReactNativeControllers
//
//  Created by Artal Druk on 25/05/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCTRootView.h"

@interface RCTHelpers : NSObject
+(BOOL)removeYellowBox:(RCTRootView*)reactRootView;
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix;
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix baseFont:(UIFont *)font;
@end
