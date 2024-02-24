#import <Foundation/Foundation.h>

@interface RNNOptionsArrayParser : NSObject

+ (NSArray *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)className;

@end
