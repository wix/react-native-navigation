#import "RNNOptions.h"

@interface RNNDotIndicatorOptions : RNNOptions

@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNNumber *size;
@property(nonatomic, strong) RNNBool *visible;

- (bool)hasValue;

@end
