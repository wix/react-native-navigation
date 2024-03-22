#import "RNNOptions.h"

@interface RNNShadowOptions : RNNOptions

@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNNumber *radius;
@property(nonatomic, strong) RNNNumber *opacity;

- (BOOL)hasValue;

@end
