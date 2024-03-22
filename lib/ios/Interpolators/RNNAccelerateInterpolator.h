//
//  RNNAccelerateInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "RNNInterpolatorProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNAccelerateInterpolator : NSObject <RNNInterpolatorProtocol>

@property(readonly) CGFloat factor;

- (instancetype)init:(CGFloat)factor;

@end
