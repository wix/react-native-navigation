
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"
#import "RNNTopTabsViewController.h"
#import "RNNRootViewProtocol.h"

@interface RNNRootViewController : UIViewController	<RNNRootViewProtocol>

@property (nonatomic, strong) RNNNavigationOptions* navigationOptions;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* containerId;
@property (nonatomic, strong) RNNTopTabsViewController* topTabsViewController;

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		   animator:(RNNAnimator*)animator;


-(void)applyNavigationButtons;
-(void)applyTabBarItem;
-(void)applyTopTabsOptions;

-(BOOL)isAnimated;

@end
