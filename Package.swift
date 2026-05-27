// swift-tools-version:5.9
import Foundation
import PackageDescription

// React Native Navigation — Swift Package Manager manifest
//
// - HMSegmentedControl: vendored source target (always buildable)
// - ReactNativeNavigation: binary xcframework (published on GitHub Releases)
//
// RN host apps still use CocoaPods for React Native core until upstream SPM support lands.
// See docs/IOS_DEPENDENCY_MANAGEMENT.md and spm/BINARY_RELEASE.md

let binaryManifestPath = "spm/binary-manifest.json"

let binaryTarget: Target? = {
    guard let data = try? Data(contentsOf: URL(fileURLWithPath: binaryManifestPath)),
          let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
          let url = json["url"] as? String,
          let checksum = json["checksum"] as? String,
          !url.isEmpty,
          !checksum.isEmpty,
          checksum != "REPLACE_ON_RELEASE"
    else {
        return nil
    }
    return .binaryTarget(
        name: "ReactNativeNavigation",
        url: url,
        checksum: checksum
    )
}()

var targets: [Target] = [
    .target(
        name: "HMSegmentedControl",
        path: "ios/Vendor/HMSegmentedControl",
        sources: ["HMSegmentedControl.m"],
        publicHeadersPath: ".",
        cSettings: [
            .headerSearchPath("."),
        ],
        linkerSettings: [.linkedFramework("QuartzCore"), .linkedFramework("UIKit")]
    ),
]

var products: [Product] = [
    .library(name: "HMSegmentedControl", targets: ["HMSegmentedControl"]),
]

if let binaryTarget {
    targets.append(binaryTarget)
    products.append(.library(name: "ReactNativeNavigation", targets: ["ReactNativeNavigation"]))
}

let package = Package(
    name: "ReactNativeNavigation",
    platforms: [.iOS(.v13)],
    products: products,
    targets: targets
)
