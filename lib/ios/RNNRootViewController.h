#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimator.h"

typedef void (^RNNReactViewReadyCompletionBlock)(void);
typedef void (^PreviewCallback)(UIViewController *vc);

@interface RNNRootViewController : UIViewController	<UIViewControllerPreviewingDelegate, UISearchResultsUpdating, UISearchBarDelegate, UINavigationControllerDelegate, UISplitViewControllerDelegate>

@property (nonatomic, strong) RNNNavigationOptions* options;
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* componentId;
@property (nonatomic) id<RNNRootViewCreator> creator;
@property (nonatomic, strong) RNNAnimator* animator;
@property (nonatomic, strong) UIViewController* previewController;
@property (nonatomic, copy) PreviewCallback previewCallback;

- (instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withComponentId:(NSString*)componentId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter
		  isExternalComponent:(BOOL)isExternalComponent;

- (void)applyTabBarItem;
- (void)applyTopTabsOptions;
- (BOOL)isCustomViewController;
- (BOOL)isCustomTransitioned;
- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock;
- (void)mergeOptions:(RNNOptions*)options;
- (void)applyModalOptions;
- (void)optionsUpdated;

@end
