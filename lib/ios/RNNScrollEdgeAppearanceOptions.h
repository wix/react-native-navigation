#import "RNNComponentOptions.h"
#import "RNNOptions.h"
#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@interface RNNScrollEdgeAppearanceOptions : RNNOptions

@property(nonatomic, strong) RNNScrollEdgeAppearanceBackgroundOptions *background;
@property(nonatomic, strong) Bool *active;

@end
