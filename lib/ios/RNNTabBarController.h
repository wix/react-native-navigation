
#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "RNNEventEmitter.h"

@interface RNNTabBarController : UITabBarController <RNNParentProtocol, UITabBarControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter*)eventEmitter;

- (void)setSelectedIndexByComponentID:(NSString *)componentID;

@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;

@end
