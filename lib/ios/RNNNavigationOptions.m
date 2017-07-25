//
//  RNNNavigationOptions.m
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 25/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import "RNNNavigationOptions.h"

@implementation RNNNavigationOptions

+(instancetype)create:(NSDictionary *)json
{
	RNNNavigationOptions* node = [RNNNavigationOptions new];
	node.data = json;
	return node;
}
@end
