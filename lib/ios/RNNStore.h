
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewController.h"

typedef void (^RNNTransitionCompletionBlock)(void);
typedef UIViewController * (^RNNExternalViewCreator)(void);

@interface RNNStore : NSObject

-(UIViewController*) findComponentForId:(NSString*)componentId;
-(void) setComponent:(UIViewController*)viewController componentId:(NSString*)componentId;
-(void) removeComponent:(NSString*)componentId;
-(void) removeComponentByViewControllerInstance:(UIViewController*)componentInstance;

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;
- (UIViewController *)getExternalComponent:(NSString *)name;

-(NSString*)componentKeyForInstance:(UIViewController*)instance;

-(void) setReadyToReceiveCommands:(BOOL)isReady;
-(BOOL) isReadyToReceiveCommands;

-(NSMutableArray*) pendingModalIdsToDismiss;

-(void) clean;

@end
