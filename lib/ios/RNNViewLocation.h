#import <Foundation/Foundation.h>
#import "RNNElementView.h"

@interface RNNViewLocation : NSObject

@property (nonatomic) CGRect fromFrame;
@property (nonatomic) CGPoint fromCenter;
@property (nonatomic) CGSize fromSize;
@property (nonatomic) CGRect toFrame;
@property (nonatomic) CGPoint toCenter;
@property (nonatomic) CGSize toSize;
@property (nonatomic) CGAffineTransform transform;
@property (nonatomic) CGAffineTransform transformBack;

-(instancetype)initWithFromElement:(RNNElementView*)fromElement toElement:(RNNElementView*)toElement startPoint:(CGPoint)startPoint endPoint:(CGPoint)endPoint andVC:(UIViewController*)vc;

-(CGRect)frameFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc;

-(CGPoint)centerFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc;

@end
