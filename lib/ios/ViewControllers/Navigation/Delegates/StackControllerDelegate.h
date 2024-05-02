#import "RNNEventEmitter.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@class RCTHost;

@interface StackControllerDelegate : NSObject <UINavigationControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter;

- (BOOL)navigationController:(UINavigationController *)navigationController
               shouldPopItem:(BOOL)shouldPopItem;

@end
