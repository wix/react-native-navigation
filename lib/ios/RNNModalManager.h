#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"

@interface RNNModalManager : NSObject

@property (nonatomic, strong) UIViewController<RNNRootViewProtocol>* toVC;

- (instancetype)initWithStore:(RNNStore*)store;
- (void)showModal:(UIViewController*)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock)completion;
- (void)dismissModal:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion;
- (void)dismissAllModals;

@end
