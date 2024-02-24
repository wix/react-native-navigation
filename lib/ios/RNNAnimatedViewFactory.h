#import "AnimatedReactView.h"
#import "RNNSharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNAnimatedViewFactory : NSObject

+ (AnimatedReactView *)createFromElement:(UIView *)element
                               toElement:(UIView *)toElement
                       transitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions;

@end
