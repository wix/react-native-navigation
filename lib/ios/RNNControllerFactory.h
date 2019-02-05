
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNStore.h"
#import "RNNEventEmitter.h"
#import "RNNParentProtocol.h"
#import "RNNReactComponentManager.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
						  eventEmitter:(RNNEventEmitter*)eventEmitter
								 store:(RNNStore *)store
					  componentManager:(RNNReactComponentManager *)componentManager
							 andBridge:(RCTBridge*)bridge;

- (UIViewController<RNNParentProtocol> *)createLayout:(NSDictionary*)layout;

- (NSArray<RNNLayoutProtocol> *)createChildrenLayout:(NSArray*)children;

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@end
