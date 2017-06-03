
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewCreator.h"
#import "RNNStore.h"
#import "RNNEventEmitter.h"

@interface RNNControllerFactory : NSObject

-(instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
								 store:(RNNStore*)store
						  eventEmitter:(RNNEventEmitter*)eventEmitter;

-(UIViewController*)createLayoutAndSaveToStore:(NSDictionary*)layout;

@end
