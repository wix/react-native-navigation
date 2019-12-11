#import <Foundation/Foundation.h>
#import "Enum.h"

@interface EnumParser : NSObject

+ (Enum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)class;

@end
