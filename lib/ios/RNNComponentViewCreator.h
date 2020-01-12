
#import <UIKit/UIKit.h>
#import "RNNComponentOptions.h"
#import "RNNReactView.h"
#import "RNNReactButtonView.h"
#import "RNNReactTitleView.h"
#import "RNNReactBackgroundView.h"

typedef enum RNNComponentType {
    RNNComponentTypeComponent,
    RNNComponentTypeTopBarTitle,
    RNNComponentTypeTopBarButton,
    RNNComponentTypeTopBarBackground
} RNNComponentType;

@protocol RNNComponentViewCreator

- (RNNReactView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId ofType:(RNNComponentType)componentType reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@end

