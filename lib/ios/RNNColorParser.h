#import "Color.h"
#import <Foundation/Foundation.h>

@interface RNNColorParser : NSObject

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key;

@end
