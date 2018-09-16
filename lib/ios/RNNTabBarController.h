
#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"
#import "RNNEventEmitter.h"

@interface RNNTabBarController : UITabBarController <RNNRootViewProtocol, UITabBarControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter*)eventEmitter;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;

@end
