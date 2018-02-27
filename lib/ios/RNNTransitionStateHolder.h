#import <Foundation/Foundation.h>

@interface RNNTransitionStateHolder : NSObject

@property (nonatomic) double startAlpha;
@property (nonatomic) double endAlpha;
@property (nonatomic) BOOL interactivePop;
@property (nonatomic) double duration;
@property (nonatomic) double springVelocity;
@property (nonatomic) double springDamping;
@property (nonatomic) double startDelay;
@property (nonatomic, strong) NSString* fromElementType;
@property (nonatomic, strong) NSString* fromId;
@property (nonatomic, strong) NSString* toId;
@property (nonatomic) BOOL isSharedElementTransition;
@property (nonatomic) double startY;
@property (nonatomic) double endY;
@property (nonatomic) double startX;
@property (nonatomic) double endX;

- (instancetype)initWithTransition:(NSDictionary*)transition;

@end
