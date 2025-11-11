#import "Enum.h"
#import <Foundation/Foundation.h>

@interface EnumParser : NSObject

+ (Enum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)clazz;

@end
