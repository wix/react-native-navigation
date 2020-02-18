#import <Foundation/Foundation.h>
#import "RNNInterpolator.h"

@protocol DisplayLinkAnimation <NSObject>

@required

- (CATransform3D)animateWithProgress:(CGFloat)p;

- (void)end;

- (NSTimeInterval)duration;

- (NSTimeInterval)startDelay;

- (RNNInterpolationOptions)interpolation;

@end
