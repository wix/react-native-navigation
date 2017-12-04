#import "RNNAnimatedView.h"


@implementation RNNAnimatedView

-(instancetype)initWithTransition:(RNNTransitionStateHolder*)transition andLocation:(RNNViewLocation*)location andIsBackButton:(BOOL)backButton {
	UIView* animationView = nil;
	if (backButton) {
		if ([[transition.fromElement subviews][0] isKindOfClass:[UIImageView class]]) {
			UIImage* image = [[transition.fromElement subviews][0] image];
			animationView = [[VICMAImageView alloc] initWithImage:image];
			animationView.contentMode = UIViewContentModeScaleAspectFill;
			if (transition.toElement.resizeMode){
				animationView.contentMode = [RNNAnimatedView contentModefromString:transition.toElement.resizeMode];
			}
		} else {
			if (transition.toElement) {
				animationView = [[transition.toElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			} else {
				animationView = [[transition.fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			}
		}
		animationView.frame = CGRectMake(0, 0, location.toSize.width, location.toSize.height);
		animationView.center = location.toCenter;
		animationView.alpha = transition.endAlpha;
	} else {
		if ([[transition.fromElement subviews][0] isKindOfClass:[UIImageView class]]) {
			UIImage* image = [[transition.fromElement subviews][0] image];
			animationView = [[VICMAImageView alloc] initWithImage:image];
			animationView.contentMode = UIViewContentModeScaleAspectFill;
			if (transition.fromElement.resizeMode){
				animationView.contentMode = [RNNAnimatedView contentModefromString:transition.fromElement.resizeMode];
			}
		} else {
			if (transition.isFromVC) {
				animationView = [[transition.fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			} else {
				animationView = [[transition.fromElement subviews][0] snapshotViewAfterScreenUpdates:YES];
			}
		}
		animationView.frame = CGRectMake(0, 0, location.fromSize.width, location.fromSize.height);
		animationView.center = location.fromCenter;
		animationView.alpha = transition.startAlpha;
	}
	return (RNNAnimatedView*)animationView;
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
