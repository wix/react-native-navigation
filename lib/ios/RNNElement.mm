
#import "RNNElement.h"
#import "RNNElementView.h"
#import <React/RCTViewManager.h>
@interface RNNElement ()

@end
@implementation RNNElement

RCT_CUSTOM_VIEW_PROPERTY(elementId, NSString, RNNElement) {
    //	[(RNNElementView*)view setElementId:json];
}

RCT_CUSTOM_VIEW_PROPERTY(resizeMode, NSString, RNNElement) {
    //	[(RNNElementView*)view. setResizeMode:json];
}

RCT_EXPORT_MODULE();

- (RNNElementView *)view {
    RNNElementView *element = [[RNNElementView alloc] init];
    return element;
}

@end
