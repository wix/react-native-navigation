/*
 *  Copyright (c) 2017-2018, Uber Technologies, Inc.
 *  Copyright (c) 2015-2018, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "FBSnapshotTestCasePlatform.h"

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, FBSnapshotTestControllerErrorCode) {
    FBSnapshotTestControllerErrorCodeUnknown,
    FBSnapshotTestControllerErrorCodeNeedsRecord,
    FBSnapshotTestControllerErrorCodePNGCreationFailed,
    FBSnapshotTestControllerErrorCodeImagesDifferentSizes,
    FBSnapshotTestControllerErrorCodeImagesDifferent,
};

/**
 Errors returned by the methods of FBSnapshotTestController use this domain.
 */
extern NSString *const FBSnapshotTestControllerErrorDomain;

/**
 Errors returned by the methods of FBSnapshotTestController sometimes contain this key in the
 `userInfo` dictionary.
 */
extern NSString *const FBReferenceImageFilePathKey;

/**
 Errors returned by the methods of FBSnapshotTestController sometimes contain this key in the
 `userInfo` dictionary.
 */
extern NSString *const FBReferenceImageKey;

/**
 Errors returned by the methods of FBSnapshotTestController sometimes contain this key in the
 `userInfo` dictionary.
 */
extern NSString *const FBCapturedImageKey;

/**
 Errors returned by the methods of FBSnapshotTestController sometimes contain this key in the
 `userInfo` dictionary.
 */
extern NSString *const FBDiffedImageKey;

/**
 Provides the heavy-lifting for FBSnapshotTestCase. It loads and saves images, along with performing
 the actual pixel- by-pixel comparison of images. Instances are initialized with the test class, and
 directories to read and write to.
 */
@interface FBSnapshotTestController : NSObject

/**
 Record snapshots.
 */
@property(readwrite, nonatomic, assign) BOOL recordMode;

/**
 When set, allows fine-grained control over what you want the file names to include.

 Allows you to combine which device or simulator specific details you want in your snapshot file
 names.

 The default value is FBSnapshotTestCaseFileNameIncludeOptionScreenScale.

 @discussion If you are migrating from the now deleted FBSnapshotTestCaseAgnosticOption to
 FBSnapshotTestCaseFileNameIncludeOption, we default to using
 FBSnapshotTestCaseFileNameIncludeOptionScreenScale for fileNameOptions to make the transition
 easier. If you don't want to have the screen scale included in your file name, you need to set
 fileNameOptions to a mask that doesn't include FBSnapshotTestCaseFileNameIncludeOptionScreenScale:

 self.fileNameOptions = (FBSnapshotTestCaseFileNameIncludeOptionDevice |
 FBSnapshotTestCaseFileNameIncludeOptionOS);
 */
@property(readwrite, nonatomic, assign) FBSnapshotTestCaseFileNameIncludeOption fileNameOptions;

/**
 Uses drawViewHierarchyInRect:afterScreenUpdates: to draw the image instead of renderInContext:
 */
@property(readwrite, nonatomic, assign) BOOL usesDrawViewHierarchyInRect;

/**
 The directory in which reference images are stored.
 */
@property(readwrite, nonatomic, copy, nullable) NSString *referenceImagesDirectory;

/**
 The directory in which failed snapshot images are stored.
 */
@property(readwrite, nonatomic, copy) NSString *imageDiffDirectory;

/**
 The name folder in which the snapshots will be saved for a given test case.
*/
@property(readwrite, nonatomic, copy) NSString *folderName;

/**
 @param testClass The subclass of FBSnapshotTestCase that is using this controller.
 @returns An instance of FBSnapshotTestController.
 */
- (instancetype)initWithTestClass:(Class)testClass;

/**
 Performs the comparison of the layer.
 @param layer The Layer to snapshot.
 @param selector The test method being run.
 @param identifier An optional identifier, used is there are muliptle snapshot tests in a given
 -test method.
 @param errorPtr An error to log in an XCTAssert() macro if the method fails (missing reference
 image, images differ, etc).
 @returns YES if the comparison (or saving of the reference image) succeeded.
 */
- (BOOL)compareSnapshotOfLayer:(CALayer *)layer
                      selector:(SEL)selector
                    identifier:(nullable NSString *)identifier
                         error:(NSError **)errorPtr;

/**
 Performs the comparison of the view.
 @param view The view to snapshot.
 @param selector The test method being run.
 @param identifier An optional identifier, used is there are muliptle snapshot tests in a given
 -test method.
 @param errorPtr An error to log in an XCTAssert() macro if the method fails (missing reference
 image, images differ, etc).
 @returns YES if the comparison (or saving of the reference image) succeeded.
 */
