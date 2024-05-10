#import "Param.h"
#import <Foundation/Foundation.h>

@interface Enum : Param

- (int)get;

- (int)withDefault:(int)defaultValue;

- (int)convertString:(NSString *)string;

@end
