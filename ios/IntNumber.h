#import "Param.h"

@interface IntNumber : Param

- (NSUInteger)get;

- (NSUInteger)withDefault:(NSUInteger)defaultValue;

@end
