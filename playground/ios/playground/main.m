/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#import <UIKit/UIKit.h>

#import "AppDelegate.h"

static bool isRunningTests()
{
	NSDictionary* environment = [[NSProcessInfo processInfo] environment];
	NSString* testEnabled = environment[@"TEST_ENABLED"];
	return [testEnabled isEqualToString:@"YES"];
}

int main(int argc, char * argv[]) {
	@autoreleasepool {
		if (isRunningTests()) {
			return UIApplicationMain(argc, argv, nil, nil);
		} else {
			return UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
		}
	}
}
