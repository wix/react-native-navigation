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

- (RNNReactView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	NSMutableDictionary* parentComponentDict = [self componentsForParentId:parentComponentId];
	
	RNNReactView* reactView = parentComponentDict[component.componentId.get];
	if (!reactView) {
		reactView = (RNNReactView *)[_creator createRootViewFromComponentOptions:component reactViewReadyBlock:reactViewReadyBlock];
		parentComponentDict[component.componentId.get] = reactView;
	} else if (reactViewReadyBlock) {
		reactViewReadyBlock();
	}
	
	return reactView;
}

- (NSMutableDictionary *)componentsForParentId:(NSString *)parentComponentId {
	if (![_componentStore objectForKey:parentComponentId]) {
		[_componentStore setObject:[NSMutableDictionary new] forKey:parentComponentId];;
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
	NSMutableDictionary* parent;
	NSEnumerator *enumerator = _componentStore.objectEnumerator;
	while ((parent = enumerator.nextObject)) {
		if (parent[childId]) {
			[parent removeObjectForKey:childId];
			return;
		}
	}
}

- (void)clear {
	[_componentStore removeAllObjects];
}


@end
