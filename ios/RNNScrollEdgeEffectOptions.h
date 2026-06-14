#import "RNNOptions.h"

@interface RNNScrollEdgeOptions : RNNOptions

@property(nonatomic, strong) Bool *hidden;
@property(nonatomic, strong) Text *style;

@end

@interface RNNScrollEdgeEffectOptions : RNNOptions

@property(nonatomic, strong) Bool *hidden;
@property(nonatomic, strong) Text *style;

@property(nonatomic, strong) RNNScrollEdgeOptions *top;
@property(nonatomic, strong) RNNScrollEdgeOptions *bottom;
@property(nonatomic, strong) RNNScrollEdgeOptions *left;
@property(nonatomic, strong) RNNScrollEdgeOptions *right;

@end
