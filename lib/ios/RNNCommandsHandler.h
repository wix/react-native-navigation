#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "RNNControllerFactory.h"
#import "RNNStore.h"

@interface RNNCommandsHandler : NSObject

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory;

-(void) setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) setOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

-(void) setDefaultOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

-(void) push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) pop:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

-(void) popTo:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion;

-(void) popToRoot:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion;

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

-(void) dismissModal:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion;

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion;

-(void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion;

-(void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion;

@end
