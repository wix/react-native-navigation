#import "Param.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Number : Param

- (NSNumber *)get;

- (NSNumber *)withDefault:(NSNumber *)defaultValue;

@end
