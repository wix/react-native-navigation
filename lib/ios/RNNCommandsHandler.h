#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "RNNControllerFactory.h"
#import "RNNStore.h"

@interface RNNCommandsHandler : NSObject

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory andBridge:(RCTBridge*)bridge;

-(void) setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) setOptions:(NSString*)containerId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

-(void) push:(NSString*)containerId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) pop:(NSString*)containerId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

-(void) popTo:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion;

-(void) popToRoot:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion;

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) dismissModal:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion;

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion;

@end
