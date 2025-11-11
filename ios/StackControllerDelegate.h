#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNEventEmitter.h"
#else
#import "RNNEventEmitter.h"
#endif
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@class RCTHost;

@interface StackControllerDelegate : NSObject <UINavigationControllerDelegate>

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter;
#else
- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter;
#endif

- (BOOL)navigationController:(UINavigationController *)navigationController
               shouldPopItem:(BOOL)shouldPopItem;

@end
