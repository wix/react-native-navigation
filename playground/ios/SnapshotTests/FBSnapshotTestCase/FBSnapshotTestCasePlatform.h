/*
 *  Copyright (c) 2017-2018, Uber Technologies, Inc.
 *  Copyright (c) 2015-2018, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

#import <Foundation/Foundation.h>

#ifdef __cplusplus
extern "C" {
#endif

NS_ASSUME_NONNULL_BEGIN

/**
 An option mask that allows you to cherry pick which parts you want to include in the snapshot file name.

 - FBSnapshotTestCaseFileNameIncludeOptionNone: Don't include any of these options at all.
 - FBSnapshotTestCaseFileNameIncludeOptionDevice: The file name should include the device name, as returned by UIDevice.currentDevice.model.
 - FBSnapshotTestCaseFileNameIncludeOptionOS: The file name should include the OS version, as returned by UIDevice.currentDevice.systemVersion.
 - FBSnapshotTestCaseFileNameIncludeOptionScreenSize: The file name should include the screen size of the current device, as returned by UIScreen.mainScreen.bounds.size.
 - FBSnapshotTestCaseFileNameIncludeOptionScreenScale: The file name should include the scale of the current device, as returned by UIScreen.mainScreen.scale.
 */
typedef NS_OPTIONS(NSUInteger, FBSnapshotTestCaseFileNameIncludeOption) {
  FBSnapshotTestCaseFileNameIncludeOptionNone = 1 << 0,
  FBSnapshotTestCaseFileNameIncludeOptionDevice = 1 << 1,
  FBSnapshotTestCaseFileNameIncludeOptionOS = 1 << 2,
  FBSnapshotTestCaseFileNameIncludeOptionScreenSize = 1 << 3,
  FBSnapshotTestCaseFileNameIncludeOptionScreenScale = 1 << 4
};

/**
 Returns a Boolean value that indicates whether the snapshot test is running in 64Bit.
 This method is a convenience for creating the suffixes set based on the architecture
 that the test is running.
 
 @returns @c YES if the test is running in 64bit, otherwise @c NO.
 */
BOOL FBSnapshotTestCaseIs64Bit(void);

/**
 Returns a default set of strings that is used to append a suffix based on the architectures.
 @warning Do not modify this function, you can create your own and use it with @c FBSnapshotVerifyViewWithOptions()
 
 @returns An @c NSOrderedSet object containing strings that are appended to the reference images directory.
 */
NSOrderedSet *FBSnapshotTestCaseDefaultSuffixes(void);

/**
 Returns a fully normalized file name as per the provided option mask. Strips punctuation and spaces and replaces them with @c _.

 @param fileName The file name to normalize.
 @param option File Name Include options to use before normalization.
 @return An @c NSString object containing the passed @c fileName and optionally, with the device model and/or OS and/or screen size and/or screen scale appended at the end.
 */
NSString *FBFileNameIncludeNormalizedFileNameFromOption(NSString *fileName, FBSnapshotTestCaseFileNameIncludeOption option);

NS_ASSUME_NONNULL_END

#ifdef __cplusplus
}
#endif
