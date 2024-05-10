#import "RNNScreenTransition.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNPushAnimation : NSObject <UIViewControllerAnimatedTransitioning>

@property(nonatomic, strong) RNNScreenTransition *screenTransition;

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition;

@end
