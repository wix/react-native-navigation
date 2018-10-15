#import "RNNOptions.h"

extern const NSInteger BLUR_STATUS_TAG;

@interface RNNStatusBarOptions : RNNOptions

@property (nonatomic, strong) Bool* blur;
@property (nonatomic, strong) Bool* hideWithTopBar;
@property (nonatomic, strong) Text* style;
@property (nonatomic, strong) Bool* visible;
@property (nonatomic, strong) Bool* animate;

@end
