//
//  RNNOvershootInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "RNNInterpolatorProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNOvershootInterpolator : NSObject <RNNInterpolatorProtocol>

@property(readonly) CGFloat tension;

- (instancetype)init:(CGFloat)tension;

@end
