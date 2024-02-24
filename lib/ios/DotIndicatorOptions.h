#import "RNNOptions.h"

@interface DotIndicatorOptions : RNNOptions

@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) RNNNumber *size;
@property(nonatomic, strong) Bool *visible;

- (bool)hasValue;

@end
