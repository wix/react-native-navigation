#import "RNNOptions.h"

@interface RNNComponentOptions : RNNOptions <NSCopying>

@property(nonatomic, strong) Text *name;
@property(nonatomic, strong) Text *componentId;
@property(nonatomic, strong) Text *alignment;
@property(nonatomic, strong) Bool *waitForRender;

- (BOOL)hasValue;

@end
