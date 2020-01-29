#import "RNNReactComponentRegistry.h"

@interface RNNReactComponentRegistry () {
	id<RNNComponentViewCreator> _creator;
	NSMapTable* _componentStore;
}

@end

@implementation RNNReactComponentRegistry

- (instancetype)initWithCreator:(id<RNNComponentViewCreator>)creator {
	self = [super init];
	_creator = creator;
	_componentStore = [NSMapTable new];
	return self;
}

- (RNNReactButtonView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId componentType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    RNNReactView* reactView = [self findComponent:component.componentId.get parentComponentId:parentComponentId];
    if (!reactView) {
        reactView = [_creator createRootView:component.name.get rootViewId:component.componentId.get ofType:componentType reactViewReadyBlock:reactViewReadyBlock];
        [self storeComponent:reactView componentId:component.componentId.get parentComponentId:parentComponentId];
    } else if (reactViewReadyBlock) {
        reactViewReadyBlock();
    }
    
    return (RNNReactButtonView *)reactView;
}

- (RNNReactView *)findComponent:(NSString *)componentId parentComponentId:(NSString *)parentComponentId {
    NSMapTable* parentComponentDict = [self componentsForParentId:parentComponentId];
    return [parentComponentDict objectForKey:componentId];
}

- (void)storeComponent:(RNNReactView *)component componentId:(NSString *)componentId parentComponentId:(NSString *)parentComponentId {
    NSMapTable* parentComponentDict = [self componentsForParentId:parentComponentId];
    [parentComponentDict setObject:component forKey:componentId];
}

- (NSMapTable *)componentsForParentId:(NSString *)parentComponentId {
	if (![_componentStore objectForKey:parentComponentId]) {
		[_componentStore setObject:[NSMapTable weakToStrongObjectsMapTable] forKey:parentComponentId];;
	}
	
	return [_componentStore objectForKey:parentComponentId];;
}

- (void)clearComponentsForParentId:(NSString *)parentComponentId {
	[_componentStore removeObjectForKey:parentComponentId];;
}

- (void)removeComponent:(NSString *)componentId {
	if ([_componentStore objectForKey:componentId]) {
		[_componentStore removeObjectForKey:componentId];
	}
}

- (void)removeChildComponent:(NSString *)childId {
	NSMapTable* parent;
	NSEnumerator *enumerator = _componentStore.objectEnumerator;
	while ((parent = enumerator.nextObject)) {
		if ([parent objectForKey:childId]) {
			[parent removeObjectForKey:childId];
			return;
		}
	}
}

- (void)clear {
	[_componentStore removeAllObjects];
}


@end
