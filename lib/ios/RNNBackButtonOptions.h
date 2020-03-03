#import "RNNOptions.h"

@interface RNNBackButtonOptions : RNNOptions

@property (nonatomic, strong) Image* icon;
@property (nonatomic, strong) Text* title;
@property (nonatomic, strong) Text* fontFamily;
@property (nonatomic, strong) Number* fontSize;
@property (nonatomic, strong) Text* transition;
@property (nonatomic, strong) Text* testID;
@property (nonatomic, strong) Color* color;
@property (nonatomic, strong) Bool* showTitle;
@property (nonatomic, strong) Bool* visible;

- (BOOL)hasValue;

@end
