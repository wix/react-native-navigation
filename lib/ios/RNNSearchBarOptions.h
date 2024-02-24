#import "RNNOptions.h"

@interface RNNSearchBarOptions : RNNOptions

@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *focus;
@property(nonatomic, strong) RNNBool *hideOnScroll;
@property(nonatomic, strong) RNNBool *hideTopBarOnFocus;
@property(nonatomic, strong) RNNBool *obscuresBackgroundDuringPresentation;
@property(nonatomic, strong) RNNColor *backgroundColor;
@property(nonatomic, strong) RNNColor *tintColor;
@property(nonatomic, strong) RNNText *placeholder;
@property(nonatomic, strong) RNNText *cancelText;

@end
