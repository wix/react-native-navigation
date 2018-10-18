#import "Image.h"

@interface Image()

@property (nonatomic, retain) UIImage* value;

@end

@implementation Image

- (UIImage *)get {
	return self.value;
}

- (UIImage *)getWithDefaultValue:(UIImage *)defaultValue {
	return [super getWithDefaultValue:defaultValue];
}

@end
