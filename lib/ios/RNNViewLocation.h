#import <Foundation/Foundation.h>

@interface RNNViewLocation : NSObject

@property (nonatomic) CGRect fromFrame;
@property (nonatomic) CGRect toFrame;
@property (nonatomic) CGFloat fromAngle;
@property (nonatomic) CGFloat toAngle;

- (instancetype)initWithFromElement:(UIView*)fromElement toElement:(UIView*)toElement;

@end
