#import "RCTConvert+SideMenuOpenGestureMode.h"

@implementation RCTConvert (SideMenuOpenGestureMode)

RCT_ENUM_CONVERTER(MMOpenDrawerGestureMode, (@{
                       @"entireScreen" : @(MMOpenDrawerGestureModeAll),
                       @"bezel" : @(MMOpenDrawerGestureModeBezelPanningCenterView),
                   }),
                   MMOpenDrawerGestureModeAll, integerValue)

@end
