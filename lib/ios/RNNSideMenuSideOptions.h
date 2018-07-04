#import "RNNOptions.h"
#import "MMDrawerController.h"

@interface RNNSideMenuSideOptions : RNNOptions

- (void)applyOnSide:(MMDrawerSide)side viewController:(UIViewController *)viewController;

@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSNumber* enabled;
@property (nonatomic, strong) NSNumber* width;

@end
