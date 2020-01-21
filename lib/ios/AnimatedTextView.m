#import "AnimatedTextView.h"
#import <React/RCTTextView.h>
#import "UIView+Utils.h"

@implementation AnimatedTextView {
    RCTTextView* _tv;
    NSTextContainer* _origTextContainer;
    CGSize _origSize;
    UIView* _origParent;
    UIColor* _fromColor;
    UIColor* _toColor;
    UIFont* _fromFont;
    UIFont* _toFont;
    NSTextStorage* _textStorage;
    NSTextStorage* _toTextStorage;
}

- (instancetype)initElement:(UIView *)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self = [super initElement:element toElement:toElement transitionOptions:transitionOptions];
    
    _textStorage = [element valueForKey:@"textStorage"];
    _origTextContainer = [self container:_textStorage];
    _toTextStorage = [toElement valueForKey:@"textStorage"];
    _origSize = _origTextContainer.size;
    
    [self prepareAnimationValues];
        
    return self;
}

- (void)prepareAnimationValues {
    NSRange range1;
    NSAttributedString* fromAttributes = [_textStorage attributedSubstringFromRange:NSMakeRange(0, _textStorage.string.length)];
    NSAttributedString* toAttributes = [_toTextStorage attributedSubstringFromRange:NSMakeRange(0, _toTextStorage.string.length)];
    
    _fromColor = [fromAttributes attribute:NSForegroundColorAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _textStorage.string.length)] ?: UIColor.blackColor;
    _toColor = [toAttributes attribute:NSForegroundColorAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _toTextStorage.string.length)] ?: UIColor.blackColor;
    
    _fromFont = [fromAttributes attribute:NSFontAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _textStorage.string.length)];
    _toFont = [toAttributes attribute:NSFontAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _toTextStorage.string.length)];
    
}

- (void)layoutSubviews {
    [super layoutSubviews];
    _origTextContainer.size = self.bounds.size;
}

- (CGAffineTransform)animateWithProgress:(CGFloat)progress {
    [super animateWithProgress:progress];
    
    NSRange range = NSMakeRange(0, _textStorage.string.length);
    UIColor* color = [RNNInterpolator fromColor:_fromColor toColor:_toColor precent:progress];
    [_textStorage addAttribute:NSForegroundColorAttributeName value:color range:range];
    CGFloat pointSize = [RNNInterpolator fromFloat:_fromFont.pointSize toFloat:_toFont.pointSize precent:progress interpolation:self.interpolation];
    [_textStorage addAttribute:NSFontAttributeName value:[_toFont fontWithSize:pointSize] range:range];
    
    return CGAffineTransformIdentity;
}

- (void)end {
    _origTextContainer.size = _origSize;
    [self resetTextStorage];
    [super end];
}

- (void)resetTextStorage {
    NSRange range = NSMakeRange(0, _textStorage.string.length);
    [_textStorage addAttribute:NSFontAttributeName value:_fromFont range:range];
    [_textStorage addAttribute:NSForegroundColorAttributeName value:_fromColor range:range];
}

- (NSTextContainer *)container:(NSTextStorage *)fromTextStorage {
    return fromTextStorage.layoutManagers.firstObject.textContainers.firstObject;
}

@end
