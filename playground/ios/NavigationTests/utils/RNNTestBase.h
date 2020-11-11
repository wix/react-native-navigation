#import <Foundation/Foundation.h>
#import <XCTest/XCTest.h>

@interface RNNTestBase : XCTestCase
- (void)setupTopLevelUI:(UIViewController *)withViewController;

- (void)tearDownTopLevelUI:(UIViewController *)withViewController;
@end