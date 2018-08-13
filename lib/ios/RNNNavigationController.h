#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"

@interface RNNNavigationController : UINavigationController <RNNRootViewProtocol>

- (instancetype)initWithOptions:(RNNNavigationOptions *)options;

@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) RNNNavigationOptions* options;

@end
