#import "RNNOptions.h"

@interface RNNSearchBarOptions : RNNOptions

@property(nonatomic, strong) Bool *visible;
@property(nonatomic, strong) Bool *focus;
@property(nonatomic, strong) Bool *hideOnScroll;
@property(nonatomic, strong) Bool *hideTopBarOnFocus;
@property(nonatomic, strong) Bool *obscuresBackgroundDuringPresentation;
@property(nonatomic, strong) Color *backgroundColor;
@property(nonatomic, strong) Color *tintColor;
@property(nonatomic, strong) Text *placeholder;
@property(nonatomic, strong) Text *cancelText;

@end
