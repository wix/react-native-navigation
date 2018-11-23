
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNStore.h"
#import "RNNEventEmitter.h"
#import "RNNParentProtocol.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
						  eventEmitter:(RNNEventEmitter*)eventEmitter
							 andBridge:(RCTBridge*)bridge;

- (UIViewController<RNNParentProtocol> *)createLayout:(NSDictionary*)layout saveToStore:(RNNStore *)store;

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;

@property (nonatomic, strong) RNNNavigationOptions* defaultOptions;

@end
