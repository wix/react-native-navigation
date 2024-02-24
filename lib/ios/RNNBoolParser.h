#import "Bool.h"
#import <Foundation/Foundation.h>

@interface RNNBoolParser : NSObject

+ (Bool *)parse:(NSDictionary *)json key:(NSString *)key;

@end
