#import "ElementBaseTransition.h"
#import "AnimatedTextView.h"

@interface TextStorageTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view from:(NSTextStorage *)from to:(NSTextStorage *)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

@property (nonatomic, strong) AnimatedTextView* view;

@property (nonatomic, readonly, strong) NSTextStorage* from;
@property (nonatomic, readonly, strong) NSTextStorage* to;

@end
