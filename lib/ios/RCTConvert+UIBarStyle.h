#import <React/RCTConvert.h>
#import <UIKit/UIKit.h>

@interface RCTConvert (UIBarStyle)

#if !TARGET_OS_TV
+ (UIBarStyle)UIBarStyle:(id)json __deprecated;
#endif

@end
