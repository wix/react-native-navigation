#import "RNNRootViewCreator.h"

@protocol RNNLeafProtocol <NSObject>

- (void)bindViewController:(UIViewController *)viewController;

- (BOOL)isCustomTransitioned;

- (id<RNNRootViewCreator>)creator;

@end
