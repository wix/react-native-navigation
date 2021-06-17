#import "RNNModalManagerEventHandler.h"
#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTModalHostViewManager.h>
#import <UIKit/UIKit.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString *_Nonnull componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString *_Nonnull code, NSString *_Nonnull message,
                                            NSError *_Nullable error);

@interface RNNModalManager : NSObject <UIAdaptivePresentationControllerDelegate>

- (instancetype _Nonnull)initWithBridge:(RCTBridge *_Nonnull)bridge
                           eventHandler:(RNNModalManagerEventHandler *_Nonnull)eventHandler;
- (void)connectModalHostViewManager:(RCTModalHostViewManager *_Nonnull)modalHostViewManager;

- (void)showModal:(UIViewController *_Nonnull)viewController
         animated:(BOOL)animated
       completion:(RNNTransitionWithComponentIdCompletionBlock _Nullable)completion;
- (void)dismissModal:(UIViewController *_Nullable)viewController
            animated:(BOOL)animated
          completion:(RNNTransitionCompletionBlock _Nullable)completion;
- (void)dismissAllModalsAnimated:(BOOL)animated completion:(void (^__nullable)(void))completion;
- (void)dismissAllModalsSynchronosly;

@end
