#import <Foundation/Foundation.h>
#import "RNNTransitionStateHolder.h"
#import "RNNAnimatedView.h"

@interface RNNTransition : NSObject

@property (nonatomic, strong) RNNAnimatedView* animatedView;
@property (nonatomic, strong) RNNTransitionStateHolder* options;

- (instancetype)initFromVC:(UIViewController*)fromVC toVC:(UIViewController*)toVC transitionOptions:(RNNTransitionStateHolder*)transitionOptions isBackButton:(BOOL)isBackButton;

- (void)animate;

- (void)setAnimatedViewFinalProperties;

- (void)transitionCompleted;

@end
