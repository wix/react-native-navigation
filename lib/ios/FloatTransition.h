#import <Foundation/Foundation.h>
#import "ElementBaseTransition.h"

@interface FloatTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view transitionDetails:(TransitionDetailsOptions *)transitionDetails;

- (instancetype)initWithView:(UIView *)view fromFloat:(CGFloat)from toFloat:(CGFloat)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

- (CGFloat)calculateFrom:(Double*)from;

- (CGFloat)calculateTo:(Double*)to;

@property (readonly) CGFloat initialValue;
@property (nonatomic) CGFloat from;
@property (nonatomic) CGFloat to;

@end
