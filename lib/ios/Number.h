#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Param.h"

@interface Number : Param

- (NSNumber *)get;

- (NSNumber *)getWithDefaultValue:(NSNumber *)defaultValue;

@end
