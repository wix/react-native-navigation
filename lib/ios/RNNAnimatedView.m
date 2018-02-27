#import "RNNAnimatedView.h"
#import "RNNElementView.h"

@implementation RNNAnimatedView

-(instancetype)initFromElement:(RNNElementView*)fromElement toElement:(RNNElementView*)toElement andLocation:(RNNViewLocation*)location andIsBackButton:(BOOL)backButton startAlpha:(CGFloat)startAlpha endAlpha:(CGFloat)endAlpha {
	UIView* animatedView = nil;
	if (backButton) {
		if ([self elementIsImage:fromElement]) {
			animatedView = [self createImageAnimatedView:animatedView fromElement:fromElement toElement:toElement];
		} else {
			if (toElement) {
				animatedView = [[toElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			} else {
				animatedView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			}
		}
		[self assignStyle:animatedView withSize:location.toSize center:location.toCenter andAlpha:endAlpha];
	} else {
		if ([self elementIsImage:fromElement]) {
			animatedView = [self createImageAnimatedView:animatedView fromElement:fromElement toElement:fromElement];
		} else {
			animatedView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:YES];
		}
		[self assignStyle:animatedView withSize:location.fromSize center:location.fromCenter andAlpha:startAlpha];
	}
	return (RNNAnimatedView*)animatedView;
}

-(BOOL)elementIsImage:(RNNElementView*)element {
	return [[element subviews][0] isKindOfClass:[UIImageView class]];
}

-(UIView*)createImageAnimatedView:(UIView*)animatedView fromElement:(RNNElementView*)fromElement toElement:(RNNElementView*)toElement {
	UIImage* image = [[fromElement subviews][0] image];
	animatedView = [[VICMAImageView alloc] initWithImage:image];
	animatedView.contentMode = UIViewContentModeScaleAspectFill;
	if (toElement.resizeMode){
		animatedView.contentMode = [RNNAnimatedView contentModefromString:toElement.resizeMode];
	}
	return animatedView;
}

-(void)assignStyle:(UIView*)animatedView withSize:(CGSize)size center:(CGPoint)center andAlpha:(double)alpha {
	animatedView.frame = CGRectMake(0, 0, size.width, size.height);
	animatedView.center = center;
	animatedView.alpha = alpha;
}

+(UIViewContentMode)contentModefromString:(NSString*)resizeMode{
	if ([resizeMode isEqualToString:@"cover"]) {
		return UIViewContentModeScaleAspectFill;
	} else if ([resizeMode isEqualToString:@"contain"]) {
		return UIViewContentModeScaleAspectFit;
	} else if ([resizeMode isEqualToString:@"stretch"]) {
		return UIViewContentModeScaleToFill;
	} else {
		return 0;
	}
}
@end
