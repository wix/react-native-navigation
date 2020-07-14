#import <Foundation/Foundation.h>
#import "AnimatedReactView.h"
#import "SharedElementTransitionOptions.h"

@interface AnimatedViewFactory : NSObject

+ (AnimatedReactView *)createFromElement:(UIView *)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions;

@end
