#import "RNNParentOptionsResolver.h"

@implementation RNNParentOptionsResolver

- (void)resolve:(UIViewController<RNNLayoutProtocol> *)parent with:(NSArray<UIViewController<RNNLayoutProtocol> *> *)children {
	for (UIViewController<RNNLayoutProtocol>* child in parent.childViewControllers) {
		if ([parent conformsToProtocol:@protocol(RNNLayoutProtocol)] && [child conformsToProtocol:@protocol(RNNLayoutProtocol)]) {
			[parent.options mergeOptions:child.options overrideOptions:YES];
		}
	}
}

@end
