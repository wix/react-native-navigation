#import "RNNAnimatedViewFactory.h"
#import "RNNAnimatedImageView.h"
#import "RNNAnimatedTextView.h"
#import "RNNAnimatedUIImageView.h"
#import "UIVIew+Utils.h"

@implementation RNNAnimatedViewFactory

+ (RNNAnimatedReactView *)createFromElement:(UIView *)element
                               toElement:(UIView *)toElement
                       transitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions {
    switch (element.viewType) {
    case ViewTypeImage:
        return [[RNNAnimatedImageView alloc] initElement:element
                                            toElement:toElement
                                    transitionOptions:transitionOptions];
    case ViewTypeUIImage:
        return [[RNNAnimatedUIImageView alloc] initElement:element
                                              toElement:toElement
                                      transitionOptions:transitionOptions];
    case ViewTypeText:
        return [[RNNAnimatedTextView alloc] initElement:element
                                           toElement:toElement
                                   transitionOptions:transitionOptions];
    case ViewTypeOther:
    default:
        return [[RNNAnimatedReactView alloc] initElement:element
                                            toElement:toElement
                                    transitionOptions:transitionOptions];
    }
}

@end
