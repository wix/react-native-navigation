#import "RNNSideMenuOpenMode.h"
#import <Foundation/Foundation.h>

@interface RNNSideMenuOpenGestureModeParser : NSObject

+ (RNNSideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key;

@end
