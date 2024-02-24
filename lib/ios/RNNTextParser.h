#import "Text.h"
#import <Foundation/Foundation.h>

@interface RNNTextParser : NSObject

+ (Text *)parse:(NSDictionary *)json key:(NSString *)key;

@end
