#import <Foundation/Foundation.h>

@interface OptionsArrayParser : NSObject

+ (NSArray *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)className;

@end
