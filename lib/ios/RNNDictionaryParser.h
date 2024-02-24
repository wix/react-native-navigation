#import "RNNDictionary.h"
#import <Foundation/Foundation.h>

@interface RNNDictionaryParser : NSObject

+ (RNNDictionary *)parse:(NSDictionary *)json key:(NSString *)key;

@end
