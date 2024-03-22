#import "RNNImage.h"

@interface RNNImage ()

@property(nonatomic, retain) UIImage *value;

@end

@implementation RNNImage

- (UIImage *)get {
    return self.value;
}

- (UIImage *)withDefault:(UIImage *)defaultValue {
    return [super withDefault:defaultValue];
}

@end
