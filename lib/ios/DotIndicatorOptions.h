#import "RNNOptions.h"

@interface DotIndicatorOptions : RNNOptions

@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNNumber *size;
@property(nonatomic, strong) RNNBool *visible;

- (bool)hasValue;

@end
