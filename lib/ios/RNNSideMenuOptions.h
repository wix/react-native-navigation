#import "RNNOptions.h"
#import "RNNSideMenuSideOptions.h"
#import "RNNSideMenuOpenMode.h"

@interface RNNSideMenuOptions : RNNOptions

@property(nonatomic, strong) RNNSideMenuSideOptions *left;
@property(nonatomic, strong) RNNSideMenuSideOptions *right;

@property(nonatomic, strong) RNNText *animationType;
@property(nonatomic, strong) RNNSideMenuOpenMode *openGestureMode;

@end
