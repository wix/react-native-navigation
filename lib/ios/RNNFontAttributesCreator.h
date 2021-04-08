#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNFontAttributesCreator : NSObject

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color;

+ (NSDictionary *)createFromDictionary:(NSDictionary *)attributesDictionary
                            fontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color;

@end
