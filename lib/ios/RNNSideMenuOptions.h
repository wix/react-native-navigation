#import "RNNOptions.h"
#import "RNNSideMenuSideOptions.h"

@interface RNNSideMenuOptions : RNNOptions

@property (nonatomic, strong) RNNSideMenuSideOptions* left;
@property (nonatomic, strong) RNNSideMenuSideOptions* right;

@end
