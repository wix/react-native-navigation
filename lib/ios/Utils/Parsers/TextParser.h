#import "Text.h"
#import <Foundation/Foundation.h>

@interface TextParser : NSObject

+ (Text *)parse:(NSDictionary *)json key:(NSString *)key;

@end
