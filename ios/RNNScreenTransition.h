#import "ElementTransitionOptions.h"
#import "RNNEnterExitAnimation.h"
#import "RNNOptions.h"
#import "SharedElementTransitionOptions.h"
#import "Text.h"

@class UIViewController;

@interface RNNScreenTransition : RNNOptions

@property(nonatomic, strong) ElementTransitionOptions *topBar;
@property(nonatomic, strong) RNNEnterExitAnimation *content;
@property(nonatomic, strong) ElementTransitionOptions *bottomTabs;
@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<SharedElementTransitionOptions *> *sharedElementTransitions;
@property(nonatomic, strong) Text *zoomFromId;
@property(nonatomic, strong) Bool *zoomEnabled;

@property(nonatomic, strong) Bool *enable;
@property(nonatomic, strong) Bool *waitForRender;
@property(nonatomic, strong) TimeInterval *duration;

- (BOOL)hasCustomAnimation;
- (BOOL)hasZoomTransition;
- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;
- (void)applyZoomToViewController:(UIViewController *)destination
         fromSourceViewController:(UIViewController *)source;

@end
