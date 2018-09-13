#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"
#import "RNNUIBarButtonItem.h"
#import "RNNParentInfo.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);

@interface RNNRootViewController : UIViewController	<UIViewControllerPreviewingDelegate, UISearchResultsUpdating, UISearchBarDelegate, UINavigationControllerDelegate, UISplitViewControllerDelegate>

@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, retain) RNNParentInfo* parentInfo;
@property (nonatomic) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) UIViewController* previewController;


- (instancetype)initWithParentInfo:(RNNParentInfo *)parentInfo
			 rootViewCreator:(id<RNNRootViewCreator>)creator
				eventEmitter:(RNNEventEmitter*)eventEmitter
		 isExternalComponent:(BOOL)isExternalComponent;

- (BOOL)isCustomViewController;
- (BOOL)isCustomTransitioned;
- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;
- (void)applyModalOptions;
- (void)optionsUpdated;

-(void)onButtonPress:(RNNUIBarButtonItem *)barButtonItem;

@end
