#import "Color.h"

@interface Color()

@property (nonatomic, retain) UIColor* value;

@end

@implementation Color

+ (instancetype)withColor:(UIColor *)value {
    return [[Color alloc] initWithValue:value];
}

- (instancetype)initWithValue:(UIColor *)value {
	return [super initWithValue:value];
}

- (UIColor *)get {
	return self.value;
}

- (UIColor *)getWithDefaultValue:(id)defaultValue {
	return [super getWithDefaultValue:defaultValue];
}

-(NSString *)description {
	return [self hexStringFromColor:[self getWithDefaultValue:nil]];
}

- (NSString *)hexStringFromColor:(UIColor *)color {
    const CGFloat *components = CGColorGetComponents(color.CGColor);

    CGFloat r = components[0];
    CGFloat g = components[1];
    CGFloat b = components[2];

    return [NSString stringWithFormat:@"#%02lX%02lX%02lX",
                                      lroundf(r * 255),
                                      lroundf(g * 255),
                                      lroundf(b * 255)];
}

@end
