#import <Foundation/Foundation.h>
#import "SideMenuOpenMode.h"

@interface SideMenuOpenGestureModeParser : NSObject

+ (SideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key;

@end
