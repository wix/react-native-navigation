#import "Bool.h"
#import <Foundation/Foundation.h>

@interface BoolParser : NSObject

+ (Bool *)parse:(NSDictionary *)json key:(NSString *)key;

@end
