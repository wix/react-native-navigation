
#import "RNNStore.h"

@interface RNNStore ()

@end

@implementation RNNStore {
	NSMapTable* _componentStore;
	NSMutableArray* _pendingModalIdsToDismiss;
	NSMutableDictionary* _externalComponentCreators;
	BOOL _isReadyToReceiveCommands;
}

-(instancetype)init {
	self = [super init];
	_isReadyToReceiveCommands = false;
	_componentStore = [NSMapTable strongToWeakObjectsMapTable];
	_pendingModalIdsToDismiss = [NSMutableArray new];
	_externalComponentCreators = [NSMutableDictionary new];
	return self;
}

-(UIViewController *)findComponentForId:(NSString *)componentId {
	return [_componentStore objectForKey:componentId];
}

- (void)setComponent:(UIViewController*)viewController componentId:(NSString*)componentId {
	UIViewController *existingVewController = [self findComponentForId:componentId];
	if (existingVewController) {
		@throw [NSException exceptionWithName:@"MultipleComponentId" reason:[@"Component id already exists " stringByAppendingString:componentId] userInfo:nil];
	}
	
	[_componentStore setObject:viewController forKey:componentId];
}

- (void)removeComponent:(NSString*)componentId {
	[_componentStore removeObjectForKey:componentId];
}

- (void)removeComponentByViewControllerInstance:(UIViewController*)componentInstance {
	NSString *foundKey = [self componentKeyForInstance:componentInstance];
	if (foundKey) {
		[self removeComponent:foundKey];
	}
}

-(void)setReadyToReceiveCommands:(BOOL)isReady {
	_isReadyToReceiveCommands = isReady;
}

-(BOOL)isReadyToReceiveCommands {
	return _isReadyToReceiveCommands;
}

-(NSMutableArray *)pendingModalIdsToDismiss {
	return _pendingModalIdsToDismiss;
}

-(void)clean {
	_isReadyToReceiveCommands = false;
	[_pendingModalIdsToDismiss removeAllObjects];
	[_componentStore removeAllObjects];
}

-(NSString*)componentKeyForInstance:(UIViewController*)instance {
	for (NSString *key in _componentStore) {
		UIViewController *value = [_componentStore objectForKey:key];
		if (value == instance) {
			return key;
		}
	}
	return nil;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
	[_externalComponentCreators setObject:[callback copy] forKey:name];
}

- (UIViewController *)getExternalComponent:(NSString *)name props:(NSDictionary*)props bridge:(RCTBridge *)bridge {
	RNNExternalViewCreator creator = [_externalComponentCreators objectForKey:name];
	return creator(props, bridge);
}

@end
