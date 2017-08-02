
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"

@interface RNNRootViewController : UIViewController

@property (nonatomic, strong) RNNNavigationOptions* navigationOptions;

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter;


@end
