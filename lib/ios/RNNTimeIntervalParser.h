#import "RNNRNNTimeInterval.h"
#import <Foundation/Foundation.h>

@interface RNNTimeIntervalParser : NSObject

+ (RNNTimeInterval *)parse:(NSDictionary *)json key:(NSString *)key;

@end
