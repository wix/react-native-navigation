#import "RNNSplitViewOptions.h"

@implementation RNNSplitViewOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.displayMode = dict[@"displayMode"];
    self.primaryEdge = dict[@"primaryEdge"];
    self.minWidth = [NumberParser parse:dict key:@"minWidth"];
    self.maxWidth = [NumberParser parse:dict key:@"maxWidth"];
    self.primaryBackgroundStyle = dict[@"primaryBackgroundStyle"];
    return self;
}

- (void)mergeOptions:(RNNSplitViewOptions *)options {
    if (options.displayMode)
        self.displayMode = options.displayMode;
    if (options.primaryEdge)
        self.primaryEdge = options.primaryEdge;
    if (options.minWidth.hasValue)
        self.minWidth = options.minWidth;
    if (options.maxWidth.hasValue)
        self.maxWidth = options.maxWidth;
    if (options.primaryBackgroundStyle)
        self.primaryBackgroundStyle = options.primaryBackgroundStyle;
}

@end
