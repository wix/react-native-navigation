#import "RNNCustomTitleView.h"

@interface RNNCustomTitleView ()
@property (nonatomic, strong) UIView *subView;
@property (nonatomic, strong) NSString *subViewAlign;
@end

@implementation RNNCustomTitleView


-(instancetype)initWithFrame:(CGRect)frame subView:(UIView*)subView alignment:(NSString*)alignment {
    self = [super initWithFrame:frame];
    
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        self.subView = subView;
        self.subViewAlign = alignment;
        
        subView.frame = self.bounds;
        [self addSubview:subView];
    }
    
    return self;
}


-(void)layoutSubviews {
    [super layoutSubviews];
    
    if ([self.subViewAlign isEqualToString:@"fill"]) {
        self.subView.frame = self.bounds;
    }
    else {
        
        CGFloat superViewWidth = self.superview.frame.size.width;
        CGFloat paddingLeftFromCenter = (superViewWidth/2) - self.frame.origin.x;
        CGFloat paddingRightFromCenter = self.frame.size.width - paddingLeftFromCenter;;
        CGRect reactViewFrame = self.subView.bounds;
        CGFloat minPadding = MIN(paddingLeftFromCenter, paddingRightFromCenter);
        
        reactViewFrame.size.width = minPadding*2;
        reactViewFrame.origin.x = paddingLeftFromCenter - minPadding;
        self.subView.frame = reactViewFrame;
    }
}

@end
