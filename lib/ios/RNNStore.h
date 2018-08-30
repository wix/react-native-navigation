
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewProtocol.h"
#import "ReactNativeNavigation.h"

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString *componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@interface RNNStore : NSObject

-(UIViewController*) findComponentForId:(NSString*)componentId;
-(void) setComponent:(UIViewController*)viewController componentId:(NSString*)componentId;
-(void) removeComponent:(NSString*)componentId;
-(void) removeComponentByViewControllerInstance:(UIViewController*)componentInstance;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(NSString *)name props:(NSDictionary*)props bridge:(RCTBridge*)bridge;

-(NSString*)componentKeyForInstance:(UIViewController*)instance;

-(void) setReadyToReceiveCommands:(BOOL)isReady;
-(BOOL) isReadyToReceiveCommands;

-(NSMutableArray*) pendingModalIdsToDismiss;

-(void) clean;

@end
