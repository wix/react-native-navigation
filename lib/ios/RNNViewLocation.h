#import <Foundation/Foundation.h>

@interface RNNViewLocation : NSObject

@property (nonatomic) CGRect fromFrame;
@property (nonatomic) CGRect toFrame;

- (instancetype)initWithFromElement:(UIView*)fromElement toElement:(UIView*)toElement;

@end
