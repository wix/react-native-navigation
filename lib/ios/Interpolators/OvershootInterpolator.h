//
//  OvershootInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Interpolator.h"

@interface OvershootInterpolator : NSObject<Interpolator>

@property (readonly) CGFloat from;
@property (readonly) CGFloat to;
@property (readonly) CGFloat tension;

- (instancetype)init:(CGFloat)from to:(CGFloat)to tension:(CGFloat)tension;

@end
