#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNEventEmitter.h"

@interface StackControllerDelegate : NSObject <UINavigationControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter;

@end
