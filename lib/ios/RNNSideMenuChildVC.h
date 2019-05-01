#import <UIKit/UIKit.h>
#import "RNNViewControllerPresenter.h"
#import "RNNLayoutProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNLayoutProtocol>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo creator:(id<RNNRootViewCreator>)creator options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter childViewController:(UIViewController *)childViewController type:(RNNSideMenuChildType)type;

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNLayoutProtocol> *child;


- (void)setWidth:(CGFloat)width;

@end
