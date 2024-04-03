#import "RNNSideMenuOpenGestureModeParser.h"
#import "RCTConvert+SideMenuOpenGestureMode.h"

@implementation RNNSideMenuOpenGestureModeParser

+ (RNNSideMenuOpenMode *)parse:(NSDictionary *)json key:(NSString *)key {
  return json[key] ? [[RNNSideMenuOpenMode alloc]
                      initWithValue:@([RCTConvert MMOpenDrawerGestureMode:json[key]])]
      : [[RNNSideMenuOpenMode alloc] initWithValue:nil];
}

@end
