#import "RNNTitleOptions.h"
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNTitleView : UIView

@property(nonatomic, strong) UILabel *titleLabel;

@property(nonatomic, strong) UILabel *subtitleLabel;

- (void)setTitleColor:(UIColor *)color;

- (void)setSubtitleColor:(UIColor *)color;

@end

@interface RNNTitleViewHelper : NSObject

@property(nonatomic, strong) RNNTitleOptions *titleOptions;
@property(nonatomic, strong) RNNSubtitleOptions *subtitleOptions;
@property(nonatomic, strong) Text *parentTestID;

- (instancetype)initWithTitleViewOptions:(RNNOptions *)titleOptions
                         subTitleOptions:(RNNOptions *)subtitleOptions
                            parentTestID:(Text *)parentTestID
                          viewController:(UIViewController *)viewController;

- (void)setup;

- (void)setTitleColor:(UIColor *)color;

- (void)setSubtitleColor:(UIColor *)color;

@end
