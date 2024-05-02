#import "AnimatedReactView.h"
#import "SharedElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface AnimatedViewFactory : NSObject

+ (AnimatedReactView *)createFromElement:(UIView *)element
                               toElement:(UIView *)toElement
                       transitionOptions:(SharedElementTransitionOptions *)transitionOptions;

@end
