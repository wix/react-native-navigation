#import "RNNAnimatedReactView.h"
#import "RNNSharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNAnimatedViewFactory : NSObject

+ (RNNAnimatedReactView *)createFromElement:(UIView *)element
                               toElement:(UIView *)toElement
                       transitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions;

@end
