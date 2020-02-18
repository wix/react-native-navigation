#import "RNNViewLocation.h"
#import "SharedElementTransitionOptions.h"

@interface AnimatedReactView : UIView

@property (nonatomic, strong) RNNViewLocation* location;
@property (nonatomic, strong) UIView* reactView;

- (NSNumber *)reactZIndex;

- (instancetype)initElement:(UIView*)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions;

- (void)reset;

@end
