//
//  RCCAppStyler.h
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 08/03/2017.
//  Copyright © 2017 artal. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RCCAppStyler : NSObject

@property (nonatomic, strong, readonly) NSDictionary *appStyle;

-(instancetype)initWithAppStyleProps:(NSDictionary*)appStyleProps;
-(NSString*)styleForKey:(NSString*)key;

@end
