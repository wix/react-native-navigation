#import "RNNImage.h"
#import <Foundation/Foundation.h>

@interface RNNImageParser : NSObject

+ (RNNImage *)parse:(NSDictionary *)json key:(NSString *)key;

@end
