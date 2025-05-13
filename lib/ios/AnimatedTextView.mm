#import "AnimatedTextView.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTParagraphComponentView.h>
#import <react/renderer/components/text/ParagraphComponentDescriptor.h>
#import <react/renderer/components/text/ParagraphProps.h>
#import <react/renderer/components/text/ParagraphState.h>
#endif

@implementation AnimatedTextView {
    NSTextContainer *_fromTextContainer;
	facebook::react::ParagraphShadowNode::ConcreteState::Shared _state;
    CGSize _fromSize;
}

- (instancetype)initElement:(UIView *)element
                  toElement:(UIView *)toElement
          transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self = [super initElement:element toElement:toElement transitionOptions:transitionOptions];
#ifdef RCT_NEW_ARCH_ENABLED
	auto castedElement = (RCTParagraphComponentView *)element;
	auto castedToElement = (RCTParagraphComponentView *)toElement;

    // Extract NSAttributedString from the view
    NSAttributedString *fromAttributedText = castedElement.attributedText;
    NSAttributedString *toAttributedText = castedToElement.attributedText;

    // Convert to NSTextStorage
    _fromTextStorage = [[NSTextStorage alloc] initWithAttributedString:fromAttributedText];
    _toTextStorage = [[NSTextStorage alloc] initWithAttributedString:toAttributedText];
#else
    _fromTextStorage = [element valueForKey:@"textStorage"];
    _toTextStorage = [toElement valueForKey:@"textStorage"];
#endif
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
