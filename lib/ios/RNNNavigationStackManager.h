#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"

@interface RNNNavigationStackManager : NSObject

@property (nonatomic) int loadCount;
-(instancetype)initWithStore:(RNNStore*)store;


-(void)push:(UIViewController<RNNRootViewProtocol>*)newTop onTop:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;
-(void)pop:(NSString*)componentId withTransitionOptions:(RNNAnimationOptions*)transitionOptions rejection:(RCTPromiseRejectBlock)rejection;
-(void)popTo:(NSString*)componentId rejection:(RCTPromiseRejectBlock)rejection;
-(void)popToRoot:(NSString*)componentId rejection:(RCTPromiseRejectBlock)rejection;
-(void)setStackRoot:(UIViewController<RNNRootViewProtocol> *)newRoot fromComponent:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection;

@end
