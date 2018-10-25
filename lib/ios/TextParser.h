#import <Foundation/Foundation.h>
#import "Text.h"

@interface TextParser : NSObject

+ (Text *)parse:(NSDictionary *)json key:(NSString *)key;

@end
