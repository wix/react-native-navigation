#import "RCCManager.h"
#import "RCCViewController.h"
#import <React/RCTBridge.h>
#import <React/RCTRedBox.h>
#import <React/RCTConvert.h>
#import <Foundation/Foundation.h>

@interface RCCManager() <RCTBridgeDelegate>
@property (nonatomic, strong) NSMutableDictionary *modulesRegistry;
@property (nonatomic, strong) RCTBridge *sharedBridge;
@property (nonatomic, strong) NSURL *bundleURL;
@property (nonatomic, strong, readwrite) NSDictionary *globalAppStyle;
@end

@implementation RCCManager

+ (instancetype)sharedInstance {
    static RCCManager *sharedInstance = nil;
    static dispatch_once_t onceToken = 0;
    
    dispatch_once(&onceToken,^{
        if (sharedInstance == nil) {
            sharedInstance = [[RCCManager alloc] init];
        }
    });
    
    return sharedInstance;
}

+ (instancetype)sharedIntance {
    return [RCCManager sharedInstance];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        self.modulesRegistry = [@{} mutableCopy];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onRNReload) name:RCTBridgeWillReloadNotification object:nil];
    }
    return self;
}

- (NSDictionary *)getLaunchArgs {
    NSMutableDictionary* mutableArguments = [[NSMutableDictionary alloc] init];
    NSArray* arguments = [[NSProcessInfo processInfo] arguments];
    for (int i = 0; i < [arguments count]; i+=2) {
        NSString* key = [arguments objectAtIndex:i];
        NSString* value = [arguments objectAtIndex:i+1];
        [mutableArguments setObject:value forKey:key];
    }
    
    return mutableArguments;
}

-(void)clearModuleRegistry {
    [self.modulesRegistry removeAllObjects];
}

-(void)onRNReload {
    id<UIApplicationDelegate> appDelegate = [UIApplication sharedApplication].delegate;
    appDelegate.window.rootViewController = nil;
    [self setAppStyle:nil];
    [self clearModuleRegistry];
}

-(void)registerController:(UIViewController*)controller componentId:(NSString*)componentId componentType:(NSString*)componentType {
    if (controller == nil || componentId == nil) {
        return;
    }
    
    NSMutableDictionary *componentsDic = self.modulesRegistry[componentType];
    if (componentsDic == nil) {
        componentsDic = [@{} mutableCopy];
        self.modulesRegistry[componentType] = componentsDic;
    }
    
    /*
     TODO: we really want this error, but we need to unregister controllers when they dealloc
     if (componentsDic[componentId])
     {
     [self.sharedBridge.redBox showErrorMessage:[NSString stringWithFormat:@"Controllers: controller with id %@ is already registered. Make sure all of the controller id's you use are unique.", componentId]];
     }
     */
    
    componentsDic[componentId] = controller;
}

-(void)unregisterController:(UIViewController*)vc {
    if (vc == nil) return;
    
    for (NSString *key in [self.modulesRegistry allKeys]) {
        NSMutableDictionary *componentsDic = self.modulesRegistry[key];
        for (NSString *componentID in [componentsDic allKeys]) {
            UIViewController *tmpVc = componentsDic[componentID];
            if (tmpVc == vc) {
                [componentsDic removeObjectForKey:componentID];
            }
        }
    }
}

-(id)getControllerWithId:(NSString*)componentId componentType:(NSString*)componentType {
    if (componentId == nil) {
        return nil;
    }
    
    id component = nil;
    
    NSMutableDictionary *componentsDic = self.modulesRegistry[componentType];
    if (componentsDic != nil) {
        component = componentsDic[componentId];
    }
    
    return component;
}

-(NSString*) getIdForController:(UIViewController*)vc {
    if([vc isKindOfClass:[RCCViewController class]]) {
        NSString *controllerId = ((RCCViewController*)vc).controllerId;
        if(controllerId != nil) {
            return controllerId;
        }
    }
    
    for (NSString *key in [self.modulesRegistry allKeys]) {
        NSMutableDictionary *componentsDic = self.modulesRegistry[key];
        for (NSString *componentID in [componentsDic allKeys]) {
            UIViewController *tmpVc = componentsDic[componentID];
            if (tmpVc == vc) {
                return componentID;
            }
        }
    }
    return nil;
}

-(void)initBridgeWithBundleURL:(NSURL *)bundleURL {
    [self initBridgeWithBundleURL :bundleURL launchOptions:nil];
}

-(void)initBridgeWithBundleURL:(NSURL *)bundleURL launchOptions:(NSDictionary *)launchOptions {
    if (self.sharedBridge && [self.sharedBridge isValid]) return;
    
    self.bundleURL = bundleURL;
    self.sharedBridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
    
    [[self class] showSplashScreen];
}

-(RCTBridge*)getBridge {
    return self.sharedBridge;
}

-(UIWindow*)getAppWindow {
    UIApplication *app = [UIApplication sharedApplication];
    UIWindow *window = (app.keyWindow != nil) ? app.keyWindow : app.windows[0];
    return window;
}

#pragma mark - Splash Screen

