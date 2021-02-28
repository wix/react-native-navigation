#import <Foundation/Foundation.h>

@interface RNNNavigationBarDelegateHandler : NSObject

- (void)popViewControllerAnimated:(BOOL)animated;

- (BOOL)navigationController:(UINavigationController *)navigationController
               shouldPopItem:(BOOL)shouldPopItem;

- (void)navigationBar:(UINavigationBar *)navigationBar didPopItem:(UINavigationItem *)item;

@end
