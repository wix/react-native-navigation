#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNFontAttributesCreator : NSObject

+ (NSDictionary *)createFontAttributesWithFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color;

@end
