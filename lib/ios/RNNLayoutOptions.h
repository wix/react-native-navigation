#import "RNNOptions.h"

@interface RNNLayoutOptions : RNNOptions

@property (nonatomic, strong) NSNumber* screenBackgroundColor;
@property (nonatomic, strong) id orientation;

- (UIInterfaceOrientationMask)supportedOrientations;

@end
