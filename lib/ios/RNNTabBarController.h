#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNEventEmitter.h"

@interface RNNTabBarController : UITabBarController <RNNParentProtocol, UITabBarControllerDelegate>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNViewControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;

@end
