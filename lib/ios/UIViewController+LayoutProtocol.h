#import <UIKit/UIKit.h>
#import "RNNEventEmitter.h"
#import "RNNLayoutProtocol.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface UIViewController (LayoutProtocol) <RNNLayoutProtocol>

- (void)render;

- (UIViewController *)getCurrentChild;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (void)mergeChildOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)resolveOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)overrideOptions:(RNNNavigationOptions *)options;

- (void)readyForPresentation;

@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;
@property (nonatomic, strong) RNNEventEmitter* eventEmitter;
@property (nonatomic) id<RNNComponentViewCreator> creator;
@property (nonatomic) RNNReactViewReadyCompletionBlock _Nonnull reactViewReadyCallback;
@property (nonatomic) BOOL waitForRender;

@end
