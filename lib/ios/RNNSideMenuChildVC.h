#import <UIKit/UIKit.h>
#import "RNNViewControllerPresenter.h"
#import "RNNLayoutProtocol.h"

typedef NS_ENUM(NSInteger, RNNSideMenuChildType) {
	RNNSideMenuChildTypeCenter,
	RNNSideMenuChildTypeLeft,
	RNNSideMenuChildTypeRight,
};


@interface RNNSideMenuChildVC : UIViewController <RNNLayoutProtocol>

@property (readonly) RNNSideMenuChildType type;
@property (readonly) UIViewController<RNNLayoutProtocol> *child;


- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNViewControllerPresenter *)presenter type:(RNNSideMenuChildType)type;

- (void)setWidth:(CGFloat)width;

@end
