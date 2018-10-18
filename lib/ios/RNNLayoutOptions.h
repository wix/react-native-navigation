#import "RNNOptions.h"

@interface RNNLayoutOptions : RNNOptions

@property (nonatomic, strong) Color* backgroundColor;
@property (nonatomic, strong) id orientation;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
