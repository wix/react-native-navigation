#import "RNNEventEmitter.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface StackControllerDelegate : NSObject <UINavigationControllerDelegate>

- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter;

@end
