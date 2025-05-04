#import "ElementTransitionOptions.h"

@implementation ElementTransitionOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.elementId = dict[@"id"];

    return self;
}

@end
