#import "RNNDouble.h"

@interface RNNTimeInterval : RNNDouble

- (NSTimeInterval)get;

- (NSTimeInterval)withDefault:(double)defaultValue;

@end
