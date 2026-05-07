#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, ViewType) {
    ViewTypeImage,
    ViewTypeUIImage,
    ViewTypeText,
    ViewTypeOther
};

@interface UIView (Utils)

- (UIView *)findChildByClass:clazz;

- (UIView *)findDescendantByClass:clazz;

- (UIView *)findDescendantByClass:(Class)clazz passingTest:(BOOL (^)(UIView *view))test;

- (ViewType)viewType;

- (void)stopMomentumScrollViews;

- (void)setCornerRadius:(CGFloat)cornerRadius;

@end
