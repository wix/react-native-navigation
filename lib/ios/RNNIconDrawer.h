#import <Foundation/Foundation.h>

@interface RNNIconDrawer : NSObject

- (UIImage *)draw:(UIImage *)image
         imageColor:(UIColor *)imageColor
    backgroundColor:(UIColor *)color
               size:(CGSize)size
       cornerRadius:(CGFloat)cornerRadius;

@end
