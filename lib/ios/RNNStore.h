
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewController.h"

typedef void (^RNNTransitionCompletionBlock)(void);

@interface RNNStore : NSObject

-(UIViewController*) findComponentForId:(NSString*)componentId;
-(void) setComponent:(UIViewController*)viewController componentId:(NSString*)componentId;
-(void) removeComponent:(NSString*)componentId;
-(void) removeComponentByViewControllerInstance:(UIViewController*)componentInstance;

-(NSString*)componentKeyForInstance:(UIViewController*)instance;

-(void) setReadyToReceiveCommands:(BOOL)isReady;
-(BOOL) isReadyToReceiveCommands;

-(NSMutableArray*) pendingModalIdsToDismiss;

-(void) clean;

@end
