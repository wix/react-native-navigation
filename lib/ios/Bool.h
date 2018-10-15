#import "Param.h"

@interface Bool : Param

- (BOOL)get;

- (NSNumber *)getValue;

- (BOOL)getWithDefaultValue:(BOOL)value;

@end
