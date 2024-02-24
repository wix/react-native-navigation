#import "RNNOptions.h"

@interface RNNIconBackgroundOptions : RNNOptions <NSCopying>

- (instancetype)initWithDict:(NSDictionary *)dict enabled:(Bool *)enabled;

- (BOOL)hasValue;

- (void)setEnabled:(Bool *)enabled;

@property(nonatomic, strong) Color *color;
@property(nonatomic, strong) Color *disabledColor;
@property(nonatomic, strong) RNNNumber *cornerRadius;
@property(nonatomic, strong) RNNNumber *width;
@property(nonatomic, strong) RNNNumber *height;

@end
