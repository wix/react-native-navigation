#import <Foundation/Foundation.h>
#import <React/RCTRootView.h>

@interface RCTHelpers : NSObject
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix;
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix baseFont:(UIFont *)font;
+ (NSString*)getTimestampString;
+ (BOOL)removeYellowBox:(RCTRootView *)reactRootView;
@end
