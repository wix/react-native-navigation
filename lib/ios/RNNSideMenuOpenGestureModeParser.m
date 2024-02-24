#import "RNNSideMenuOpenGestureModeParser.h"
#import "RCTConvert+SideMenuOpenGestureMode.h"

@implementation RNNSideMenuOpenGestureModeParser

+ (SideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[SideMenuOpenMode alloc]
                           initWithValue:@([RCTConvert MMOpenDrawerGestureMode:json[key]])]
                     : [[SideMenuOpenMode alloc] initWithValue:nil];
}

@end
