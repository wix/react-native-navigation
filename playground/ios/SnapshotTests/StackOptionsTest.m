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

- (void)testStack_shouldChangeBackgroundColor {
	[self setRootPushAndPop:@{
		@"topBar": @{
				@"background": @{
						@"color": @(0xFFFF00FF)
				}
		}
	} secondComponentOptions:@{
		@"topBar": @{
				@"background": @{
						@"color": @(0xFFFF0000)
				}
		}
	}];
}

- (void)testStack_shouldChangeTitle {
	[self setRootPushAndPop:@{
		@"topBar": @{
				@"title": @{
						@"text": @"First Component"
				}
		}
	} secondComponentOptions:@{
		@"topBar": @{
				@"title": @{
						@"text": @"Second Component"
				}
		}
	}];
}

- (void)testStack_shouldSetTranslucentBackground {
	[self setRootPushAndPop:@{
		@"topBar": @{
				@"background": @{
						@"translucent": @(0)
				}
		}
	} secondComponentOptions:@{
		@"topBar": @{
				@"background": @{
						@"translucent": @(1)
				}
		}
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
