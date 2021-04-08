#import "Color.h"
#import <Foundation/Foundation.h>

@interface ColorParser : NSObject

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key;

@end
