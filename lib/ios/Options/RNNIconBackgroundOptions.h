#import "RNNOptions.h"

@interface RNNIconBackgroundOptions : RNNOptions <NSCopying>

- (instancetype)initWithDict:(NSDictionary *)dict enabled:(Bool *)enabled;

- (BOOL)hasValue;

- (void)setEnabled:(Bool *)enabled;

@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Color *disabledColor;
@property(nonatomic, strong) Number *cornerRadius;
@property(nonatomic, strong) Number *width;
@property(nonatomic, strong) Number *height;

@end
