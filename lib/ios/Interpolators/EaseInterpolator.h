//
//  EaseInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "Interpolator.h"

@interface EaseInterpolator : NSObject<Interpolator>

@property (readonly) float from;
@property (readonly) float to;

- (instancetype)init:(float)from to:(float)to;

@end
