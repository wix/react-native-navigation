#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNNavigationControllerPresenter.h"
#import "UINavigationController+RNNOptions.h"

@interface RNNNavigationController : UINavigationController <RNNParentProtocol>

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNNavigationControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;


- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo creator:(id<RNNRootViewCreator>)creator childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter;

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor;

@end
