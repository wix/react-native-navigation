#import <React/RCTConvert.h>
#import "MMDrawerController.h"

@interface RCTConvert (SideMenuOpenGestureMode)

@end

@implementation RCTConvert (SideMenuOpenGestureMode)

RCT_ENUM_CONVERTER(MMOpenDrawerGestureMode,
				   (@{@"entireScreen": @(MMOpenDrawerGestureModeAll),
					  @"bezel": @(MMOpenDrawerGestureModeBezelPanningCenterView),
					  }), MMOpenDrawerGestureModeAll, integerValue)

@end

