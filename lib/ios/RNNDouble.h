#import "RNNParam.h"

@interface RNNDouble : RNNParam

+ (instancetype)withValue:(double)value;

- (double)get;

- (double)withDefault:(double)defaultValue;

@end
