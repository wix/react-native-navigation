#import <Foundation/Foundation.h>
#import "ElementBaseTransition.h"

@interface FloatTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view transitionDetails:(TransitionDetailsOptions *)transitionDetails;

@property (readonly) CGFloat initialValue;
@property (nonatomic) CGFloat from;
@property (nonatomic) CGFloat to;

@end
