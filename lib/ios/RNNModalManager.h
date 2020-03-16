#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString * _Nonnull componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString * _Nonnull code, NSString * _Nonnull message, NSError * _Nullable error);

@protocol RNNModalManagerDelegate <NSObject>

- (void)dismissedModal:(UIViewController * _Nonnull)viewController;
- (void)attemptedToDismissModal:(UIViewController * _Nonnull)viewController;
- (void)dismissedMultipleModals:(NSArray * _Nonnull)viewControllers;

@end

@interface RNNModalManager : NSObject <UIAdaptivePresentationControllerDelegate>

- (instancetype _Nullable)initWithBridge:(RCTBridge * _Nonnull)bridge;

@property (nonatomic, weak) id<RNNModalManagerDelegate> _Nullable delegate;

- (void)showModal:(UIViewController * _Nonnull)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock _Nonnull)completion;
- (void)dismissModal:(UIViewController * _Nonnull)viewController completion:(RNNTransitionCompletionBlock _Nonnull)completion;
- (void)dismissAllModalsAnimated:(BOOL)animated completion:(void (^ __nullable)(void))completion;
- (void)dismissAllModalsSynchronosly;

@end
