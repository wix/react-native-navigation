#import "MMDrawerController.h"
#import "RNNOptions.h"

@interface RNNSideMenuSideOptions : RNNOptions

@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *enabled;
@property(nonatomic, strong) RNNDouble *width;
@property(nonatomic, strong) RNNBool *shouldStretchDrawer;
@property(nonatomic, strong) RNNDouble *animationVelocity;

@end
