#import "RNNViewLocation.h"
#import "DisplayLinkAnimation.h"
#import "SharedElementTransitionOptions.h"

@interface AnimatedReactView : UIView <DisplayLinkAnimation>

@property (nonatomic, strong) RNNViewLocation* location;
@property (nonatomic, strong) UIView* reactView;

- (instancetype)initElement:(UIView*)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions;

@end
