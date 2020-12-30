
#import "RNNTitleViewHelper.h"
#import "RCTHelpers.h"
#import "RNNFontAttributesCreator.h"
#import <React/RCTConvert.h>

@implementation RNNTitleView

- (void)layoutSubviews {
    CGFloat heightSum = _titleLabel.frame.size.height + _subtitleLabel.frame.size.height;
    CGFloat yOffset = (self.frame.size.height - heightSum) / 2;

    [_titleLabel
        setFrame:CGRectMake(0, yOffset, self.frame.size.width, _titleLabel.frame.size.height)];
    [_subtitleLabel setFrame:CGRectMake(0, yOffset + _titleLabel.frame.size.height,
                                        self.frame.size.width, _subtitleLabel.frame.size.height)];
}

@end

@interface RNNTitleViewHelper ()

@property(nonatomic, weak) UIViewController *viewController;

@property(nonatomic, strong) RNNTitleView *titleView;

@end

@implementation RNNTitleViewHelper

- (instancetype)initWithTitleViewOptions:(RNNTitleOptions *)titleOptions
                         subTitleOptions:(RNNSubtitleOptions *)subtitleOptions
                          viewController:(UIViewController *)viewController {
    self = [super init];
    if (self) {
        self.viewController = viewController;
        self.titleOptions = titleOptions;
        self.subtitleOptions = subtitleOptions;
    }
    return self;
}

- (NSString *)title {
    return [self.titleOptions.text withDefault:nil];
}

- (NSString *)subtitle {
    return [self.subtitleOptions.text withDefault:nil];
}

+ (NSString *)validateString:(NSString *)string {
    if ([string isEqual:[NSNull null]]) {
        return nil;
    }

    return string;
}

- (void)setup {
    CGRect navigationBarBounds = self.viewController.navigationController.navigationBar.bounds;

    self.titleView = [[RNNTitleView alloc] initWithFrame:navigationBarBounds];
    self.titleView.backgroundColor = [UIColor clearColor];
    self.titleView.autoresizingMask =
        UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin |
        UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleHeight;
    self.titleView.clipsToBounds = YES;

    if (self.subtitle) {
        self.titleView.subtitleLabel = [self setupSubtitle];
    }

    if (self.title) {
        self.titleView.titleLabel = [self setupTitle];
    }

    [self centerTitleView:navigationBarBounds
               titleLabel:self.titleView.titleLabel
            subtitleLabel:self.titleView.subtitleLabel];

    self.viewController.navigationItem.titleView = self.titleView;
}

- (void)centerTitleView:(CGRect)navigationBarBounds
             titleLabel:(UILabel *)titleLabel
          subtitleLabel:(UILabel *)subtitleLabel {
    CGRect titleViewFrame = navigationBarBounds;
    titleViewFrame.size.width = MAX(titleLabel.frame.size.width, subtitleLabel.frame.size.width);
    ;
    self.titleView.frame = titleViewFrame;

    for (UIView *view in self.titleView.subviews) {
        CGRect viewFrame = view.frame;
        viewFrame.size.width = self.titleView.frame.size.width;
        viewFrame.origin.x = (self.titleView.frame.size.width - viewFrame.size.width) / 2;
        view.frame = viewFrame;
    }
}

- (UILabel *)setupSubtitle {
    CGRect subtitleFrame = self.titleView.frame;
    subtitleFrame.size.height /= 2;
    subtitleFrame.origin.y = subtitleFrame.size.height;

    UILabel *subtitleLabel = [[UILabel alloc] initWithFrame:subtitleFrame];
    subtitleLabel.textAlignment = NSTextAlignmentCenter;
    subtitleLabel.backgroundColor = [UIColor clearColor];
    subtitleLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;

    NSDictionary *fontAttributes =
        [RNNFontAttributesCreator createWithFontFamily:[_subtitleOptions.fontFamily withDefault:nil]
                                              fontSize:[_subtitleOptions.fontSize withDefault:@(12)]
                                            fontWeight:[_subtitleOptions.fontWeight withDefault:nil]
                                                 color:[_subtitleOptions.color withDefault:nil]];
    [subtitleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.subtitle
                                                                     attributes:fontAttributes]];

    CGSize labelSize = [subtitleLabel.text sizeWithAttributes:fontAttributes];
    CGRect labelframe = subtitleLabel.frame;
    labelframe.size = labelSize;
    subtitleLabel.frame = labelframe;
    [subtitleLabel sizeToFit];

    if (_subtitleOptions.color.hasValue) {
        UIColor *color = _subtitleOptions.color.get;
        subtitleLabel.textColor = color;
    }

    [self.titleView addSubview:subtitleLabel];

    return subtitleLabel;
}

- (UILabel *)setupTitle {
    CGRect titleFrame = self.titleView.frame;
    if (self.subtitle) {
        titleFrame.size.height /= 2;
    }
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:titleFrame];
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.backgroundColor = [UIColor clearColor];

    titleLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;

    NSDictionary *fontAttributes =
        [RNNFontAttributesCreator createWithFontFamily:[_titleOptions.fontFamily withDefault:nil]
                                              fontSize:[_titleOptions.fontSize withDefault:@(16)]
                                            fontWeight:[_titleOptions.fontWeight withDefault:nil]
                                                 color:[_subtitleOptions.color withDefault:nil]];
    [titleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.title
                                                                  attributes:fontAttributes]];

    CGSize labelSize = [titleLabel.text sizeWithAttributes:fontAttributes];
    CGRect labelframe = titleLabel.frame;
    labelframe.size = labelSize;
    titleLabel.frame = labelframe;
    [titleLabel sizeToFit];

    if (!self.subtitle) {
        titleLabel.center = self.titleView.center;
    }

    if (_titleOptions.color.hasValue) {
        UIColor *color = _titleOptions.color.get;
        titleLabel.textColor = color;
    }

    [self.titleView addSubview:titleLabel];

    return titleLabel;
}

@end
