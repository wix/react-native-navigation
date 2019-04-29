
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNExternalComponentStore.h"
#import "RNNEventEmitter.h"
#import "RNNReactComponentRegistry.h"
#import "RNNNavigationOptions.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
						  eventEmitter:(RNNEventEmitter*)eventEmitter
								 store:(RNNExternalComponentStore *)store
					  componentRegistry:(RNNReactComponentRegistry *)componentRegistry
							 andBridge:(RCTBridge*)bridge;

- (UIViewController *)createLayout:(NSDictionary*)layout;

- (NSArray *)createChildrenLayout:(NSArray*)children;

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@end
