#import "RNNParam.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNNumber : RNNParam

- (NSNumber *)get;

- (NSNumber *)withDefault:(NSNumber *)defaultValue;

@end
