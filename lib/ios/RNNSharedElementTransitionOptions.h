#import "ElementTransitionOptions.h"
#import <Foundation/Foundation.h>

@interface RNNSharedElementTransitionOptions : ElementTransitionOptions

@property(nonatomic, strong) NSString *fromId;
@property(nonatomic, strong) NSString *toId;
@property(nonatomic, strong) RNNTimeInterval *duration;
@property(nonatomic, strong) RNNTimeInterval *startDelay;
@property(nonatomic, strong) id<RNNInterpolatorProtocol> interpolator;

@end
