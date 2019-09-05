#import <UIKit/UIKit.h>
#import "RNNEventEmitter.h"
#import "RNNLayoutProtocol.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface UIViewController (LayoutProtocol) <RNNLayoutProtocol>

- (void)renderTreeAndWait:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (UIViewController *)getCurrentChild;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (void)mergeChildOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)overrideOptions:(RNNNavigationOptions *)options;

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;
@property (nonatomic, strong) RNNEventEmitter* eventEmitter;
@property (nonatomic) id<RNNRootViewCreator> creator;

@end
