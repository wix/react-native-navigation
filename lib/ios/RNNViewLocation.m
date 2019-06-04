#import "RNNViewLocation.h"

@implementation RNNViewLocation

- (instancetype)initWithFromElement:(RNNElementView *)fromElement toElement:(RNNElementView *)toElement startPoint:(CGPoint)startPoint endPoint:(CGPoint)endPoint andVC:(UIViewController *)vc {
	self = [super init];
	
	UIView *fromView = [fromElement subviews][0];
	UIView *toView = [toElement subviews][0];
	UIWindow *window = fromView.window;
	
	self.fromFrame = [fromView convertRect:fromView.bounds toView:window];
	CGSize fromSize = self.fromFrame.size;
	CGPoint fromCenter = CGPointMake(CGRectGetMidX(self.fromFrame), CGRectGetMidY(self.fromFrame));
	fromCenter.x = fromCenter.x + startPoint.x;
	fromCenter.y = fromCenter.y + startPoint.y;
	self.fromCenter = fromCenter;
	
	CGRect toFrame = self.fromFrame;
	CGSize toSize = self.fromFrame.size;
	CGPoint toCenter = CGPointMake(CGRectGetMidX(self.fromFrame), CGRectGetMidY(self.fromFrame));
	if (toElement) {
		toFrame = [toView convertRect:toView.bounds toView:window];
		toSize = toFrame.size;
		toCenter = CGPointMake(CGRectGetMidX(toFrame), CGRectGetMidY(toFrame));
	}
	toCenter.x = toCenter.x + endPoint.x;
	toCenter.y = toCenter.y + endPoint.y;
	
	CGAffineTransform transform = CGAffineTransformMakeScale(toSize.width/fromSize.width, toSize.height/fromSize.height);
	CGAffineTransform transformBack = CGAffineTransformMakeScale(fromSize.width/toSize.width, fromSize.height/toSize.height);
	
	self.toFrame = toFrame;
	self.fromSize = fromSize;
	self.toSize = toSize;
	self.toCenter = toCenter;
	self.transform = transform;
	self.transformBack = transformBack;
	
	return self;
}

@end
