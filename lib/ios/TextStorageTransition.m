#import "TextStorageTransition.h"
#import "RNNInterpolator.h"

@implementation TextStorageTransition {
	UIColor* _fromColor;
    UIColor* _toColor;
    UIFont* _fromFont;
    UIFont* _toFont;
}

- (instancetype)initWithView:(UIView *)view from:(NSTextStorage *)from to:(NSTextStorage *)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _from = from;
    _to = to;
	[self prepareAnimationValues];
    return self;
}

- (void)prepareAnimationValues {
    NSRange range1;
    NSAttributedString* fromAttributes = [_from attributedSubstringFromRange:NSMakeRange(0, _from.string.length)];
    NSAttributedString* toAttributes = [_to attributedSubstringFromRange:NSMakeRange(0, _to.string.length)];
    
    _fromColor = [fromAttributes attribute:NSForegroundColorAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _from.string.length)] ?: UIColor.blackColor;
    _toColor = [toAttributes attribute:NSForegroundColorAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _to.string.length)] ?: UIColor.blackColor;
    
    _fromFont = [fromAttributes attribute:NSFontAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _from.string.length)];
    _toFont = [toAttributes attribute:NSFontAttributeName atIndex:0 longestEffectiveRange:&range1 inRange:NSMakeRange(0, _to.string.length)];
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    NSRange range = NSMakeRange(0, _from.string.length);
    UIColor* color = [RNNInterpolator fromColor:_fromColor toColor:_toColor precent:p];
    [_from addAttribute:NSForegroundColorAttributeName value:color range:range];
    CGFloat pointSize = [RNNInterpolator fromFloat:_fromFont.pointSize toFloat:_toFont.pointSize precent:p interpolation:self.interpolation];
    [_from addAttribute:NSFontAttributeName value:[_toFont fontWithSize:pointSize] range:range];
	
    return CATransform3DIdentity;
}

- (void)end {
    NSRange range = NSMakeRange(0, _from.string.length);
    [_from addAttribute:NSFontAttributeName value:_fromFont range:range];
    [_from addAttribute:NSForegroundColorAttributeName value:_fromColor range:range];
}

@end
