#import <XCTest/XCTest.h>
#import "RNNTestRootViewCreator.h"
#import "LayoutCreator.h"
#import <ReactNativeNavigation/RNNEventEmitter.h>
#import <ReactNativeNavigation/RNNOverlayManager.h>
#import <ReactNativeNavigation/RNNModalManager.h>
#import <ReactNativeNavigation/RNNControllerFactory.h>
#import <ReactNativeNavigation/RNNCommandsHandler.h>
#import <FBSnapshotTestCase/FBSnapshotTestCase.h>

@interface StackOptionsTest : FBSnapshotTestCase

@property (nonatomic, strong) RNNCommandsHandler* commandsHandler;
@property (nonatomic, strong) UIWindow* window;

@end

@implementation StackOptionsTest

- (void)setUp {
	[super setUp];
	_window = [[[UIApplication sharedApplication] delegate] window];
	RNNTestRootViewCreator* creator = [RNNTestRootViewCreator new];
	RNNEventEmitter* eventEmmiter = [RNNEventEmitter new];
	RNNOverlayManager* overlayManager = [RNNOverlayManager new];
	RNNModalManager* modalManager = [RNNModalManager new];
	RNNControllerFactory* controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:creator eventEmitter:eventEmmiter store:nil componentRegistry:nil andBridge:nil bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];
	_commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory eventEmitter:eventEmmiter modalManager:modalManager overlayManager:overlayManager mainWindow:_window];
	[_commandsHandler setReadyToReceiveCommands:YES];
	[_commandsHandler setDefaultOptions:@{
		@"animations": @{
				@"push": @{
						@"enabled": @(0)
				},
				@"pop": @{
						@"enabled": @(0)
				}
		},
		@"topBar": @{
				@"drawBehind": @(1)
		},
		@"layout": @{
				@"componentBackgroundColor": @(0xFF00FF00)
		}
	} completion:^{}];
	self.usesDrawViewHierarchyInRect = YES;
//	self.recordMode = YES;
}

- (void)tearDown {
	[super tearDown];
	_window.rootViewController = nil;
}

- (void)testStack_backgroundColor {
	[self setRootPushAndPopWithTopBarOptions:@{
		@"background": @{
				@"color": @(0xFFFF00FF)
		}
	} secondTopBarOptions:@{
		@"background": @{
				@"color": @(0xFFFF0000)
		}
	}];
}

- (void)testStack_title {
	[self setRootPushAndPopWithTopBarOptions:@{
		@"title": @{
				@"text": @"First Component"
		}
	} secondTopBarOptions:@{
		@"title": @{
				@"text": @"Second Component"
		}
	}];
}

- (void)testStack_translucent {
	[self setRootPushAndPopWithTopBarOptions:@{
		@"background": @{
				@"translucent": @(0)
		}
	} secondTopBarOptions:@{
		@"background": @{
				@"translucent": @(1)
		}
	}];
}

- (void)testStack_topBarVisibility {
	[self setRootPushAndPopWithTopBarOptions:@{
		@"visible": @(0)
	} secondTopBarOptions:@{
		@"visible": @(1)
	}];
}

- (void)setRootPushAndPopWithTopBarOptions:(NSDictionary *)firstTopBarOptions secondTopBarOptions:(NSDictionary *)secondTopBarOptions {
	[self setRootPushAndPop:@{
		@"topBar": firstTopBarOptions
	} secondComponentOptions:@{
		@"topBar": secondTopBarOptions
	}];
}

- (void)setRootPushAndPop:(NSDictionary *)firstComponentOptions secondComponentOptions:(NSDictionary *)secondComponentOptions {
	NSDictionary* firstComponent = [LayoutCreator component:@"FirstComponent" options:firstComponentOptions];
	NSDictionary* secondComponent = [LayoutCreator component:@"SecondComponent" options:secondComponentOptions];
	NSDictionary* root = [LayoutCreator stack:@{} children:@[firstComponent]];

	[_commandsHandler setRoot:@{@"root": root}
					commandId:@"SetRoot"
				   completion:^{}];
	FBSnapshotVerifyView(_window, @"root");

	[_commandsHandler push:@"FirstComponent"
				 commandId:@"push"
					layout:secondComponent
				completion:^{}
				 rejection:^(NSString *code, NSString *message, NSError *error) {}];
	FBSnapshotVerifyView(_window, @"push");
	
	[_commandsHandler pop:@"SecondComponent"
				commandId:@"pop"
			 mergeOptions:@{}
			   completion:^{}
				rejection:^(NSString *code, NSString *message, NSError *error) {}];
	FBSnapshotVerifyView(_window, @"pop");
}


@end
