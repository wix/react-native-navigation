#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "RNNControllerFactory.h"
#import "RNNStore.h"

@interface RNNCommandsHandler : NSObject

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory andBridge:(RCTBridge*)bridge;

-(void) setRoot:(NSDictionary*)layout;

-(void) setOptions:(NSString*)containerId options:(NSDictionary*)options;

-(void) push:(NSString*)containerId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) pop:(NSString*)containerId options:(NSDictionary*)options;

-(void) popTo:(NSString*)containerId;

-(void) popToRoot:(NSString*)containerId;

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) dismissModal:(NSString*)containerId;

-(void) dismissAllModals;

@end
