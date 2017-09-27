#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNSharedElementView.h"

@interface RNNAnimationController : NSObject <UIViewControllerAnimatedTransitioning>
+(UIViewContentMode)contentModefromString:(NSString*)resizeMode;
-(void)setupTransition:(NSDictionary*)data;
-(NSArray*)findRNNSharedElementViews:(UIView*)view;
-(RNNSharedElementView*)findViewToShare:(NSArray*)RNNSharedElementViews withId:(NSString*)elementId;

@end
