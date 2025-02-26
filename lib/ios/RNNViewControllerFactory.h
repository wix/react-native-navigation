
#import "BottomTabsAttachModeFactory.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNExternalComponentStore.h"
#import "RNNNavigationOptions.h"
#import "RNNReactComponentRegistry.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

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

@property(nonatomic, strong) RNNEventEmitter *eventEmitter;

@property(nonatomic, strong) RNNNavigationOptions *defaultOptions;

@end
