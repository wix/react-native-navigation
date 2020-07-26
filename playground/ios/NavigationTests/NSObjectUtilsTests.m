#import <XCTest/XCTest.h>
#import <ReactNativeNavigation/NSObject+Utils.h>
#import <ReactNativeNavigation/RNNLayoutOptions.h>
#import <ReactNativeNavigation/ElementTransitionOptions.h>

@interface TestClass : NSObject
@property (nonatomic, strong) NSString* testProperty;
@end
@implementation TestClass
@end

@interface TestSubClass : TestClass
@property (nonatomic, strong) NSString* testSubProperty;
@end
@implementation TestSubClass
@end

@interface NSObjectUtilsTests : XCTestCase

@end

@implementation NSObjectUtilsTests

- (void)testReturnObjectProperties {
	TestClass* testObject = [TestClass new];
	NSArray* properties = [testObject objectProperties];
	NSArray* expectedProperties = @[@"testProperty"];
	XCTAssertTrue([properties isEqualToArray:expectedProperties]);
}

- (void)testReturnSuperClassObjectProperties {
	TestSubClass* testObject = [TestSubClass new];
	NSArray* properties = [testObject objectProperties];
	NSArray* expectedProperties = @[@"testSubProperty", @"testProperty"];
	XCTAssertTrue([properties isEqualToArray:expectedProperties]);
}

@end
