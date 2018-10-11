#import "RNNOptions.h"

@interface RNNLayoutOptions : RNNOptions

@property (nonatomic, strong) Number* backgroundColor;
@property (nonatomic, strong) id orientation;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
