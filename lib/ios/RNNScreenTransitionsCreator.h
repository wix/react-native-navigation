#import <Foundation/Foundation.h>
#import "RNNScreenTransition.h"

@interface RNNScreenTransitionsCreator : NSObject

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView screenTransition:(RNNScreenTransition *)screenTransition reversed:(BOOL)reversed;

@end
