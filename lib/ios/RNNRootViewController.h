
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"
#import "RNNTopTabsViewController.h"
#import "RNNRootViewProtocol.h"

@class RNNRootViewController;

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNRootViewController : UIViewController	<RNNRootViewProtocol, UIViewControllerPreviewingDelegate, UISearchResultsUpdating>

@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) RNNTopTabsViewController* topTabsViewController;
@property (nonatomic) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) UIViewController* previewController;


-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		  isExternalComponent:(BOOL)isExternalComponent;

- (void)onReactViewReady:(RNNReactViewReadyCompletionBlock)readyBlock;

-(void)applyTabBarItem;
-(void)applyTopTabsOptions;

@end
