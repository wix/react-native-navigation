#import <Foundation/Foundation.h>
#import "RNNElementView.h"
#import "RNNTransitionStateHolder.h"

@interface RNNElementFinder : NSObject

@property (nonatomic, strong) NSArray* toVCTransitionElements;
@property (nonatomic, strong) NSArray* fromVCTransitionElements;

-(instancetype)initWithToVC:(UIViewController*)toVC andfromVC:(UIViewController*)fromVC;
-(NSArray*)findRNNElementViews:(UIView*)view;
-(RNNElementView*)findViewToAnimate:(NSArray*)RNNTransitionElementViews withId:(NSString*)elementId;
-(void)findElementsInTransition:(RNNTransitionStateHolder*)transitionStateHolder;
@end
