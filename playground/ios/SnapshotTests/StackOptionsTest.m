#import "CommandsHandlerCreator.h"
#import "FBSnapshotTestCase.h"
#import "LayoutCreator.h"
#import <XCTest/XCTest.h>

@interface StackOptionsTest : FBSnapshotTestCase
@property(nonatomic, strong) RNNCommandsHandler *commandsHandler;
@property(nonatomic, strong) UIWindow *window;
@end

@implementation StackOptionsTest

- (void)setUp {
    [super setUp];
    _window = [[[UIApplication sharedApplication] delegate] window];
    _commandsHandler = [CommandsHandlerCreator createWithWindow:_window];
    self.usesDrawViewHierarchyInRect = YES;

    if (NSProcessInfo.processInfo.environment[@"RECORD_SNAPSHOTS"]) {
        self.recordMode = YES;
    }
}

- (void)tearDown {
    [super tearDown];
    _window.rootViewController = nil;
}

#define RNNStackFlow(NAME, FIRST, SECOND, ...)                                                     \
    [self setRoot:[LayoutCreator component:@"FirstComponent" options:FIRST] testName:@ #NAME];     \
    [self push:[LayoutCreator component:@"SecondComponent" options:SECOND] testName:@ #NAME];      \
    [self pop:@"SecondComponent" testName:@ #NAME];

- (void)testStack_topBar {
    RNNStackFlow(opaque background, @{@"topBar" : @{@"background" : @{@"color" : @(0xFFFF00FF)}}},
                 @{@"topBar" : @{@"background" : @{@"color" : @(0xFFFF0000)}}});
    RNNStackFlow(tansparent background,
                 @{@"topBar" : @{@"background" : @{@"color" : @(0x00FFFFFF)}}},
                 @{@"topBar" : @{@"background" : @{@"color" : @(0xFFFF0000)}}});
    RNNStackFlow(translucent background, @{@"topBar" : @{@"background" : @{@"translucent" : @(1)}}},
                 @{@"topBar" : @{@"background" : @{@"translucent" : @(0)}}});
    RNNStackFlow(title change, @{@"topBar" : @{@"title" : @{@"text" : @"First Component"}}},
                 @{@"topBar" : @{@"title" : @{@"text" : @"Second Component"}}});
    RNNStackFlow(visibility, @{@"topBar" : @{@"visible" : @(0)}},
                 @{@"topBar" : @{@"visible" : @(1)}});
    RNNStackFlow(title font, @{@"topBar" : @{@"title" : @{@"text" : @"First Component"}}}, (@{
                     @"topBar" : @{
                         @"title" : @{
                             @"text" : @"Second Component",
                             @"fontFamily" : @"Arial",
                             @"color" : @(0xFFFF00FF),
                             @"fontSize" : @(15)
                         }
                     }
                 }));
}

- (void)setRoot:(NSDictionary *)firstComponent testName:(NSString *)testName {
    NSDictionary *root = [LayoutCreator stack:@{} children:@[ firstComponent ]];
    NSString *rootTestName = [NSString stringWithFormat:@"%@_root", testName];
    [_commandsHandler setRoot:@{@"root" : root}
                    commandId:@"SetRoot"
                   completion:^(NSString *componentId){
                   }];
    FBSnapshotVerifyView(_window, rootTestName);
}

- (void)push:(NSDictionary *)secondComponent testName:(NSString *)testName {
    NSString *pushTestName = [NSString stringWithFormat:@"%@_push", testName];
    [_commandsHandler push:@"FirstComponent"
                 commandId:@"push"
                    layout:secondComponent
                completion:^(NSString *pushedComponentId) {
                }
                 rejection:^(NSString *code, NSString *message, NSError *error){
                 }];
    FBSnapshotVerifyView(_window, pushTestName);
}

- (void)pop:(NSString *)componentId testName:(NSString *)testName {
    NSString *popTestName = [NSString stringWithFormat:@"%@_pop", testName];
    [_commandsHandler pop:componentId
                commandId:@"pop"
             mergeOptions:@{}
               completion:^{
               }
                rejection:^(NSString *code, NSString *message, NSError *error){
                }];
    FBSnapshotVerifyView(_window, popTestName);
}

@end
