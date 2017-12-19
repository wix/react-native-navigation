#import <UIKit/UIKit.h>

@interface RNNElementView : UIView

@property (nonatomic, strong) NSString* elementId;
@property (nonatomic, strong) NSString* type;
@property (nonatomic, strong) NSString* resizeMode;
@property (nonatomic, strong) NSNumber* interactive;
@property (nonatomic, strong) UIViewController* vc;
@property (nonatomic) CGPoint originalCenter;

@end
