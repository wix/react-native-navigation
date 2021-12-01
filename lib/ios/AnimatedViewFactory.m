#import "AnimatedViewFactory.h"
#import "AnimatedImageView.h"
#import "AnimatedTextView.h"
#import "AnimatedUIImageView.h"
#import "UIVIew+Utils.h"

@implementation AnimatedViewFactory

+ (AnimatedReactView *)createFromElement:(UIView *)element
                               toElement:(UIView *)toElement
                       transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    switch (element.viewType) {
    case ViewTypeImage:
        return [[AnimatedImageView alloc] initElement:element
                                            toElement:toElement
                                    transitionOptions:transitionOptions];
    case ViewTypeUIImage:
        return [[AnimatedUIImageView alloc] initElement:element
                                              toElement:toElement
                                      transitionOptions:transitionOptions];
    case ViewTypeText:
        return [[AnimatedTextView alloc] initElement:element
                                           toElement:toElement
                                   transitionOptions:transitionOptions];
    case ViewTypeOther:
    default:
        return [[AnimatedReactView alloc] initElement:element
                                            toElement:toElement
                                    transitionOptions:transitionOptions];
    }
}

@end
