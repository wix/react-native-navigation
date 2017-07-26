//
//  RNNRootViewControllerTest.m
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 25/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "RNNRootViewController.h"
#import "RNNReactRootViewCreator.h"
#import "RNNTestRootViewCreator.h"
#import <React/RCTConvert.h>
#import "RNNNavigationOptions.h"

@interface RNNRootViewControllerTest : XCTestCase

@end

@implementation RNNRootViewControllerTest

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testExample {
    // This is an example of a functional test case.
    // Use XCTAssert and related functions to verify your tests produce the correct results.
}

- (void)testPerformanceExample {
    // This is an example of a performance test case.
    [self measureBlock:^{
        // Put the code you want to measure the time of here.
    }];
}

-(void)testInitRNNRootViewController{
	id<RNNRootViewCreator> creator = [[RNNTestRootViewCreator alloc] init];
	id emitter = nil;
	NSString* name = @"name";
	NSString* containerId = @"containerId2";
	NSDictionary* options = @{@"topBarBackgroundColor": @(0xFFFF0000)};
	
	XCTAssertNoThrow([[RNNRootViewController alloc] initWithName:name withOptions:options withContainerId:containerId rootViewCreator:creator eventEmitter:emitter]);
}

-(void)testTopBarBackgroundColor_validColor{
	id<RNNRootViewCreator> creator = [[RNNTestRootViewCreator alloc] init];
	NSString* name = @"somename";
	NSString* containerId = @"cntId";
	id emitter = nil;
	
	NSNumber* inputColor = @(0xFFFF0000);
	
	RNNNavigationOptions* options = [RNNNavigationOptions new];
	options.topBarBackgroundColor = inputColor;
	
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:name withOptions:options withContainerId:containerId rootViewCreator:creator eventEmitter:emitter];
	
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	
	[vc viewWillAppear:false];

	UIColor* expectedColor = [RCTConvert UIColor:inputColor];
	XCTAssertTrue([vc.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
	
}

@end
