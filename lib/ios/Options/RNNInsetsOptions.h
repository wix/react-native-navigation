#import "RNNOptions.h"

@interface RNNInsetsOptions : RNNOptions <NSCopying>

@property(nonatomic, strong) Double *top;
@property(nonatomic, strong) Double *left;
@property(nonatomic, strong) Double *right;
@property(nonatomic, strong) Double *bottom;

+ (RNNInsetsOptions *)withValue:(UIEdgeInsets)insets;

- (UIEdgeInsets)edgeInsetsWithDefault:(UIEdgeInsets)defaultInsets;

- (UIEdgeInsets)UIEdgeInsets;

- (BOOL)hasValue;

@end
