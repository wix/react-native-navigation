#import "RNNInsetsOptions.h"
#import "RNNOptions.h"
@interface RNNLayoutOptions : RNNOptions

@property(nonatomic, strong) RNNColor *backgroundColor;
@property(nonatomic, strong) RNNColor *componentBackgroundColor;
@property(nonatomic, strong) RNNText *direction;
@property(nonatomic, strong) id orientation;
@property(nonatomic, strong) RNNBool *autoHideHomeIndicator;
@property(nonatomic, strong) RNNInsetsOptions *insets;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
