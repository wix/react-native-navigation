#import "RNNOptions.h"

extern const NSInteger BLUR_STATUS_TAG;

@interface RNNStatusBarOptions : RNNOptions

@property(nonatomic, strong) RNNBool *blur;
@property(nonatomic, strong) RNNBool *hideWithTopBar;
@property(nonatomic, strong) RNNText *style;
@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *animate;

@end
