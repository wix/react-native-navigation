#import "RNNInsetsOptions.h"
#import "RNNOptions.h"
@interface RNNLayoutOptions : RNNOptions

@property(nonatomic, strong) Color *backgroundColor;
@property(nonatomic, strong) Color *componentBackgroundColor;
@property(nonatomic, strong) Text *direction;
@property(nonatomic, strong) id orientation;
@property(nonatomic, strong) Bool *autoHideHomeIndicator;
@property(nonatomic, strong) RNNInsetsOptions *insets;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
