#import <Foundation/Foundation.h>
#import "Image.h"

@interface ImageParser : NSObject

+ (Image *)parse:(NSDictionary *)json key:(NSString *)key;

@end
