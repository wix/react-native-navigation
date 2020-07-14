#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNOptions.h"

@interface RNNTransitionStateHolder : RNNOptions

@property (nonatomic) double startAlpha;
@property (nonatomic) double endAlpha;
@property (nonatomic) BOOL interactivePop;
@property (nonatomic) double duration;
@property (nonatomic) double springVelocity;
@property (nonatomic) double springDamping;
@property (nonatomic) double startDelay;
@property (nonatomic, strong) NSString* fromId;
@property (nonatomic, strong) NSString* toId;
@property (nonatomic) double startY;
@property (nonatomic) double endY;
@property (nonatomic) double startX;
@property (nonatomic) double endX;
@property (nonatomic) UIViewAnimationOptions interpolation;

- (CGPoint)startPoint;

- (CGPoint)endPoint;

@end
