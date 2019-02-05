#import "RNNReactComponentManager.h"

@interface RNNReactComponentManager () {
	id<RNNRootViewCreator> _creator;
	NSMutableDictionary* _componentStore;
}

@end

@implementation RNNReactComponentManager

- (instancetype)initWithCreator:(id<RNNRootViewCreator>)creator {
	self = [super init];
	_creator = creator;
	_componentStore = [NSMutableDictionary new];
	return self;
}

- (RNNReactView *)createComponentIfNotExists:(RNNComponentOptions *)component parentComponentId:(NSString *)parentComponentId {
	NSMutableDictionary* parentComponentDict = [self componentsForParentId:parentComponentId];
	
	RNNReactView* reactView = [parentComponentDict objectForKey:component.componentId.get];
	if (!reactView) {
		reactView = (RNNReactView *)[_creator createRootViewFromComponentOptions:component];
		[parentComponentDict setObject:reactView forKey:component.componentId.get];
	}
	
	return reactView;
}

- (NSMutableDictionary *)componentsForParentId:(NSString *)parentComponentId {
	if (!_componentStore[parentComponentId]) {
		[_componentStore setObject:[NSMutableDictionary new] forKey:parentComponentId];;
	}
	
	return [_componentStore objectForKey:parentComponentId];;
}

- (void)removeComponent:(NSString *)componentId {
	if ([_componentStore objectForKey:componentId]) {
		[_componentStore removeObjectForKey:componentId];
	}
}

- (void)clean {
	[_componentStore removeAllObjects];
}


@end
