#import "UIColor+RNNUtils.h"

// Observer 2°, D65 Illuminant
#define REFX_O2_D65 95.047
#define REFY_O2_D65 100.0
#define REFZ_O2_D65 108.883

@implementation UIColor (RNNUtils)

#pragma mark Public

- (BOOL)isTransparent {
    return (CGColorGetAlpha(self.CGColor) == 0.0);
}

- (NSString *)toHex {
    const CGFloat *components = CGColorGetComponents([self CGColor]);

    CGFloat r = components[0];
    CGFloat g = components[1];
    CGFloat b = components[2];

    return [NSString stringWithFormat:@"#%02lX%02lX%02lX", lroundf((float)(r * 255)),
                                      lroundf((float)(g * 255)), lroundf((float)(b * 255))];
}

+ (void)rgbToLabx:(CGFloat)r
                g:(CGFloat)g
                b:(CGFloat)b
                l:(CGFloat *)outL
                a:(CGFloat *)outA
                b:(CGFloat *)outB {
    CGFloat x, y, z;
    [UIColor rgbToXYZr:r g:g b:b x:&x y:&y z:&z];
    [UIColor xyzToLabx:x y:y z:z l:outL a:outA b:outB];
}

#pragma mark Private
+ (void)rgbToXYZr:(CGFloat)r
                g:(CGFloat)g
                b:(CGFloat)b
                x:(CGFloat *)outX
                y:(CGFloat *)outY
                z:(CGFloat *)outZ {
    if (r > 0.04045) {
        r = pow(((r + 0.055) / 1.055), 2.4);
    } else {
        r = r / 12.92;
    }
    if (g > 0.04045) {
        g = pow(((g + 0.055) / 1.055), 2.4);
    } else {
        g = g / 12.92;
    }
    if (b > 0.04045) {
        b = pow(((b + 0.055) / 1.055), 2.4);
    } else {
        b = b / 12.92;
    }

    r *= 100.0;
    g *= 100.0;
    b *= 100.0;

    *outX = (r * 0.4124) + (g * 0.3576) + (b * 0.1805);
    *outY = (r * 0.2126) + (g * 0.7152) + (b * 0.0722);
    *outZ = (r * 0.0193) + (g * 0.1192) + (b * 0.9505);
}

+ (void)xyzToLabx:(CGFloat)x
                y:(CGFloat)y
                z:(CGFloat)z
                l:(CGFloat *)outL
                a:(CGFloat *)outA
                b:(CGFloat *)outB {
    x /= REFX_O2_D65;
    y /= REFY_O2_D65;
    z /= REFZ_O2_D65;

    if (x > 0.008856) {
        x = pow(x, 1.0 / 3.0);
    } else {
        x = (7.787 * x) + (16.0 / 116.0);
    }
    if (y > 0.008856) {
        y = pow(y, 1.0 / 3.0);
    } else {
        y = (7.787 * y) + (16.0 / 116.0);
    }
    if (z > 0.008856) {
        z = pow(z, 1.0 / 3.0);
    } else {
        z = (7.787 * z) + (16.0 / 116.0);
    }

    *outL = (116.0 * y) - 16.0;
    *outA = 500.0 * (x - y);
    *outB = 200.0 * (y - z);
}

@end
