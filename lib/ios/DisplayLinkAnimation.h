#import <Foundation/Foundation.h>
#import "RNNInterpolator.h"
#import "Interpolator.h"

@protocol DisplayLinkAnimation <NSObject>

@required

- (CATransform3D)animateWithProgress:(CGFloat)p;

- (void)end;

- (NSTimeInterval)duration;

- (NSTimeInterval)startDelay;

- (id<Interpolator>)interpolator;

@end
