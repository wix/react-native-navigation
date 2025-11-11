#import "RNNEventEmitter.h"
#import <Foundation/Foundation.h>

@interface RNNModalManagerEventHandler : NSObject

- (instancetype _Nonnull)initWithEventEmitter:(RNNEventEmitter *_Nonnull)eventEmitter;

- (void)dismissedModal:(UIViewController *_Nonnull)viewController;
- (void)attemptedToDismissModal:(UIViewController *_Nonnull)viewController;
- (void)dismissedMultipleModals:(NSArray *_Nonnull)viewControllers;

@end
