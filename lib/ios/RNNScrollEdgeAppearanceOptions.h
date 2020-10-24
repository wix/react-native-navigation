#import "RNNOptions.h"
#import "RNNComponentOptions.h"
#import "RNNScrollEdgeAppearanceBackgroundOptions.h"


@interface RNNScrollEdgeAppearanceOptions : RNNOptions

@property (nonatomic, strong) RNNScrollEdgeAppearanceBackgroundOptions* background;
@property (nonatomic, strong) Bool* active;

@end
