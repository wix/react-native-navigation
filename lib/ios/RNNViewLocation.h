#import <Foundation/Foundation.h>

@interface RNNViewLocation : NSObject

@property(nonatomic) CGRect fromFrame;
@property(nonatomic) CGRect toFrame;
@property(nonatomic) CGFloat fromAngle;
@property(nonatomic) CGFloat toAngle;
@property(nonatomic) CGFloat fromCornerRadius;
@property(nonatomic) CGFloat toCornerRadius;
@property(nonatomic) NSUInteger index;
@property(nonatomic) CATransform3D fromTransform;
@property(nonatomic) CATransform3D toTransform;

@property(nonatomic) CGRect fromBounds;
@property(nonatomic) CGRect toBounds;
@property(nonatomic) CGPoint fromCenter;
@property(nonatomic) CGPoint toCenter;
@property(nonatomic) CGRect fromPath;
@property(nonatomic) CGRect toPath;

- (instancetype)initWithFromElement:(UIView *)fromElement toElement:(UIView *)toElement;

@end
