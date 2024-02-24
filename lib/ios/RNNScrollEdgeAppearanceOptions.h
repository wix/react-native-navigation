#import "RNNComponentOptions.h"
#import "RNNOptions.h"
#import "RNNScrollEdgeAppearanceBackgroundOptions.h"

@interface RNNScrollEdgeAppearanceOptions : RNNOptions

@property(nonatomic, strong) RNNScrollEdgeAppearanceBackgroundOptions *background;
@property(nonatomic, strong) RNNBool *active;
@property(nonatomic, strong) RNNBool *noBorder;
@property(nonatomic, strong) RNNColor *borderColor;

@end
