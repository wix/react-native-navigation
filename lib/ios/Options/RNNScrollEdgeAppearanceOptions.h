#import "RNNComponentOptions.h"
#import "RNNOptions.h"
#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@interface RNNScrollEdgeAppearanceOptions : RNNOptions

@property(nonatomic, strong) RNNScrollEdgeAppearanceBackgroundOptions *background;
@property(nonatomic, strong) Bool *active;
@property(nonatomic, strong) Bool *noBorder;
@property(nonatomic, strong) Color *borderColor;

@end
