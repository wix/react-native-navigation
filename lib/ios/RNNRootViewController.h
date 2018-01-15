
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

@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) RNNTopTabsViewController* topTabsViewController;

-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		   animator:(RNNAnimator*)animator;


-(void)applyTabBarItem;
-(void)applyTopTabsOptions;

-(BOOL)isCustomTransitioned;

@end
