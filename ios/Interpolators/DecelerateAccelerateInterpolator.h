//
//  DecelerateAccelerateInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "Interpolator.h"
#import <Foundation/Foundation.h>

@interface DecelerateAccelerateInterpolator : NSObject <Interpolator>

- (instancetype)init;

@end
