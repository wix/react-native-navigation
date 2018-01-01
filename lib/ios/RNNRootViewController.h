
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"
#import "RNNRootViewProtocol.h"

@interface RNNRootViewController : UIViewController	<RNNRootViewProtocol>

@property (nonatomic, strong) RNNNavigationOptions* navigationOptions;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* containerId;

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		   animator:(RNNAnimator*)animator;


-(void)applyNavigationButtons;
-(void)applyTabBarItem;

@end
