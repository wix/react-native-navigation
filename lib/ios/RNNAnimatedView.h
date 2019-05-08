#import <UIKit/UIKit.h>
#import "RNNViewLocation.h"
#import "VICMAImageView.h"


@interface RNNAnimatedView : UIView

-(instancetype)initFromElement:(RNNElementView*)fromElement toElement:(RNNElementView*)toElement andLocation:(RNNViewLocation*)location andIsBackButton:(BOOL)backButton startAlpha:(CGFloat)startAlpha endAlpha:(CGFloat)endAlpha;
+(UIViewContentMode)contentModefromString:(NSString*)resizeMode;

@end
