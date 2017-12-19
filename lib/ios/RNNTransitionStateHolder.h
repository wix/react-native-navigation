#import <Foundation/Foundation.h>
#import "RNNElementView.h"
#import "RNNViewLocation.h"
#import "RNNAnimatedView.h"

@class RNNAnimatedView;
@class RNNViewLocation;
@interface RNNTransitionStateHolder : NSObject

@property (nonatomic) double startAlpha;
@property (nonatomic) double endAlpha;
@property (nonatomic) BOOL interactivePop;
@property (nonatomic) double duration;
@property (nonatomic) double springVelocity;
@property (nonatomic) double springDamping;
@property (nonatomic) double startDelay;
@property (nonatomic, strong) RNNElementView* fromElement;
@property (nonatomic, strong) NSString* fromElementType;
@property (nonatomic) UIViewContentMode fromElementResizeMode;
@property (nonatomic, strong) RNNElementView* toElement;
@property (nonatomic, strong) NSString* fromId;
@property (nonatomic, strong) NSString* toId;
@property (nonatomic, strong) RNNAnimatedView* animatedView;
@property (nonatomic) BOOL isSharedElementTransition;
@property (nonatomic, strong) RNNViewLocation* locations;
@property (nonatomic) BOOL isFromVC;
@property (nonatomic) double startY;
@property (nonatomic) double endY;
@property (nonatomic) double startX;
@property (nonatomic) double endX;

-(instancetype)initWithTransition:(NSDictionary*)transition;
@end
