#import "SideMenuOpenMode.h"
#import <Foundation/Foundation.h>

@interface SideMenuOpenGestureModeParser : NSObject

+ (SideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key;

@end
