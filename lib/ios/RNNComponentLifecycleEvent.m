#import "RNNComponentLifecycleEvent.h"

@interface RNNComponentLifecycleEvent()
@property (nonatomic, strong) NSString* componentName;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) LifecycleEvent event;

@end

@implementation RNNComponentLifecycleEvent

+ (instancetype)create:(LifecycleEvent)event componentName:(NSString *)componentName componentId:(NSString *)componentId {
	RNNComponentLifecycleEvent* lifecycleEvent = [[RNNComponentLifecycleEvent alloc] init];
	lifecycleEvent.componentName = componentName;
	lifecycleEvent.componentId = componentId;
	lifecycleEvent.event = event;
	return lifecycleEvent;
}

- (NSDictionary *)body {
	return @{@"event": self.event, @"componentName": self.componentName, @"componentId": self.componentId};
}

@end
