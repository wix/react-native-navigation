#import <Foundation/Foundation.h>
#import "Color.h"

@interface ColorParser : NSObject

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key;

@end
