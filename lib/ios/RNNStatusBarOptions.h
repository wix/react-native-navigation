#import "RNNOptions.h"

extern const NSInteger BLUR_STATUS_TAG;

@interface RNNStatusBarOptions : RNNOptions

@property (nonatomic, strong) NSNumber* blur;
@property (nonatomic, strong) NSNumber* hideWithTopBar;
@property (nonatomic, strong) NSString* style;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSNumber* animate;

@end
