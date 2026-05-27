
#import "RNNComponentOptions.h"
#import "RNNReactBackgroundView.h"
#import "RNNReactButtonView.h"
#import "RNNReactView.h"
#import <UIKit/UIKit.h>

typedef enum RNNComponentType {
    RNNComponentTypeComponent,
    RNNComponentTypeTopBarTitle,
    RNNComponentTypeTopBarButton,
    RNNComponentTypeTopBarBackground,
    RNNComponentTypeBottomTabItem
} RNNComponentType;

@protocol RNNComponentViewCreator

- (RNNReactView *)createRootView:(NSString *)name
                      rootViewId:(NSString *)rootViewId
                          ofType:(RNNComponentType)componentType
             reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@end
