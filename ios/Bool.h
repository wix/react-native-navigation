#import "Param.h"

@interface Bool : Param

- (instancetype)initWithBOOL:(BOOL)boolValue;

- (BOOL)get;

- (NSNumber *)getValue;

- (BOOL)withDefault:(BOOL)value;

- (bool)isFalse;

+ (instancetype)withValue:(BOOL)value;

@end
