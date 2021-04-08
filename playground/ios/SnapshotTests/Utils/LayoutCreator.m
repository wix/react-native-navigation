#import "LayoutCreator.h"

@implementation LayoutCreator

+ (NSDictionary *)component:(NSString *)componentId options:(NSDictionary *)options {
    return @{@"type" : @"Component", @"id" : componentId, @"data" : @{@"options" : options}};
}

+ (NSDictionary *)parentWithID:(NSString *)componentId
                          type:(NSString *)type
                       options:(NSDictionary *)options
                      children:(NSArray<NSDictionary *> *)children {
    return @{
        @"type" : type,
        @"id" : componentId,
        @"data" : @{@"options" : options},
        @"children" : children
    };
}

+ (NSDictionary *)stack:(NSDictionary *)options children:(NSArray<NSDictionary *> *)children {
    return @{
        @"type" : @"Stack",
        @"id" : @"StackID",
        @"data" : @{@"options" : options},
        @"children" : children
    };
}

@end
