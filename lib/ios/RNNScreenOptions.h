#import "RNNOptions.h"

extern const NSInteger BLUR_STATUS_TAG;

@interface RNNScreenOptions : RNNOptions

@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSNumber* screenBackgroundColor;
@property (nonatomic, strong) NSString* backButtonTransition;
@property (nonatomic, strong) id orientation;
@property (nonatomic, strong) NSNumber* statusBarBlur;
@property (nonatomic, strong) NSNumber* statusBarHideWithTopBar;
@property (nonatomic, strong) NSNumber* popGesture;
@property (nonatomic, strong) UIImage* backgroundImage;
@property (nonatomic, strong) UIImage* rootBackgroundImage;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
