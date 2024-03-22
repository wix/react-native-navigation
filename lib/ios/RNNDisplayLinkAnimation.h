#import "RNNInterpolatorProtocol.h"
#import "RNNInterpolator.h"
#import <Foundation/Foundation.h>

@protocol RNNDisplayLinkAnimation <NSObject>

@required

- (CATransform3D)animateWithProgress:(CGFloat)p;

- (void)end;

- (NSTimeInterval)duration;

- (NSTimeInterval)startDelay;

- (id<RNNInterpolatorProtocol>)interpolator;

@end
