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

- (instancetype)initWithFromElement:(UIView *)fromElement toElement:(UIView *)toElement;

@end
