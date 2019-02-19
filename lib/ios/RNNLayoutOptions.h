#import "RNNOptions.h"

@interface RNNLayoutOptions : RNNOptions

@property (nonatomic, strong) Color* backgroundColor;
@property (nonatomic, strong) Text* direction;
@property (nonatomic, strong) id orientation;

#if !TARGET_OS_TV
- (UIInterfaceOrientationMask)supportedOrientations;
#endif

@end
