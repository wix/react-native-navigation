
#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"
#import "RNNEventEmitter.h"

@interface RNNTabBarController : UITabBarController <RNNRootViewProtocol, UITabBarControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter*)eventEmitter;

- (void)setTabBarHidden:(BOOL)hidden animated:(BOOL)animated;
- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@end
