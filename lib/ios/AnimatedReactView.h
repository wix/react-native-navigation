#import "RNNDisplayLinkAnimation.h"
#import "RNNViewLocation.h"
#import "RNNSharedElementTransitionOptions.h"

@interface AnimatedReactView : UIView

@property(nonatomic, strong) RNNViewLocation *location;
@property(nonatomic, strong) UIView *reactView;

- (NSNumber *)reactZIndex;

- (instancetype)initElement:(UIView *)element
                  toElement:(UIView *)toElement
          transitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions;

- (void)reset;

- (NSArray<id<RNNDisplayLinkAnimation>> *)extraAnimations;

@end
