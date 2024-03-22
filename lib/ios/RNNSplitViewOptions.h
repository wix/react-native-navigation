#import "RNNOptions.h"

@interface RNNSplitViewOptions : RNNOptions

@property(nonatomic, strong) NSString *displayMode;
@property(nonatomic, strong) NSString *primaryEdge;
@property(nonatomic, strong) RNNNumber *minWidth;
@property(nonatomic, strong) RNNNumber *maxWidth;
@property(nonatomic, strong) NSString *primaryBackgroundStyle;

@end
