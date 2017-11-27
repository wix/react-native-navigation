#import <Foundation/Foundation.h>
#import <React/RCTRootView.h>

@interface RCTHelpers : NSObject
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix;
+ (NSMutableDictionary *)textAttributesFromDictionary:(NSDictionary *)dictionary withPrefix:(NSString *)prefix baseFont:(UIFont *)font;
+ (NSString*)getTimestampString;
+ (UILabel *)labelWithFrame:(CGRect)frame textAttributes:(NSDictionary *)textAttributes andText:(NSString *)text;
+ (UILabel *)labelWithFrame:(CGRect)frame textAttributesFromDictionary:(NSDictionary *)dictionary baseFont:(UIFont *)font andText:(NSString *)text;
@end
