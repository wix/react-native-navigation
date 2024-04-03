#import <Foundation/Foundation.h>

@interface RNNParam : NSObject <NSCopying>

+ (instancetype)withValue:(id)value;

- (instancetype)initWithValue:(id)value;

- (id)get;

- (id)withDefault:(id)defaultValue;

- (BOOL)hasValue;

- (void)consume;

@end
