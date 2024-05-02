#import "MMDrawerController.h"
#import "RNNOptions.h"

@interface RNNSideMenuSideOptions : RNNOptions

@property(nonatomic, strong) Bool *visible;
@property(nonatomic, strong) Bool *enabled;
@property(nonatomic, strong) Double *width;
@property(nonatomic, strong) Bool *shouldStretchDrawer;
@property(nonatomic, strong) Double *animationVelocity;

@end
