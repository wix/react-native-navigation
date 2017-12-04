#import <Foundation/Foundation.h>

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNTopBarOptions : NSObject

@property (nonatomic, strong) NSNumber* backgroundColor;
@property (nonatomic, strong) NSNumber* textColor;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* textFontFamily;
@property (nonatomic, strong) NSNumber* hidden;
@property (nonatomic, strong) NSNumber* hideOnScroll;
@property (nonatomic, strong) NSNumber* buttonColor;
@property (nonatomic, strong) NSNumber* translucent;
@property (nonatomic, strong) NSNumber* textFontSize;
@property (nonatomic, strong) NSNumber* noBorder;
@property (nonatomic, strong) NSNumber* blur;
@property (nonatomic, strong) NSNumber* animateHide;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)topBarOptions;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end