+ (void)showSplashScreen {
    UIViewController* viewControllerFromLaunchStoryboard;
    viewControllerFromLaunchStoryboard = [self viewControllerFromLaunchStoryboard];
    if (viewControllerFromLaunchStoryboard) {
        [self showSplashScreenViewController:viewControllerFromLaunchStoryboard];
        return;
    }
    
    CGRect screenBounds = [UIScreen mainScreen].bounds;
    UIViewController* viewControllerFromLaunchNib = [self viewControllerFromLaunchNibForScreenBounds:screenBounds];
    if (viewControllerFromLaunchNib) {
        [self showSplashScreenViewController:viewControllerFromLaunchNib];
        return;
    }
    
    UIViewController* viewControllerFromLaunchImage = [self viewControllerFromLaunchImageForScreenBounds:screenBounds];
    if (viewControllerFromLaunchImage) {
        [self showSplashScreenViewController:viewControllerFromLaunchImage];
        return;
    }
    
    UIViewController* viewController = [[UIViewController alloc] init];
    viewController.view.frame = screenBounds;
    viewController.view.backgroundColor = [UIColor whiteColor];
    
    [self showSplashScreenViewController:viewController];
}

+ (UIViewController *)viewControllerFromLaunchStoryboard {
    NSString* launchStoryboardName = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"UILaunchStoryboardName"];
    if (launchStoryboardName == nil) {
        return nil;
    }
    
    @try {
        UIStoryboard* storyboard = [UIStoryboard storyboardWithName:launchStoryboardName bundle:nil];
        return storyboard.instantiateInitialViewController;
    } @catch(NSException *exception) {
        return nil;
    }
}

+ (UIViewController *)viewControllerFromLaunchNibForScreenBounds:(CGRect)screenBounds {
    NSString* launchStoryboardName = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"UILaunchStoryboardName"];
    if (launchStoryboardName == nil) {
        return nil;
    }
    
    @try {
        id nibContents = [[NSBundle mainBundle] loadNibNamed:launchStoryboardName owner:self options:nil];
        if (!nibContents || [nibContents count] == 0) {
            return nil;
        }
        
        id firstObject = [nibContents firstObject];
        if (![firstObject isKindOfClass:[UIView class]]) {
            return nil;
        }
        
        UIViewController* viewController = [[UIViewController alloc] init];
        viewController.view = (UIView *)firstObject;
        viewController.view.frame = screenBounds;
        return viewController;
    } @catch(NSException *exception) {
        return nil;
    }
}

+ (UIViewController *)viewControllerFromLaunchImageForScreenBounds:(CGRect)screenBounds {
    //load the splash from the default image or from LaunchImage in the xcassets
    
    CGFloat screenHeight = screenBounds.size.height;
    
    NSString* imageName = @"Default";
    if (screenHeight == 568)
        imageName = [imageName stringByAppendingString:@"-568h"];
    else if (screenHeight == 667)
        imageName = [imageName stringByAppendingString:@"-667h"];
    else if (screenHeight == 736)
        imageName = [imageName stringByAppendingString:@"-736h"];
    
    //xcassets LaunchImage files
    UIImage *image = [UIImage imageNamed:imageName];
    if (image == nil)
    {
      imageName = @"LaunchImage";
      
      if (screenHeight == 480)
        imageName = [imageName stringByAppendingString:@"-700"];
      if (screenHeight == 568)
        imageName = [imageName stringByAppendingString:@"-700-568h"];
      else if (screenHeight == 667)
        imageName = [imageName stringByAppendingString:@"-800-667h"];
      else if (screenHeight == 736)
        imageName = [imageName stringByAppendingString:@"-800-Portrait-736h"];
      else if (screenHeight == 768)
        imageName = [imageName stringByAppendingString:@"-Landscape"]; 
      else if (screenHeight == 812)
        imageName = [imageName stringByAppendingString:@"-1100-Portrait-2436h"];
      else if (screenHeight == 1024)
        imageName = [imageName stringByAppendingString:@"-Portrait"];

      image = [UIImage imageNamed:imageName];
    }
    
    if (image == nil) {
        return nil;
    }
    
    UIViewController* viewController = [[UIViewController alloc] init];
    
    UIImageView* imageView = [[UIImageView alloc] initWithImage:image];
    viewController.view = imageView;
    viewController.view.frame = screenBounds;
    
    return viewController;
}

+ (void)showSplashScreenViewController:(UIViewController *)viewController {
    id<UIApplicationDelegate> appDelegate = [UIApplication sharedApplication].delegate;
    appDelegate.window.rootViewController = viewController;
    [appDelegate.window makeKeyAndVisible];
}

-(NSDictionary*)getAppStyle {
    return [NSDictionary dictionaryWithDictionary:self.globalAppStyle];
}

-(void)setAppStyle:(NSDictionary*)appStyle {
    self.globalAppStyle = [NSDictionary dictionaryWithDictionary:appStyle];
    [self applyAppStyle];
    
}

-(void)applyAppStyle {
    id backButtonImage = self.globalAppStyle[@"backButtonImage"];
    UIImage *image = backButtonImage ? [RCTConvert UIImage:backButtonImage] : nil;
    [[UINavigationBar appearance] setBackIndicatorImage:image];
    [[UINavigationBar appearance] setBackIndicatorTransitionMaskImage:image];
}


#pragma mark - RCTBridgeDelegate methods

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    return self.bundleURL;
}

@end
