#import "RNNOptions.h"
#import "RNNComponentOptions.h"

@interface RNNBackgroundOptions : RNNOptions

@property (nonatomic, strong) Number* color;
@property (nonatomic, strong) Bool* translucent;
@property (nonatomic, strong) Bool* blur;
@property (nonatomic, strong) Bool* clipToBounds;
@property (nonatomic, strong) RNNComponentOptions* component;

@end
