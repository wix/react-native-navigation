#import "RNNOptions.h"
#import "RNNComponentOptions.h"

@interface RNNBackgroundOptions : RNNOptions

@property (nonatomic, strong) NSNumber* color;
@property (nonatomic, strong) NSNumber* translucent;
@property (nonatomic, strong) NSNumber* blur;
@property (nonatomic, strong) RNNComponentOptions* component;
@property (nonatomic, strong) NSNumber* clipToBounds; 

@end
