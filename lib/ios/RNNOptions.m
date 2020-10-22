#import "RNNOptions.h"
#import "NSObject+Utils.h"

@implementation RNNOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    return self;
}

- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override {
    for (id prop in self.classProperties) {
        id value = [otherOptions valueForKey:prop];
        if ([value isKindOfClass:[RNNOptions class]]) {
            [[self valueForKey:prop] mergeOptions:value overrideOptions:override];
        } else if ([value isKindOfClass:[Param class]]) {
            if ((((Param *)value).hasValue) &&
                (override || !((Param *)[self valueForKey:prop]).hasValue)) {
                [self setValue:value forKey:prop];
            }
        } else if (value && (override || ![self valueForKey:prop])) {
            [self setValue:value forKey:prop];
        }
    }

    return self;
}

- (RNNOptions *)overrideOptions:(RNNOptions *)otherOptions {
    return [self mergeOptions:otherOptions overrideOptions:YES];
}

- (RNNOptions *)mergeOptions:(RNNOptions *)otherOptions {
    return [self mergeOptions:otherOptions overrideOptions:NO];
}

- (RNNOptions *)mergeInOptions:(RNNOptions *)otherOptions {
    if (!otherOptions) {
        return self;
    }

    return [otherOptions mergeOptions:self overrideOptions:NO];
}

- (RNNOptions *)withDefault:(RNNOptions *)defaultOptions {
    RNNOptions *newOptions = [[[self class] alloc] initWithDict:@{}];
    [newOptions mergeOptions:defaultOptions overrideOptions:YES];
    [newOptions mergeOptions:self overrideOptions:YES];

    return newOptions;
}

@end
