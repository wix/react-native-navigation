#import <XCTest/XCTest.h>
#import "RNNTestRootViewCreator.h"
#import <ReactNativeNavigation/RNNEventEmitter.h>
#import <ReactNativeNavigation/RNNOverlayManager.h>
#import <ReactNativeNavigation/RNNModalManager.h>
#import <ReactNativeNavigation/RNNControllerFactory.h>
#import <ReactNativeNavigation/RNNCommandsHandler.h>
#import <FBSnapshotTestCase/FBSnapshotTestCase.h>

@interface NavigationSnapshotTest : FBSnapshotTestCase

@property (nonatomic, strong) RNNCommandsHandler* commandsHandler;
@property (nonatomic, strong) UIWindow* window;

@end

@implementation NavigationSnapshotTest

- (void)setUp {
	[super setUp];
	_window = [UIWindow new];
	RNNTestRootViewCreator* creator = [RNNTestRootViewCreator new];
	RNNEventEmitter* eventEmmiter = [RNNEventEmitter new];
	RNNOverlayManager* overlayManager = [RNNOverlayManager new];
	RNNModalManager* modalManager = [RNNModalManager new];
	RNNControllerFactory* controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:creator eventEmitter:eventEmmiter store:nil componentRegistry:nil andBridge:nil bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
	_commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory eventEmitter:eventEmmiter modalManager:modalManager overlayManager:overlayManager mainWindow:_window];
	[_commandsHandler setReadyToReceiveCommands:YES];
	self.recordMode = YES;
}

- (void)testSetRoot_shouldPresentComponent {
	[_commandsHandler setRoot:@{
		@"root": [self createComponentWithID:@"id" options:@{}]
	} commandId:@"SetRoot" completion:^{
		
	}];
	
	FBSnapshotVerifyView(_window.rootViewController.view, @"");
}

- (void)testSetRoot_shouldPresentStack {
	[_commandsHandler setRoot:@{
		@"root": [self createParentWithID:@"stack" type:@"Stack" options:@{} children:@[
			[self createComponentWithID:@"id" options:@{
				@"topBar": @{
						@"title": @{
								@"text": @"TTET"
						}
				}
			}]]]
	} commandId:@"SetRoot" completion:^{
		
	}];
	
	[_window.rootViewController beginAppearanceTransition:YES animated:YES];
	[_window.rootViewController endAppearanceTransition];
	FBSnapshotVerifyView(_window.rootViewController.view, @"");
}

- (NSDictionary *)createComponentWithID:(NSString *)componentId options:(NSDictionary *)options {
	return @{
		@"type": @"Component",
		@"id": componentId,
		@"data": @{
				@"options": options
		}
	};
}

- (NSDictionary *)createParentWithID:(NSString *)componentId type:(NSString *)type options:(NSDictionary *)options children:(NSArray<NSDictionary *> *)children {
	return @{
		@"type": type,
		@"id": componentId,
		@"data": @{
				@"options": options
		},
		@"children": children
	};
}


@end
