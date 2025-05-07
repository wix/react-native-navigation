
#import "BottomTabsAttachModeFactory.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNExternalComponentStore.h"
#import "RNNNavigationOptions.h"
#import "RNNReactComponentRegistry.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNTurboEventEmitter.h"

@interface RNNViewControllerFactory : NSObject

- (instancetype)initWithRootViewCreator:(id<RNNComponentViewCreator>)creator
                           eventEmitter:(RNNEventEmitter *)eventEmitter
                                  store:(RNNExternalComponentStore *)store
                      componentRegistry:(RNNReactComponentRegistry *)componentRegistry
                              andBridge:(RCTBridge *)bridge
            bottomTabsAttachModeFactory:(BottomTabsAttachModeFactory *)bottomTabsAttachModeFactory;

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithRootViewCreator:(id<RNNComponentViewCreator>)creator
						   eventEmitter:(RNNEventEmitter *)eventEmitter
								  store:(RNNExternalComponentStore *)store
					  componentRegistry:(RNNReactComponentRegistry *)componentRegistry
								andHost:(RCTHost *)host
			bottomTabsAttachModeFactory:(BottomTabsAttachModeFactory *)bottomTabsAttachModeFactory;
#endif

- (UIViewController *)createLayout:(NSDictionary *)layout;

- (NSArray *)createChildrenLayout:(NSArray *)children;

#ifdef RCT_NEW_ARCH_ENABLED
@property(nonatomic, strong) RNNTurboEventEmitter *eventEmitter;
#else
@property(nonatomic, strong) RNNEventEmitter *eventEmitter;
#endif

@property(nonatomic, strong) RNNNavigationOptions *defaultOptions;

@end
