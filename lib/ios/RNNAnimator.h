#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNElementView.h"

@interface RNNAnimator : NSObject <UIViewControllerAnimatedTransitioning>
-(void)setupTransition:(NSDictionary*)data;

@end
