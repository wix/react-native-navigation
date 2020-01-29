#import "UIColor+RNNUtils.h"

@implementation UIColor (RNNUtils)
- (NSString *)toHex {
    const CGFloat *components = CGColorGetComponents([self CGColor]);

    CGFloat r = components[0];
    CGFloat g = components[1];
    CGFloat b = components[2];

    return [NSString stringWithFormat:@"#%02lX%02lX%02lX",
                                      lroundf((float) (r * 255)),
                                      lroundf((float) (g * 255)),
                                      lroundf((float) (b * 255))];
}

@end