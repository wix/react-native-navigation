#import <Foundation/Foundation.h>
#import "ElementBaseTransition.h"

@interface ElementFrameTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view from:(CGRect)from to:(CGRect)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

@property (nonatomic) CGRect from;
@property (nonatomic) CGRect to;

@end
