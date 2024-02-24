#import "RNNOptions.h"

@interface RNNIconBackgroundOptions : RNNOptions <NSCopying>

- (instancetype)initWithDict:(NSDictionary *)dict enabled:(RNNBool *)enabled;

- (BOOL)hasValue;

- (void)setEnabled:(RNNBool *)enabled;

@property(nonatomic, strong) RNNColor *color;
@property(nonatomic, strong) RNNColor *disabledColor;
@property(nonatomic, strong) RNNNumber *cornerRadius;
@property(nonatomic, strong) RNNNumber *width;
@property(nonatomic, strong) RNNNumber *height;

@end
