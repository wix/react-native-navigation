// iPhone Duo simulator only: folds and unfolds the device by asking SpringBoard's display tool
// service for a hinge sweep, the same request Apple's internal `suiatool swap` makes. Uses private
// SpringBoard SPI, so it's e2e tooling and never part of the library.
//
// Usage: hinge swap [-position=<closed|book|open|0.0-1.0>] [-duration=<seconds>] [-wait]
//        hinge lock | unlock
#import <Foundation/Foundation.h>
#import <dlfcn.h>
#import <objc/message.h>

// SBFDisplayToolSwapSettings is a 64-byte C struct that only SpringBoardFoundation reads.
typedef struct {
    uint64_t words[8];
} HingeSwapSettings;

typedef BOOL (*HingeSettingsFromArguments)(NSArray<NSString *> *, HingeSwapSettings *, NSError **);

int main(int argc, const char *argv[]) {
    @autoreleasepool {
        dlopen("/System/Library/PrivateFrameworks/SpringBoardFoundation.framework/"
               "SpringBoardFoundation",
               RTLD_NOW);
        dlopen(
            "/System/Library/PrivateFrameworks/SpringBoardServices.framework/SpringBoardServices",
            RTLD_NOW);
        Class serviceClass = NSClassFromString(@"SBSDisplayToolService");
        if (!serviceClass) {
            fprintf(stderr, "SBSDisplayToolService is not available on this runtime\n");
            return 2;
        }

        NSString *command = argc > 1 ? @(argv[1]) : @"swap";
        NSMutableArray<NSString *> *arguments = [NSMutableArray new];
        for (int i = 2; i < argc; i++) {
            [arguments addObject:@(argv[i])];
        }

        id service = [serviceClass new];
        dispatch_semaphore_t done = dispatch_semaphore_create(0);
        __block int status = 0;
        void (^completion)(NSError *) = ^(NSError *error) {
          if (error) {
              fprintf(stderr, "%s\n", error.description.UTF8String);
              status = 1;
          }
          dispatch_semaphore_signal(done);
        };

        if ([command isEqualToString:@"swap"]) {
            HingeSettingsFromArguments settingsFromArguments = (HingeSettingsFromArguments)dlsym(
                RTLD_DEFAULT, "SBFDisplayToolSwapSettingsFromArguments");
            HingeSwapSettings settings = {0};
            NSError *error = nil;
            if (!settingsFromArguments || !settingsFromArguments(arguments, &settings, &error)) {
                fprintf(stderr, "invalid arguments: %s\n", error.description.UTF8String ?: "");
                return 2;
            }
            ((void (*)(id, SEL, HingeSwapSettings, id))objc_msgSend)(
                service, NSSelectorFromString(@"swapDisplayWithSettings:completionHandler:"),
                settings, completion);
        } else if ([command isEqualToString:@"lock"] || [command isEqualToString:@"unlock"]) {
            NSString *selector = [command isEqualToString:@"lock"]
                                     ? @"lockForTestingWithCompletion:"
                                     : @"unlockForTestingWithCompletion:";
            ((void (*)(id, SEL, id))objc_msgSend)(service, NSSelectorFromString(selector),
                                                  completion);
        } else {
            fprintf(stderr, "unknown command: %s\n", command.UTF8String);
            return 2;
        }

        long timedOut =
            dispatch_semaphore_wait(done, dispatch_time(DISPATCH_TIME_NOW, 30 * NSEC_PER_SEC));
        ((void (*)(id, SEL))objc_msgSend)(service, NSSelectorFromString(@"invalidate"));
        if (timedOut) {
            fprintf(stderr, "timed out waiting for SpringBoard\n");
            return 3;
        }
        return status;
    }
}
