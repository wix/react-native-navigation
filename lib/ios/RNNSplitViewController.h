
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNSplitViewOptions.h"
#import "RNNAnimator.h"
#import "RNNTopTabsViewController.h"
#import "RNNParentProtocol.h"

@interface RNNSplitViewController : UISplitViewController	<RNNParentProtocol>

@property (nonatomic, strong) RNNSplitViewOptions* options;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, retain) RNNLayoutInfo* layoutInfo;
@property (nonatomic, retain) RNNBasePresenter* presenter;
@property (nonatomic) id<RNNRootViewCreator> creator;

-(instancetype)initWithOptions:(RNNSplitViewOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
				  eventEmitter:(RNNEventEmitter*)eventEmitter;

@end
