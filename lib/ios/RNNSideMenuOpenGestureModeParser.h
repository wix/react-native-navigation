#import "SideMenuOpenMode.h"
#import <Foundation/Foundation.h>

@interface RNNSideMenuOpenGestureModeParser : NSObject

+ (SideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key;

@end
