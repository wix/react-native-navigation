#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <rnnavigation/rnnavigation.h>
#endif

@interface Constants : NSObject

+ (NSDictionary *)getConstants;

#ifdef RCT_NEW_ARCH_ENABLED
+ (JS::NativeRNNTurboModule::Constants::Builder::Input)getTurboConstants;
#endif

@end
