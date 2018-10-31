#import "Color.h"

@interface Color()

@property (nonatomic, retain) UIColor* value;

@end

@implementation Color

- (instancetype)initWithValue:(UIColor *)value {
	return [super initWithValue:value];
}

- (UIColor *)get {
	return self.value;
}

- (UIColor *)getWithDefaultValue:(id)defaultValue {
	return [super getWithDefaultValue:defaultValue];
}

@end
