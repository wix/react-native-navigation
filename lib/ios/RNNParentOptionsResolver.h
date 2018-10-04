#import <Foundation/Foundation.h>
#import "RNNLayoutProtocol.h"

@interface RNNParentOptionsResolver : NSObject

- (void)resolve:(UIViewController<RNNLayoutProtocol> *)parent with:(NSArray<UIViewController<RNNLayoutProtocol> *> *)children;

@end
