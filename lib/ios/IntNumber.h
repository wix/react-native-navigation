#import "Param.h"

@interface IntNumber : Param

- (NSUInteger)get;

- (NSUInteger)getWithDefaultValue:(NSUInteger)defaultValue;

@end
