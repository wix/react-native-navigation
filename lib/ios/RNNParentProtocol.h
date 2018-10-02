#import "RNNLayoutProtocol.h"
#import "RNNLeafProtocol.h"
#import "RNNParentOptionsResolver.h"

@protocol RNNParentProtocol <RNNLayoutProtocol, UINavigationControllerDelegate, UIViewControllerTransitioningDelegate, UISplitViewControllerDelegate>

@required

@property (nonatomic, strong) RNNParentOptionsResolver* optionsResolver;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options optionsResolver:(RNNParentOptionsResolver *)optionsResolver presenter:(RNNBasePresenter *)presenter;

- (UIViewController<RNNLeafProtocol> *)getLeafViewController;

@end
