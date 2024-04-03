#import "RNNParam.h"
#import <Foundation/Foundation.h>

@interface RNNEnum : RNNParam

- (int)get;

- (int)withDefault:(int)defaultValue;

- (int)convertString:(NSString *)string;

@end
