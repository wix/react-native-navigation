#import "RNNEnum.h"
#import <Foundation/Foundation.h>

@interface RNNEnumParser : NSObject

+ (RNNEnum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)clazz;

@end
