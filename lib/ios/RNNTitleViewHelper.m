
#import "RNNTitleViewHelper.h"
#import <React/RCTConvert.h>
#import "RCTHelpers.h"

@implementation RNNTitleView


@end

@interface RNNTitleViewHelper ()

@property (nonatomic, weak) UIViewController *viewController;

@property (nonatomic, strong) NSString *title;
@property (nonatomic, strong) NSString *subtitle;
@property (nonatomic, strong) id titleImageData;
@property (nonatomic) BOOL isSetSubtitle;

@property (nonatomic, strong) RNNTitleView *titleView;

@end


@implementation RNNTitleViewHelper

- (instancetype)init:(UIViewController*)viewController
			   title:(NSString*)title subtitle:(NSString*)subtitle
	  titleImageData:(id)titleImageData
	   isSetSubtitle:(BOOL)isSetSubtitle {
	self = [super init];
	if (self) {
		self.viewController = viewController;
		if (isSetSubtitle){
			self.title = viewController.navigationItem.title;
		} else {
			self.title = [RNNTitleViewHelper validateString:title];
		}
		self.subtitle = [RNNTitleViewHelper validateString:subtitle];
		self.titleImageData = titleImageData;
	}
	return self;
}

+(NSString*)validateString:(NSString*)string {
	if ([string isEqual:[NSNull null]]) {
		return nil;
	}
	
	return string;
}

-(void)setup:(RNNTitleOptions*)style {

	CGRect navigationBarBounds = self.viewController.navigationController.navigationBar.bounds;
	
	self.titleView = [[RNNTitleView alloc] initWithFrame:navigationBarBounds];
	self.titleView.backgroundColor = [UIColor clearColor];
	self.titleView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
	self.titleView.clipsToBounds = YES;
	
	self.viewController.navigationItem.title = self.title;
	
	if ([self isTitleOnly]) {
		self.viewController.navigationItem.titleView = nil;
		return;
	}
	
	if ([self isTitleImage]) {
		[self setupTitleImage];
		return;
	}
	
	if (self.subtitle) {
		self.titleView.subtitleLabel = [self setupSubtitle:style];
	}
	
	if (self.title) {
		self.titleView.titleLabel = [self setupTitle:style];
	}
	
	[self centerTitleView:navigationBarBounds titleLabel:self.titleView.titleLabel subtitleLabel:self.titleView.subtitleLabel];
	
	self.viewController.navigationItem.titleView = self.titleView;
}


-(BOOL)isTitleOnly {
	return self.title && !self.subtitle && !self.titleImageData;
}


-(BOOL)isTitleImage {
	return self.titleImageData && ![self.titleImageData isEqual:[NSNull null]];
}


-(void)setupTitleImage {
	UIImage *titleImage = [RCTConvert UIImage:self.titleImageData];
	UIImageView *imageView = [[UIImageView alloc] initWithImage:titleImage];
	imageView.contentMode = UIViewContentModeScaleAspectFit;
	imageView.autoresizingMask = self.titleView.autoresizingMask;
	
	self.viewController.navigationItem.titleView = imageView;
}


-(void)centerTitleView:(CGRect)navigationBarBounds titleLabel:(UILabel*)titleLabel subtitleLabel:(UILabel*)subtitleLabel
{
	CGRect titleViewFrame = navigationBarBounds;
	titleViewFrame.size.width = MAX(titleLabel.frame.size.width, subtitleLabel.frame.size.width);;
	self.titleView.frame = titleViewFrame;
	
	for (UIView *view in self.titleView.subviews) {
		CGRect viewFrame = view.frame;
		viewFrame.size.width = self.titleView.frame.size.width;
		viewFrame.origin.x = (self.titleView.frame.size.width - viewFrame.size.width)/2;
		view.frame = viewFrame;
	}
	
}


-(UILabel*)setupSubtitle:(RNNTitleOptions*)style {
	CGRect subtitleFrame = self.titleView.frame;
	subtitleFrame.size.height /= 2;
	subtitleFrame.origin.y = subtitleFrame.size.height;
	
	UILabel *subtitleLabel = [[UILabel alloc] initWithFrame:subtitleFrame];
	subtitleLabel.textAlignment = NSTextAlignmentCenter;
	subtitleLabel.backgroundColor = [UIColor clearColor];
	subtitleLabel.autoresizingMask = self.titleView.autoresizingMask;
	
	[subtitleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.subtitle attributes:style.subtitle.fontAttributes]];
	
	
	CGSize labelSize = [subtitleLabel.text sizeWithAttributes:style.subtitle.fontAttributes];
	CGRect labelframe = subtitleLabel.frame;
	labelframe.size = labelSize;
	subtitleLabel.frame = labelframe;
	[subtitleLabel sizeToFit];
	
	if (style.subtitle.color) {
		UIColor *color = style.subtitle.color != (id)[NSNull null] ? [RCTConvert UIColor:style.subtitle.color] : nil;
		subtitleLabel.textColor = color;
	}
	
	[self.titleView addSubview:subtitleLabel];
	
	return subtitleLabel;
}


-(UILabel*)setupTitle:(RNNTitleOptions*)style {
	CGRect titleFrame = self.titleView.frame;
	if (self.subtitle) {
		titleFrame.size.height /= 2;
	}
	UILabel *titleLabel = [[UILabel alloc] initWithFrame:titleFrame];
	titleLabel.textAlignment = NSTextAlignmentCenter;
	titleLabel.backgroundColor = [UIColor clearColor];
	
	titleLabel.autoresizingMask = self.titleView.autoresizingMask;
	
	UIFont *titleFont = [UIFont boldSystemFontOfSize:17.f];
	
	id fontSize = style.fontSize;
	if (fontSize) {
		CGFloat fontSizeFloat = [RCTConvert CGFloat:fontSize];
		titleFont = [UIFont boldSystemFontOfSize:fontSizeFloat];
	}
	
	[titleLabel setAttributedText:[[NSAttributedString alloc] initWithString:self.title attributes:style.fontAttributes]];
	
	CGSize labelSize = [titleLabel.text sizeWithAttributes:@{NSFontAttributeName:titleFont}];
	CGRect labelframe = titleLabel.frame;
	labelframe.size = labelSize;
	titleLabel.frame = labelframe;
	
	if (!self.subtitle) {
		titleLabel.center = self.titleView.center;
	}
	
	id navBarTextColor = style.color;
	if (navBarTextColor) {
		UIColor *color = navBarTextColor != (id)[NSNull null] ? [RCTConvert UIColor:navBarTextColor] : nil;
		titleLabel.textColor = color;
	}
	
	[titleLabel sizeToFit];
	[self.titleView addSubview:titleLabel];
	
	return titleLabel;
}


@end

