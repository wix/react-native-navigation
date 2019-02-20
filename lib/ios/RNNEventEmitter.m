#import "RNNEventEmitter.h"
#import "RNNUtils.h"

@implementation RNNEventEmitter {
	NSInteger _appLaunchedListenerCount;
	BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE();

static NSString* const AppLaunched				= @"RNN.AppLaunched";
static NSString* const CommandCompleted			= @"RNN.CommandCompleted";
static NSString* const BottomTabSelected		= @"RNN.BottomTabSelected";
static NSString* const ComponentDidAppear		= @"RNN.ComponentDidAppear";
static NSString* const ComponentDidDisappear	= @"RNN.ComponentDidDisappear";
static NSString* const NavigationButtonPressed	= @"RNN.NavigationButtonPressed";
static NSString* const ModalDismissed	        = @"RNN.ModalDismissed";
static NSString* const SearchBarUpdated 		= @"RNN.SearchBarUpdated";
static NSString* const SearchBarCancelPressed 	= @"RNN.SearchBarCancelPressed";
static NSString* const PreviewCompleted         = @"RNN.PreviewCompleted";

-(NSArray<NSString *> *)supportedEvents {
	return @[AppLaunched,
			 CommandCompleted,
			 BottomTabSelected,
			 ComponentDidAppear,
			 ComponentDidDisappear,
			 NavigationButtonPressed,
			 ModalDismissed,
			 SearchBarUpdated,
			 SearchBarCancelPressed,
			 PreviewCompleted];
}

# pragma mark public

-(void)sendOnAppLaunched {
	if (_appLaunchedListenerCount > 0) {
		[self send:AppLaunched body:nil];
	} else {
		_appLaunchedEventDeferred = TRUE;
	}
}

-(void)sendComponentDidAppear:(NSString *)componentId componentName:(NSString *)componentName {
	[self send:ComponentDidAppear body:@{
										 @"componentId":componentId,
										 @"componentName": componentName
										 }];
}

-(void)sendComponentDidDisappear:(NSString *)componentId componentName:(NSString *)componentName{
	[self send:ComponentDidDisappear body:@{
											@"componentId":componentId,
											@"componentName": componentName
											}];
}

-(void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString*)buttonId {
	[self send:NavigationButtonPressed body:@{
											  @"componentId": componentId,
											  @"buttonId": buttonId
											  }];
}

-(void)sendBottomTabSelected:(NSNumber *)selectedTabIndex unselected:(NSNumber*)unselectedTabIndex {
	[self send:BottomTabSelected body:@{
									  @"selectedTabIndex": selectedTabIndex,
									  @"unselectedTabIndex": unselectedTabIndex
									  }];
}

-(void)sendOnNavigationCommandCompletion:(NSString *)commandName commandId:(NSString *)commandId params:(NSDictionary*)params {
	[self send:CommandCompleted body:@{
									   @"commandId":commandId,
									   @"commandName":commandName,
									   @"params": params,
									   @"completionTime": [RNNUtils getCurrentTimestamp]
									   }];
}

-(void)sendOnSearchBarUpdated:(NSString *)componentId
						 text:(NSString*)text
					isFocused:(BOOL)isFocused {
	[self send:SearchBarUpdated body:@{
									   @"componentId": componentId,
									   @"text": text,
									   @"isFocused": @(isFocused)
									   }];
}

- (void)sendOnSearchBarCancelPressed:(NSString *)componentId {
	[self send:SearchBarCancelPressed body:@{
											@"componentId": componentId
											}];
}

- (void)sendOnPreviewCompleted:(NSString *)componentId previewComponentId:(NSString *)previewComponentId {
	[self send:PreviewCompleted body:@{
											 @"componentId": componentId,
											 @"previewComponentId": previewComponentId
                       }];
}

- (void)sendModalsDismissedEvent:(NSString *)componentId numberOfModalsDismissed:(NSNumber *)modalsDismissed {
	[self send:ModalDismissed body:@{
											 @"componentId": componentId,
											 @"modalsDismissed": modalsDismissed
											 }];
}

- (void)addListener:(NSString *)eventName {
	[super addListener:eventName];
	if ([eventName isEqualToString:AppLaunched]) {
		_appLaunchedListenerCount++;
		if (_appLaunchedEventDeferred) {
			_appLaunchedEventDeferred = FALSE;
			[self sendOnAppLaunched];
		}
	}
}

# pragma mark private

-(void)send:(NSString *)eventName body:(id)body {
	if (self.bridge == nil) {
		return;
	}
	[self sendEventWithName:eventName body:body];
}

@end
