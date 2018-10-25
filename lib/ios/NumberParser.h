#import <Foundation/Foundation.h>
#import "Number.h"

@interface NumberParser : NSObject

+ (Number *)parse:(NSDictionary *)json key:(NSString *)key;

@end
