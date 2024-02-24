#import "RNNParam.h"
#import <Foundation/Foundation.h>

@interface Enum : RNNParam

- (int)get;

- (int)withDefault:(int)defaultValue;

- (int)convertString:(NSString *)string;

@end
