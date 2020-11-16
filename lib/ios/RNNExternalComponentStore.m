#import "RNNExternalComponentStore.h"

@interface RNNExternalComponentStore ()

@end

@implementation RNNExternalComponentStore {
    NSMutableDictionary *_externalComponentCreators;
}

- (instancetype)init {
    self = [super init];
    _externalComponentCreators = [NSMutableDictionary new];
    return self;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
    [_externalComponentCreators setObject:[callback copy] forKey:name];
}

- (UIViewController *)getExternalComponent:(RNNLayoutInfo *)layoutInfo bridge:(RCTBridge *)bridge {
    RNNExternalViewCreator creator = [_externalComponentCreators objectForKey:layoutInfo.name];
    return creator(layoutInfo.props, bridge);
}

@end
