#import "RNNOptions.h"

@interface RNNComponentOptions : RNNOptions <NSCopying>

@property(nonatomic, strong) RNNText *name;
@property(nonatomic, strong) RNNText *componentId;
@property(nonatomic, strong) RNNText *alignment;
@property(nonatomic, strong) RNNBool *waitForRender;

- (BOOL)hasValue;

@end
