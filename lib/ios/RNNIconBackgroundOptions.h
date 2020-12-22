#import "RNNOptions.h"

@interface RNNIconBackgroundOptions : RNNOptions <NSCopying>

@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Number *cornerRadius;
@property(nonatomic, strong) Number *width;
@property(nonatomic, strong) Number *height;

- (BOOL)hasValue;

@end
