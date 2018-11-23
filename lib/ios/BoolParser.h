#import <Foundation/Foundation.h>
#import "Bool.h"

@interface BoolParser : NSObject

+ (Bool *)parse:(NSDictionary *)json key:(NSString *)key;

@end
