#import "RNNDouble.h"
#import <Foundation/Foundation.h>

@interface RNNDoubleParser : NSObject

+ (RNNDouble *)parse:(NSDictionary *)json key:(NSString *)key;

@end
