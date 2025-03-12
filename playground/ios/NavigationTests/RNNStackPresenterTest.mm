#import "RNNComponentViewController+Utils.h"
#import "RNNStackController.h"
#import "UIImage+Utils.h"
#import "UINavigationController+RNNOptions.h"
#import <OCMock/OCMock.h>
#import <ReactNativeNavigation/RNNStackPresenter.h>
#import <XCTest/XCTest.h>

@interface RNNStackPresenterTest : XCTestCase

@property(nonatomic, strong) RNNStackPresenter *uut;
@property(nonatomic, strong) RNNNavigationOptions *options;
@property(nonatomic, strong) RNNStackController *boundViewController;

@end

@implementation RNNStackPresenterTest

- (void)setUp {
    [super setUp];
    self.uut = [[RNNStackPresenter alloc] init];
    RNNStackController *stackController = [[RNNStackController alloc]
          initWithLayoutInfo:nil
                     creator:nil
                     options:[RNNNavigationOptions emptyOptions]
              defaultOptions:nil
                   presenter:self.uut
                eventEmitter:nil
        childViewControllers:@[
            [RNNComponentViewController createWithComponentId:@"component1"],
            [RNNComponentViewController createWithComponentId:@"component2"]
        ]];
    self.boundViewController = [OCMockObject partialMockForObject:stackController];
    [self.uut bindViewController:self.boundViewController];
    self.options = [RNNNavigationOptions emptyOptions];
}

- (void)testApplyOptionsBeforePoppingShouldSetTopBarBackgroundForPoppingViewController {
    _options.topBar.background.color = [[Color alloc] initWithValue:[UIColor redColor]];

    [self.uut applyOptionsBeforePopping:self.options];
    XCTAssertFalse([_boundViewController.childViewControllers.lastObject.navigationItem
                        .standardAppearance.backgroundColor isEqual:[UIColor redColor]]);
}

- (void)testApplyOptionsShouldSetLargeTitleVisible {
    _options.topBar.largeTitle.visible = [[Bool alloc] initWithBOOL:YES];

    [self.uut applyOptions:self.options];
    XCTAssertTrue([[_boundViewController navigationBar] prefersLargeTitles]);
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withTitle {
    Text *title = [[Text alloc] initWithValue:@"Title"];
    self.options.topBar.backButton.title = title;
    [self.uut applyOptions:self.options];
    XCTAssertTrue(
        [self.boundViewController.viewControllers.firstObject.navigationItem.backBarButtonItem.title
            isEqual:@"Title"]);
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withHideTitle {
    Text *title = [[Text alloc] initWithValue:@"Title"];
    self.options.topBar.backButton.title = title;
    self.options.topBar.backButton.showTitle = [[Bool alloc] initWithValue:@(0)];
    [self.uut applyOptions:self.options];
    NSLog(@"%@", self.boundViewController.viewControllers.firstObject.navigationItem
                     .backBarButtonItem.title);
    XCTAssertTrue(
        [self.boundViewController.viewControllers.firstObject.navigationItem.backBarButtonItem.title
            isEqualToString:@""]);
}

- (void)testApplyOptions_shouldSetBackButtonOnBoundViewController_withDefaultValues {
    [self.uut applyOptions:self.options];
    XCTAssertTrue(
        [self.boundViewController.viewControllers.firstObject.navigationItem.backBarButtonItem.title
            isEqualToString:@""]);
}

- (void)testSetBackButtonIcon_withColor_shouldSetColor {
    Color *color = [[Color alloc] initWithValue:UIColor.redColor];
    self.options.topBar.backButton.color = color;
    [self.uut applyOptions:self.options];
    XCTAssertEqual(self.boundViewController.viewControllers.firstObject.navigationItem
                       .backBarButtonItem.tintColor,
                   UIColor.redColor);
}

- (void)testSetBackButtonIcon_withColor_shouldSetTitle {
    Color *color = [[Color alloc] initWithValue:UIColor.redColor];
    Text *title = [[Text alloc] initWithValue:@"Title"];
    self.options.topBar.backButton.color = color;
    self.options.topBar.backButton.title = title;
    [self.uut applyOptions:self.options];
    XCTAssertEqual(self.boundViewController.viewControllers.firstObject.navigationItem
                       .backBarButtonItem.tintColor,
                   UIColor.redColor);
    XCTAssertEqual(
        self.boundViewController.viewControllers.firstObject.navigationItem.backBarButtonItem.title,
        @"Title");
}

- (void)testSetBackButtonIcon_withColor_shouldSetIcon {
    Color *color = [[Color alloc] initWithValue:UIColor.redColor];
    UIImage *image = [UIImage emptyImage];

    Image *icon = [[Image alloc] initWithValue:image];
    self.options.topBar.backButton.color = color;
    self.options.topBar.backButton.icon = icon;
    [self.uut applyOptions:self.options];
    XCTAssertEqual(self.boundViewController.viewControllers.firstObject.navigationItem
                       .backBarButtonItem.tintColor,
                   UIColor.redColor);
    XCTAssertTrue([self.boundViewController.viewControllers.lastObject.navigationItem
                       .standardAppearance.backIndicatorImage isEqual:image]);
}

- (void)testBackgroundColor_validColor {
    UIColor *inputColor = [RCTConvert UIColor:@(0xFFFF0000)];
    self.options.layout.backgroundColor = [[Color alloc] initWithValue:inputColor];
    [self.uut applyOptions:self.options];
    UIColor *expectedColor = [UIColor colorWithRed:1 green:0 blue:0 alpha:1];
    XCTAssertTrue([self.boundViewController.view.backgroundColor isEqual:expectedColor]);
}

@end
