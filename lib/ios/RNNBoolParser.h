#import "RNNBool.h"
#import <Foundation/Foundation.h>

@interface RNNBoolParser : NSObject

+ (RNNBool *)parse:(NSDictionary *)json key:(NSString *)key;

@end
