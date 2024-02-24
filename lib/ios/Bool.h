#import "RNNParam.h"

@interface Bool : RNNParam

- (instancetype)initWithBOOL:(BOOL)boolValue;

- (BOOL)get;

- (NSNumber *)getValue;

- (BOOL)withDefault:(BOOL)value;

- (bool)isFalse;

+ (instancetype)withValue:(BOOL)value;

@end
