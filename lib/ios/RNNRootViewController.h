
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNLayoutNode.h"
#import "RNNRootViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNNavigationOptions.h"
#import "RNNAnimationController.h"
@interface RNNRootViewController : UIViewController	<UINavigationControllerDelegate>
@property (nonatomic, strong) RNNNavigationOptions* navigationOptions;
<<<<<<< HEAD
@property (nonatomic, strong) RNNAnimationController* animator;
=======
@property (nonatomic, strong) RNNEventEmitter *eventEmitter;
@property (nonatomic, strong) NSString* containerId;

>>>>>>> v2
-(instancetype)initWithName:(NSString*)name
				withOptions:(RNNNavigationOptions*)options
			withContainerId:(NSString*)containerId
			rootViewCreator:(id<RNNRootViewCreator>)creator
			   eventEmitter:(RNNEventEmitter*)eventEmitter;


-(void) applyNavigationButtons;

@end
