#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"

@interface RNNNavigationController : UINavigationController <RNNRootViewProtocol>

@property (nonatomic, strong) NSString* componentId;

@end
