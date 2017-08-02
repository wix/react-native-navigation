
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewController.h"

@interface RNNStore : NSObject

-(RNNRootViewController*) findContainerForId:(NSString*)containerId;
-(void) setContainer:(UIViewController*)viewController containerId:(NSString*)containerId;
-(void) removeContainer:(NSString*)containerId;
-(void) removeContainerByViewControllerInstance:(UIViewController*)containerInstance;

-(NSString*)containerKeyForInstance:(UIViewController*)instance;

-(void) setReadyToReceiveCommands:(BOOL)isReady;
-(BOOL) isReadyToReceiveCommands;

-(NSMutableArray*) pendingModalIdsToDismiss;

-(void) clean;

@end
