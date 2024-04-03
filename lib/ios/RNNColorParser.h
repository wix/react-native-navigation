#import "RNNColor.h"
#import <Foundation/Foundation.h>

@interface RNNColorParser : NSObject

+ (RNNColor *)parse:(NSDictionary *)json key:(NSString *)key;

@end
