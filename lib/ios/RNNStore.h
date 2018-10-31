
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNParentProtocol.h"
#import "ReactNativeNavigation.h"

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString *componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@interface RNNStore : NSObject

- (UIViewController*)findComponentForId:(NSString*)componentId;
- (void)setComponent:(UIViewController*)viewController componentId:(NSString*)componentId;
- (void)removeComponent:(NSString*)componentId;
- (void)removeComponentByViewControllerInstance:(UIViewController*)componentInstance;
- (void)removeAllComponents;
- (void)removeAllComponentsFromWindow:(UIWindow *)window;
- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(RNNLayoutInfo *)layoutInfo bridge:(RCTBridge *)bridge;

- (NSString*)componentKeyForInstance:(UIViewController*)instance;

- (void)setReadyToReceiveCommands:(BOOL)isReady;
- (BOOL)isReadyToReceiveCommands;

- (void)clean;

@end
