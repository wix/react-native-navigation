#import "RNNOptions.h"
#import "RNNButtonOptions.h"

@interface RNNToolbarOptions : RNNOptions

@property (nonatomic, strong) NSArray* buttons;
@property (nonatomic, strong) RNNButtonOptions* buttonStyle;

@end