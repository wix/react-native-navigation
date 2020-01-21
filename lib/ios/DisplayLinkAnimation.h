#import <Foundation/Foundation.h>
#import "RNNInterpolator.h"

@protocol DisplayLinkAnimation <NSObject>

@required

- (CGAffineTransform)animateWithProgress:(CGFloat)p;

- (void)end;

- (NSTimeInterval)duration;

- (NSTimeInterval)startDelay;

- (RNNInterpolationOptions)interpolation;

@end
