#import "Number.h"
#import <Foundation/Foundation.h>

@interface NumberParser : NSObject

+ (Number *)parse:(NSDictionary *)json key:(NSString *)key;

@end
