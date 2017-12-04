#import "RNNViewLocation.h"

@implementation RNNViewLocation
-(instancetype)initWithTransition:(RNNTransitionStateHolder*)transition andVC:(UIViewController*)vc {
	self = [super init];
	self.fromFrame = [self frameFromSuperViewController:[transition.fromElement subviews][0] andVC:vc];
	CGSize fromSize = self.fromFrame.size;
	CGPoint fromCenter = [self centerFromSuperViewController:[transition.fromElement subviews][0] andVC:vc];
	fromCenter.x = fromCenter.x + transition.startX;
	fromCenter.y = fromCenter.y + transition.startY;
	self.fromCenter = fromCenter;
	CGRect toFrame = [self frameFromSuperViewController:[transition.fromElement subviews][0] andVC:vc];
	CGSize toSize = self.fromFrame.size;
	CGPoint toCenter = [self centerFromSuperViewController:[transition.fromElement subviews][0] andVC:vc];
	if (transition.toElement) {
	   toFrame = [self frameFromSuperViewController:[transition.toElement subviews][0] andVC:vc];
		toSize = toFrame.size;
		toCenter = [self centerFromSuperViewController:[transition.toElement subviews][0] andVC:vc];
	}
	toCenter.x = toCenter.x + transition.endX;
	toCenter.y = toCenter.y + transition.endY;
	
	CGAffineTransform transform = CGAffineTransformMakeScale(toSize.width/fromSize.width ,toSize.height/fromSize.height);
	CGAffineTransform transformBack = CGAffineTransformMakeScale(fromSize.width/toSize.width ,fromSize.height/toSize.height);
	self.toFrame = toFrame;
	self.fromSize = fromSize;
	self.toSize = toSize;
	self.toCenter = toCenter;
	self.transform = transform;
	self.transformBack = transformBack;
	return self;
}

-(CGRect)frameFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc{
	CGPoint sharedViewFrameOrigin = [view.superview convertPoint:view.frame.origin toView:vc.view];
	CGRect originRect = CGRectMake(sharedViewFrameOrigin.x, sharedViewFrameOrigin.y, view.frame.size.width, view.frame.size.height);
	return originRect;
}

-(CGPoint)centerFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc{
	CGPoint sharedViewFrameOrigin = [view.superview convertPoint:view.frame.origin toView:vc.view];
	CGRect originRect = CGRectMake(sharedViewFrameOrigin.x, sharedViewFrameOrigin.y, view.frame.size.width, view.frame.size.height);
	CGFloat x = originRect.origin.x + view.frame.size.width/2;
	CGFloat y = originRect.origin.y + view.frame.size.height/2;
	CGPoint center = CGPointMake(x, y);
	return center;
}

@end