- (BOOL)compareSnapshotOfView:(UIView *)view
                     selector:(SEL)selector
                   identifier:(nullable NSString *)identifier
                        error:(NSError **)errorPtr;

/**
 Performs the comparison of a view or layer.
 @param viewOrLayer The view or layer to snapshot.
 @param selector The test method being run.
 @param identifier An optional identifier, used is there are muliptle snapshot tests in a given
 -test method.
 @param overallTolerance The percentage of pixels that can differ and still be considered
 'identical'.
 @param errorPtr An error to log in an XCTAssert() macro if the method fails (missing reference
 image, images differ, etc).
 @returns YES if the comparison (or saving of the reference image) succeeded.
 */
- (BOOL)compareSnapshotOfViewOrLayer:(id)viewOrLayer
                            selector:(SEL)selector
                          identifier:(nullable NSString *)identifier
                    overallTolerance:(CGFloat)overallTolerance
                               error:(NSError **)errorPtr;

/**
 Performs the comparison of a view or layer.
 @param viewOrLayer The view or layer to snapshot.
 @param selector The test method being run.
 @param identifier An optional identifier, used is there are muliptle snapshot tests in a given
 -test method.
 @param perPixelTolerance The percentage a given pixel's R,G,B and A components can differ and still
 be considered 'identical'.
 @param overallTolerance The percentage of pixels that can differ and still be considered
 'identical'.
 @param errorPtr An error to log in an XCTAssert() macro if the method fails (missing reference
 image, images differ, etc).
 @returns YES if the comparison (or saving of the reference image) succeeded.
 */
- (BOOL)compareSnapshotOfViewOrLayer:(id)viewOrLayer
                            selector:(SEL)selector
                          identifier:(nullable NSString *)identifier
                   perPixelTolerance:(CGFloat)perPixelTolerance
                    overallTolerance:(CGFloat)overallTolerance
                               error:(NSError **)errorPtr;

/**
 Loads a reference image.
 @param selector The test method being run.
 @param identifier The optional identifier, used when multiple images are tested in a single -test
 method.
 @param errorPtr An error, if this methods returns nil, the error will be something useful.
 @returns An image.
 */
- (nullable UIImage *)referenceImageForSelector:(SEL)selector
                                     identifier:(nullable NSString *)identifier
                                          error:(NSError **)errorPtr;

/**
 Performs a pixel-by-pixel comparison of the two images with an allowable margin of error.
 @param referenceImage The reference (correct) image.
 @param image The image to test against the reference.
 @param overallTolerance The percentage of pixels that can differ and still be considered
 'identical'.
 @param errorPtr An error that indicates why the comparison failed if it does.
 @returns YES if the comparison succeeded and the images are the same(ish).
 */
- (BOOL)compareReferenceImage:(UIImage *)referenceImage
                      toImage:(UIImage *)image
             overallTolerance:(CGFloat)overallTolerance
                        error:(NSError **)errorPtr;

/**
 Performs a pixel-by-pixel comparison of the two images with an allowable margin of error.
 @param referenceImage The reference (correct) image.
 @param image The image to test against the reference.
 @param perPixelTolerance The percentage a given pixel's R,G,B and A components can differ and still
 be considered 'identical'.
 @param overallTolerance The percentage of pixels that can differ and still be considered
 'identical'.
 @param errorPtr An error that indicates why the comparison failed if it does.
 @returns YES if the comparison succeeded and the images are the same(ish).
 */
- (BOOL)compareReferenceImage:(UIImage *)referenceImage
                      toImage:(UIImage *)image
            perPixelTolerance:(CGFloat)perPixelTolerance
             overallTolerance:(CGFloat)overallTolerance
                        error:(NSError **)errorPtr;

/**
 Saves the reference image and the test image to `failedOutputDirectory`.
 @param referenceImage The reference (correct) image.
 @param testImage The image to test against the reference.
 @param selector The test method being run.
 @param identifier The optional identifier, used when multiple images are tested in a single -test
 method.
 @param errorPtr An error that indicates why the comparison failed if it does.
 @returns YES if the save succeeded.
 */
- (BOOL)saveFailedReferenceImage:(UIImage *)referenceImage
                       testImage:(UIImage *)testImage
                        selector:(SEL)selector
                      identifier:(nullable NSString *)identifier
                           error:(NSError **)errorPtr;
@end

NS_ASSUME_NONNULL_END
