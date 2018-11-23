#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNEventEmitter.h"
#import "RNNTabBarPresenter.h"

@interface RNNTabBarController : UITabBarController <RNNParentProtocol, UITabBarControllerDelegate>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNTabBarPresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNTabBarPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;

@end
