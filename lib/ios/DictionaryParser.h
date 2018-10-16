#import <Foundation/Foundation.h>
#import "Dictionary.h"

@interface DictionaryParser : NSObject

+ (Dictionary *)parse:(NSDictionary *)json key:(NSString *)key;

@end
