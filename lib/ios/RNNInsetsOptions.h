#import "RNNOptions.h"

@interface RNNInsetsOptions : RNNOptions <NSCopying>

@property(nonatomic, strong) RNNDouble *top;
@property(nonatomic, strong) RNNDouble *left;
@property(nonatomic, strong) RNNDouble *right;
@property(nonatomic, strong) RNNDouble *bottom;

+ (RNNInsetsOptions *)withValue:(UIEdgeInsets)insets;

- (UIEdgeInsets)edgeInsetsWithDefault:(UIEdgeInsets)defaultInsets;

- (UIEdgeInsets)UIEdgeInsets;

- (BOOL)hasValue;

@end
