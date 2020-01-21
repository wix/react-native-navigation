#import "Double.h"

@interface TimeInterval : Double

- (NSTimeInterval)get;

- (NSTimeInterval)getWithDefaultValue:(double)defaultValue;

@end
