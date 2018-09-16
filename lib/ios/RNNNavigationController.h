#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"

@interface RNNNavigationController : UINavigationController <RNNParentProtocol>

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;


@end
