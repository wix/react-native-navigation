#import "RNNOptions.h"
#import "RNNTitleOptions.h"

@interface RNNTopBarOptions : RNNOptions

@property (nonatomic, strong) NSArray* leftButtons;
@property (nonatomic, strong) NSArray* rightButtons;
@property (nonatomic, strong) NSNumber* backgroundColor;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSNumber* hideOnScroll;
@property (nonatomic, strong) NSNumber* buttonColor;
@property (nonatomic, strong) NSNumber* translucent;
@property (nonatomic, strong) NSNumber* transparent;
@property (nonatomic, strong) NSNumber* drawBehind;
@property (nonatomic, strong) NSNumber* noBorder;
@property (nonatomic, strong) NSNumber* blur;
@property (nonatomic, strong) NSNumber* animate;
@property (nonatomic, strong) NSNumber* largeTitle;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) RNNTitleOptions* title;

@property (nonatomic, strong) NSString* componentName;
@property (nonatomic, strong) NSString* backgroundComponentName;

@end
