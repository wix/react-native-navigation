#import "Double.h"

@interface TimeInterval : Double

- (NSTimeInterval)get;

- (NSTimeInterval)withDefault:(double)defaultValue;

@end
