#import "UIViewController+RNNOptions.h"

@implementation UIViewController (RNNOptions)

- (void)rnn_setBackgroundImage:(UIImage *)backgroundImage {
	UIImageView* backgroundImageView = (self.view.subviews.count > 0) ? self.view.subviews[0] : nil;
	if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
		backgroundImageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
		[self.view insertSubview:backgroundImageView atIndex:0];
	}
	
	backgroundImageView.layer.masksToBounds = YES;
	backgroundImageView.image = backgroundImage;
	[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
}

- (void)rnn_setModalPresentationStyle:(NSString *)modalPresentationStyle {
	self.modalPresentationStyle = [RCTConvert UIModalPresentationStyle:modalPresentationStyle];
	[self.view setBackgroundColor:[UIColor clearColor]];
}

- (void)rnn_setModalTransitionStyle:(NSString *)modalTransitionStyle {
	self.modalTransitionStyle = [RCTConvert UIModalTransitionStyle:modalTransitionStyle];
}

@end
