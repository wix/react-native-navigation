#import "RNNOptions.h"

@interface RNNShadowOptions : RNNOptions

@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Number *radius;
@property(nonatomic, strong) Number *opacity;

- (BOOL)hasValue;

@end
