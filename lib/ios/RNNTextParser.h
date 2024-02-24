#import "RNNText.h"
#import <Foundation/Foundation.h>

@interface RNNTextParser : NSObject

+ (RNNText *)parse:(NSDictionary *)json key:(NSString *)key;

@end
