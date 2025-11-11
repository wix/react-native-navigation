#import "RNNDictionary.h"
#import <Foundation/Foundation.h>

@interface DictionaryParser : NSObject

+ (RNNDictionary *)parse:(NSDictionary *)json key:(NSString *)key;

@end
