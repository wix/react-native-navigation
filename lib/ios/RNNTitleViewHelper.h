#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNTitleOptions.h"

@interface RNNTitleView : UIView

@property (nonatomic, strong) UILabel *titleLabel;

@property (nonatomic, strong) UILabel *subtitleLabel;

@end

@interface RNNTitleViewHelper : NSObject


- (instancetype)init:(UIViewController*)viewController
			   title:(NSString*)title subtitle:(NSString*)subtitle
	  titleImageData:(id)titleImageData
	   isSetSubtitle:(BOOL)isSetSubtitle;

-(void)setup:(RNNTitleOptions*)style;

@end

