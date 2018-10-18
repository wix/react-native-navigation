#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNParentProtocol>

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNParentProtocol> *child;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNViewControllerPresenter* presenter;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options presenter:(RNNViewControllerPresenter *)presenter type:(RNNSideMenuChildType)type;

@end
