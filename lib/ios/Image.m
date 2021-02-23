#import "Image.h"

@interface Image ()

@property(nonatomic, retain) UIImage *value;

@end

@implementation Image

- (UIImage *)get {
    return self.value;
}

- (UIImage *)withDefault:(UIImage *)defaultValue {
    return [super withDefault:defaultValue];
}

@end
