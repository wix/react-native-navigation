#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNOptions.h"
#import "RCTConvert+Interpolation.h"

@interface RNNInterpolation : RNNOptions

@property (nonatomic) CGFloat overshootTension;
@property (nonatomic) RNNInterpolationOptions interpolation;

@end
