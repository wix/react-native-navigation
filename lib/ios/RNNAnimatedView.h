#import <UIKit/UIKit.h>
#import "RNNTransitionStateHolder.h"
#import "RNNViewLocation.h"
#import "VICMAImageView.h"

@class RNNViewLocation;
@class RNNTransitionStateHolder;
@interface RNNAnimatedView : UIView

-(instancetype)initWithTransition:(RNNTransitionStateHolder*)transition andLocation:(RNNViewLocation*)location andIsBackButton:(BOOL)backButton;
+(UIViewContentMode)contentModefromString:(NSString*)resizeMode;
@end
