#import "Param.h"

@interface Bool : Param

- (instancetype)initWithBOOL:(BOOL)boolValue;

- (BOOL)get;

- (NSNumber *)getValue;

- (BOOL)getWithDefaultValue:(BOOL)value;

- (bool) isFalse;

@end
