#import "AnimatedImageView.h"

@implementation AnimatedImageView

- (instancetype)initElement:(UIView *)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self = [super initElement:element toElement:toElement transitionOptions:transitionOptions];
    self.contentMode = element.contentMode;

    return self;
}

@end
