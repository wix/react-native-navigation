#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "RNNControllerFactory.h"
#import "RNNStore.h"

@interface RNNCommandsHandler : NSObject

- (instancetype)initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory eventEmitter:(RNNEventEmitter*)eventEmitter;

- (void)setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion;

- (void)mergeOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

- (void)setDefaultOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion;

- (void)push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

- (void)pop:(NSString*)componentId mergeOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

- (void)popTo:(NSString*)componentId mergeOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

- (void)popToRoot:(NSString*)componentId mergeOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

- (void)setStackRoot:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

- (void)showModal:(NSDictionary*)layout completion:(RNNTransitionWithComponentIdCompletionBlock)completion;

- (void)dismissModal:(NSString*)componentId mergeOptions:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject;

- (void)dismissAllModals:(NSDictionary *)options completion:(RNNTransitionCompletionBlock)completion;

- (void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion;

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)reject;

@end
