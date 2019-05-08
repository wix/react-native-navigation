#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNScreenTransition.h"

@interface RNNPushAnimation : NSObject <UIViewControllerAnimatedTransitioning>

@property (nonatomic, strong) RNNScreenTransition* screenTransition;

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition;

@end
