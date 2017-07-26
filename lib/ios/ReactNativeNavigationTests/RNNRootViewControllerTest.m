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

@property (nonatomic) id<RNNRootViewCreator> creator;
@property (nonatomic) NSString* pageName;
@property (nonatomic) NSString* containerId;
@property (nonatomic) id emitter;
@property (nonatomic) RNNNavigationOptions* options;
@end

@implementation RNNRootViewControllerTest

- (void)setUp {
    [super setUp];
	self.creator = [[RNNTestRootViewCreator alloc] init];
	self.pageName = @"somename";
	self.containerId = @"cntId";
	self.emitter = nil;
	self.options = [RNNNavigationOptions new];
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
	RNNNavigationOptions* options = [RNNNavigationOptions new];
	
	XCTAssertNoThrow([[RNNRootViewController alloc] initWithName:name withOptions:options withContainerId:containerId rootViewCreator:creator eventEmitter:emitter]);
}

-(void)testTopBarBackgroundColor_validColor{
	NSNumber* inputColor = @(0xFFFF0000);
	self.options.topBarBackgroundColor = inputColor;
	
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
	
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	
	[vc viewWillAppear:false];

	UIColor* expectedColor = [RCTConvert UIColor:inputColor];
	XCTAssertTrue([vc.navigationController.navigationBar.barTintColor isEqual:expectedColor]);
	
}

- (void)testStatusBarHidden_default {
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
	
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	
	[vc viewWillAppear:false];
	
	XCTAssertFalse([vc prefersStatusBarHidden]);
}

- (void)testStatusBarHidden_true {
	self.options.statusBarHidden = @(1);
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
	
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	
	[vc viewWillAppear:false];
	
	XCTAssertTrue([vc prefersStatusBarHidden]);
}

- (void)testStatusBarHidden_false {
	self.options.statusBarHidden = @(0);
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithName:self.pageName withOptions:self.options withContainerId:self.containerId rootViewCreator:self.creator eventEmitter:self.emitter];
	
	__unused UINavigationController* nav = [[UINavigationController alloc] initWithRootViewController:vc];
	
	[vc viewWillAppear:false];
	
	XCTAssertFalse([vc prefersStatusBarHidden]);
}

-
@end
