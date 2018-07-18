#import "RNNOptions.h"

@interface RNNLayoutOptions : RNNOptions

@property (nonatomic, strong) NSNumber* backgroundColor;
@property (nonatomic, strong) id orientation;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
