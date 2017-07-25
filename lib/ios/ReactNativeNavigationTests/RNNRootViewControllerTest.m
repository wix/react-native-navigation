//
//  RNNRootViewControllerTest.m
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 25/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "RNNRootViewController.h"

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

-(void)testTopBarBackgroundColor_color{
	id creator = nil;
	id emitter = nil;
	NSDictionary* json =  @{@"id": @"cntId",
							@"type": @"ContainerStack",
							@"data": @{},
							@"children": @[
									@{@"id": @"cntId_2",
									  @"type": @"Container",
									  @"data": @{@"topBarBackgroundColor" : @(0xFFFF0000)},
									  @"children": @[]}]};
	RNNLayoutNode* node = [RNNLayoutNode create:json];
	RNNRootViewController* vc = [[RNNRootViewController alloc] initWithNode:node rootViewCreator:creator eventEmitter:emitter];
	XCTAssertTrue([vc.navigationController.navigationBar.barTintColor isEqual:@(0xFFFF0000)]);
	
}

@end
