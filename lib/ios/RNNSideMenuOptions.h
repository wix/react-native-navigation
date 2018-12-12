#import "RNNOptions.h"
#import "RNNSideMenuSideOptions.h"
#import "SideMenuOpenMode.h"

@interface RNNSideMenuOptions : RNNOptions

@property (nonatomic, strong) RNNSideMenuSideOptions* left;
@property (nonatomic, strong) RNNSideMenuSideOptions* right;

@property (nonatomic, strong) Text* animationType;
@property (nonatomic, strong) SideMenuOpenMode* openGestureMode;

@end
