#import <Foundation/Foundation.h>
#import "Param.h"

@interface Enum : Param

- (int)get;

- (int)getWithDefaultValue:(int)defaultValue;

- (int)convertString:(NSString *)string;

@end
