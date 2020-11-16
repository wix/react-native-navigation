#import "AnimatedTextView.h"

@implementation AnimatedTextView {
    NSTextContainer *_fromTextContainer;
    CGSize _fromSize;
}

- (instancetype)initElement:(UIView *)element
                  toElement:(UIView *)toElement
          transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self = [super initElement:element toElement:toElement transitionOptions:transitionOptions];
    _fromTextStorage = [element valueForKey:@"textStorage"];
    _toTextStorage = [toElement valueForKey:@"textStorage"];
    _fromTextContainer = [self container:_fromTextStorage];
    _fromSize = _fromTextContainer.size;
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    _fromTextContainer.size = self.bounds.size;
    self.reactView.frame = self.bounds;
}

- (void)reset {
    [super reset];
    _fromTextContainer.size = _fromSize;
}

- (NSTextContainer *)container:(NSTextStorage *)fromTextStorage {
    return fromTextStorage.layoutManagers.firstObject.textContainers.firstObject;
}

@end
