#import <Foundation/Foundation.h>

@interface Param : NSObject

- (instancetype)initWithValue:(id)value;

- (id)get;

- (id)getWithDefaultValue:(id)defaultValue;

- (BOOL)hasValue;

- (void)consume;

@end
