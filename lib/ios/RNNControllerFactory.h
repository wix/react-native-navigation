
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNStore.h"
#import "RNNEventEmitter.h"
#import "RNNRootViewProtocol.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
								 store:(RNNStore*)store
						  eventEmitter:(RNNEventEmitter*)eventEmitter
							 andBridge:(RCTBridge*)bridge;

-(UIViewController<RNNRootViewProtocol> *)createLayoutAndSaveToStore:(NSDictionary*)layout;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@end
