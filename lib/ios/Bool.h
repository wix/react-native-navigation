#import "Param.h"

@interface Bool : Param

- (BOOL)get;

- (NSNumber *)getValue;

- (BOOL)getWithDefaultValue:(id)value;

@end
