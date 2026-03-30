#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNFontAttributesCreator : NSObject

+ (NSDictionary *)createWithFontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color
                              centered:(BOOL)centered;

+ (NSDictionary *)createFromDictionary:(NSDictionary *)attributesDictionary
                            fontFamily:(NSString *)fontFamily
                              fontSize:(NSNumber *)fontSize
                            fontWeight:(NSString *)fontWeight
                                 color:(UIColor *)color
                              centered:(BOOL)centered;

@end
