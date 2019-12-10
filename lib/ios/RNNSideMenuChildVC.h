#import <UIKit/UIKit.h>
#import "RNNComponentPresenter.h"
#import "RNNLayoutProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNLayoutProtocol>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo creator:(id<RNNComponentViewCreator>)creator options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter childViewController:(UIViewController *)childViewController type:(RNNSideMenuChildType)type;

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNLayoutProtocol> *child;


- (void)setWidth:(CGFloat)width;

@end
