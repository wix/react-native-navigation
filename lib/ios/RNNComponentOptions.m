#import "RNNComponentOptions.h"

@implementation RNNComponentOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.name = [TextParser parse:dict key:@"name"];
    self.componentId = [TextParser parse:dict key:@"componentId"];
    self.alignment = [TextParser parse:dict key:@"alignment"];
    self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];

    return self;
}

- (id)copyWithZone:(NSZone *)zone {
    RNNComponentOptions *newOptions = RNNComponentOptions.new;
    newOptions.name = self.name.copy;
    newOptions.componentId = self.componentId.copy;
    newOptions.alignment = self.alignment.copy;
    newOptions.waitForRender = self.waitForRender.copy;
    return newOptions;
}

- (void)mergeOptions:(RNNComponentOptions *)options {
    if (options.name.hasValue)
        self.name = options.name;
    if (options.componentId.hasValue)
        self.componentId = options.componentId;
    if (options.alignment.hasValue)
        self.alignment = options.alignment;
    if (options.waitForRender.hasValue)
        self.waitForRender = options.waitForRender;
}

- (BOOL)hasValue {
    return _name.hasValue;
}

@end
