#import <Foundation/Foundation.h>
#import "String.h"

@interface StringParser : NSObject

+ (String *)parse:(NSDictionary *)json key:(NSString *)key;

@end
