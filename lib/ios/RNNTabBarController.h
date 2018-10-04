
#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNEventEmitter.h"

@interface RNNTabBarController : UITabBarController <RNNParentProtocol, UITabBarControllerDelegate>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options optionsResolver:(RNNParentOptionsResolver *)optionsResolver presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNParentOptionsResolver* optionsResolver;

@end
