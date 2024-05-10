#import <Foundation/Foundation.h>

@interface Param : NSObject <NSCopying>

+ (instancetype)withValue:(id)value;

- (instancetype)initWithValue:(id)value;

- (id)get;

- (id)withDefault:(id)defaultValue;

- (BOOL)hasValue;

- (void)consume;

@end
