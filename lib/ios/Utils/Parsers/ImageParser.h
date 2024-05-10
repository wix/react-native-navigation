#import "Image.h"
#import <Foundation/Foundation.h>

@interface ImageParser : NSObject

+ (Image *)parse:(NSDictionary *)json key:(NSString *)key;

@end
