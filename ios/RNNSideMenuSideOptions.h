#import "MMDrawerController.h"
#import "RNNOptions.h"

@interface RNNSideMenuSideOptions : RNNOptions

@property(nonatomic, strong) Bool *visible;
@property(nonatomic, strong) Bool *enabled;
@property(nonatomic, strong) Double *width;
@property(nonatomic, strong) Bool *shouldStretchDrawer;
@property(nonatomic, strong) Double *animationVelocity;
@property(nonatomic, strong) Text *openMode;

/**
 * Converts a string open mode to the equivalent MMDrawerOpenMode enum value
 */
MMDrawerOpenMode MMDrawerOpenModeFromString(NSString *openModeString);

@end
