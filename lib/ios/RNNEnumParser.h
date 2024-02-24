#import "Enum.h"
#import <Foundation/Foundation.h>

@interface RNNEnumParser : NSObject

+ (Enum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)clazz;

@end
