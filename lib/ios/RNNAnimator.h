#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNElementView.h"

@interface RNNAnimator : NSObject <UIViewControllerAnimatedTransitioning>

-(instancetype)initWithAnimationsDictionary:(NSDictionary *)animationsDic;
-(void)setupTransition:(NSDictionary*)data;

@end
