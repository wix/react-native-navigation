#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"

@interface RNNNavigationController : UINavigationController <RNNRootViewProtocol>

- (instancetype)initWithParentInfo:(RNNParentInfo *)parentInfo;

@property (nonatomic, retain) RNNParentInfo* parentInfo;


@end
