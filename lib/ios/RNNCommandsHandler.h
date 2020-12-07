#import "RNNControllerFactory.h"
#import "RNNLayoutManager.h"
#import "RNNModalManager.h"
#import "RNNOverlayManager.h"
#import "RNNSetRootAnimator.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNCommandsHandler : NSObject

- (instancetype)initWithControllerFactory:(RNNControllerFactory *)controllerFactory
                            layoutManager:(RNNLayoutManager *)layoutManager
                             eventEmitter:(RNNEventEmitter *)eventEmitter
                             modalManager:(RNNModalManager *)modalManager
                           overlayManager:(RNNOverlayManager *)overlayManager
                          setRootAnimator:(RNNSetRootAnimator *)setRootAnimator
                               mainWindow:(UIWindow *)mainWindow;

@property(nonatomic) BOOL readyToReceiveCommands;

- (void)setRoot:(NSDictionary *)layout
      commandId:(NSString *)commandId
     completion:(RNNTransitionWithComponentIdCompletionBlock)completion;

- (void)mergeOptions:(NSString *)componentId
             options:(NSDictionary *)options
          completion:(RNNTransitionCompletionBlock)completion;

- (void)setDefaultOptions:(NSDictionary *)options
               completion:(RNNTransitionCompletionBlock)completion;

- (void)push:(NSString *)componentId
     commandId:(NSString *)commandId
        layout:(NSDictionary *)layout
    completion:(RNNTransitionWithComponentIdCompletionBlock)completion
     rejection:(RCTPromiseRejectBlock)rejection;

- (void)pop:(NSString *)componentId
       commandId:(NSString *)commandId
    mergeOptions:(NSDictionary *)options
      completion:(RNNTransitionCompletionBlock)completion
       rejection:(RCTPromiseRejectBlock)rejection;

- (void)popTo:(NSString *)componentId
       commandId:(NSString *)commandId
    mergeOptions:(NSDictionary *)options
      completion:(RNNTransitionCompletionBlock)completion
       rejection:(RCTPromiseRejectBlock)rejection;

- (void)popToRoot:(NSString *)componentId
        commandId:(NSString *)commandId
     mergeOptions:(NSDictionary *)options
       completion:(RNNTransitionCompletionBlock)completion
        rejection:(RCTPromiseRejectBlock)rejection;

- (void)setStackRoot:(NSString *)componentId
           commandId:(NSString *)commandId
            children:(NSArray *)children
          completion:(RNNTransitionCompletionBlock)completion
           rejection:(RCTPromiseRejectBlock)rejection;

- (void)showModal:(NSDictionary *)layout
        commandId:(NSString *)commandId
       completion:(RNNTransitionWithComponentIdCompletionBlock)completion;

- (void)dismissModal:(NSString *)componentId
           commandId:(NSString *)commandId
        mergeOptions:(NSDictionary *)options
          completion:(RNNTransitionWithComponentIdCompletionBlock)completion
           rejection:(RNNTransitionRejectionBlock)reject;

- (void)dismissAllModals:(NSDictionary *)options
               commandId:(NSString *)commandId
              completion:(RNNTransitionCompletionBlock)completion;

- (void)showOverlay:(NSDictionary *)layout
          commandId:(NSString *)commandId
         completion:(RNNTransitionWithComponentIdCompletionBlock)completion;

- (void)dismissOverlay:(NSString *)componentId
             commandId:(NSString *)commandId
            completion:(RNNTransitionCompletionBlock)completion
             rejection:(RNNTransitionRejectionBlock)reject;

- (void)dismissAllOverlays:(NSString *)commandId;

@end
