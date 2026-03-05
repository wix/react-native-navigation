#import <Foundation/Foundation.h>
#ifndef RCT_NEW_ARCH_ENABLED
#import <React/RCTRootView.h>
#endif

@interface RCTHelpers : NSObject
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary
                                           withPrefix:(NSString *)prefix;
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary
                                           withPrefix:(NSString *)prefix
                                             baseFont:(UIFont *)font;
+ (NSString *)getTimestampString;
#ifndef RCT_NEW_ARCH_ENABLED
+ (BOOL)removeYellowBox:(RCTRootView *)reactRootView;
#endif
@end
