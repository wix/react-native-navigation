#import "Dictionary.h"
#import <Foundation/Foundation.h>

@interface DictionaryParser : NSObject

+ (Dictionary *)parse:(NSDictionary *)json key:(NSString *)key;

@end
