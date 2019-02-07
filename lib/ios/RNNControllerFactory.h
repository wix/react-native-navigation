
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNStore.h"
#import "RNNEventEmitter.h"
#import "RNNParentProtocol.h"
#import "RNNReactComponentRegistry.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
						  eventEmitter:(RNNEventEmitter*)eventEmitter
								 store:(RNNStore *)store
					  componentRegistry:(RNNReactComponentRegistry *)componentRegistry
							 andBridge:(RCTBridge*)bridge;

- (UIViewController<RNNParentProtocol> *)createLayout:(NSDictionary*)layout;

- (NSArray<RNNLayoutProtocol> *)createChildrenLayout:(NSArray*)children;

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@end
