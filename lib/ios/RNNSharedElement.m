
#import <React/RCTViewManager.h>
#import "RNNSharedElement.h"
#import "RNNSharedElementView.h"
@interface RNNSharedElement()


@end
@implementation RNNSharedElement


RCT_CUSTOM_VIEW_PROPERTY(elementId, NSString, RNNSharedElement)
{
	[(RNNSharedElementView*)view setElementId:json];
}
RCT_CUSTOM_VIEW_PROPERTY(type, NSString, RNNSharedElement)
{
	[(RNNSharedElementView*)view setType:json];
}
RCT_CUSTOM_VIEW_PROPERTY(interactive, NSNumber, RNNSharedElement)
{
	[(RNNSharedElementView*)view setInteractive:json];
}

RCT_CUSTOM_VIEW_PROPERTY(resizeMode, NSNumber, RNNSharedElement)
{
	[(RNNSharedElementView*)view setResizeMode:json];
}

RCT_EXPORT_MODULE();

- (RNNSharedElementView *)view
{
	RNNSharedElementView* sharedElement = [[RNNSharedElementView alloc] init];
	return sharedElement;
}

@end
