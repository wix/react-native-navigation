---
name: ios26-navigation
description: Reference for iOS 26 navigation bar, tab bar, toolbar, and Liquid Glass building blocks (UITab, scroll-edge effects, search placement, shared-background platter, UIButton glass configurations, UIGlassEffect / UIGlassContainerEffect, UINavigationItem.style navigator/browser/editor, UICornerConfiguration / concentric corners, UISheetPresentationController, SF Symbols 7) plus the internal view hierarchy of UIBarButtonItem and UIToolbar, and how react-native-navigation maps to all of it. Use when adding or fixing iOS 26 behavior in RNN's top bar, bottom tabs, bar buttons, toolbar, modals, or any glass-styled custom chrome; when investigating iOS 26-only regressions; or when deciding how to expose a new iOS 26 capability through RNN options.
---

# iOS 26 Navigation in RNN

## When this applies

Use this skill any time work touches:
- Top bar / navigation bar appearance, buttons, titles, or search
- Bottom tab bar appearance, items, badges, or layout
- Toolbar items or bar button styling
- Any `@available(iOS 26.0, *)` branch
- iOS 26-only bugs (Liquid Glass clipping, tab bar floating, button platter, scroll-edge effects)

Pair this with `rnn-codebase` (repo navigation) and `rnn-e2e-runner` (validation).

## Build configuration

- **Always Release** for verification — see workspace rule `rnn-build-release.mdc`. Debug masks iOS 26 layout/transition issues.
- Xcode 26.1 toolchain: `DEVELOPER_DIR=/Applications/Xcode_26.1.app/Contents/Developer`
- Simulator: `iPhone 17 Pro Max` (iOS 26 default)
- Do **not** disable Fabric / New Architecture (user rule).
- Compatibility opt-out flag: `UIDesignRequiresCompatibility` in `Info.plist`. RNN reads it in `RNNReactButtonView.designRequiresCompatibility` to fall back to pre-iOS-26 layout. Always honor it in new iOS 26 code paths.

---

## Apple iOS 26 API map

### 1. Liquid Glass material
The system bar material on `UINavigationBar`, `UITabBar`, `UIToolbar` is **always translucent / glass** by default. `configureWithOpaqueBackground` is silently overlaid with glass unless you also nil out `backgroundEffect`.

- `UIBarAppearance.backgroundEffect = nil` — required to actually get an opaque/transparent fill on iOS 26.
- `UITabBarAppearance` / `UINavigationBarAppearance` / `UIToolbarAppearance` — same `backgroundEffect` rule.
- Apple HIG: [Adopt Liquid Glass](https://developer.apple.com/design/human-interface-guidelines/materials)
- WWDC25: "What's new in UIKit", "Build a UIKit app with the new design"

**RNN handling:**
- `ios/BottomTabsAppearancePresenter.mm`: branches on iOS 26 — uses `configureWithTransparentBackground` + `backgroundEffect=nil` + sets `tabBar.backgroundColor`. Pre-26 uses `configureWithOpaqueBackground`.
- `ios/TopBarAppearancePresenter.mm`: same pattern for the navigation bar.

### 2. Floating tab bar + drawBehind default
The tab bar floats above content on iOS 26 instead of pushing it up. Content underneath needs to extend.

- Tab bar no longer occupies layout space the same way; `safeAreaInsets.bottom` increases for content.
- Default `drawBehind` should be `YES` on iOS 26.

**RNN handling:**
- `ios/RNNBottomTabsOptions.mm` (`-shouldDrawBehind`): defaults `drawBehind` to `YES` on iOS 26, `NO` otherwise.
- `ios/RNNBottomTabsController.mm`: `rnn_cycleAllTabsThenRestoreInitialSelection` — iOS 26 first-layout bug where tab item titles get misplaced; cycle selection in `viewDidAppear` to force a correct layout. Gated by `_rnnDidApplyInitialTabBarSelectionFix`.

### 3. UITab / UITabBarController.tabs (iOS 18+, expanded in 26)
Modern tab API replaces `viewControllers` with `tabs: [UITab]`. Supports `UITabGroup`, `UISearchTab`, and `UITabBarController.mode = .tabSidebar`.

- `UITab(title:image:identifier:viewControllerProvider:)` — lazy VC creation per tab.
- `UITabBarController.tabs` — array of `UITab`, can mix `UITab` and `UITabGroup`.
- `UITabBarController.mode` — `.tabBar` (default) vs `.tabSidebar` (iPad sidebar).
- `UISearchTab` — first-class search tab, integrates with `UINavigationItem` search.
- `UITabBarController.sidebarLayoutPreferences` — iPad sidebar customization.
- `UITabBarController.tabBarPlacement` — `.automatic`, `.bottom`, `.top` (iPadOS 26 can pin the tab bar at the top of the window).
- Programmatic transitions between sidebar and tab bar layouts via animating `mode` changes inside `UIView.animate`.
- Apple: [UITabBarController.tabs](https://developer.apple.com/documentation/uikit/uitabbarcontroller/tabs), [UITab](https://developer.apple.com/documentation/uikit/uitab), [UITabGroup](https://developer.apple.com/documentation/uikit/uitabgroup)

**RNN handling:** RNN currently uses the legacy `viewControllers` API. There is **no** `UITab` / `tabs` adoption yet. New iOS 26 tab features (sidebar mode, search tab, groups, top placement) would need a new code path in `RNNBottomTabsController` and corresponding `Options.ts` surface.

### 4. UITabBarController.bottomAccessory
Persistent floating mini-control attached above the tab bar (e.g., Apple Music's now-playing bar).

- `UITabBarController.bottomAccessory: UITabAccessory?`
- `UITabAccessory(contentView:)` — wraps a UIView.
- Apple: [UITabAccessory](https://developer.apple.com/documentation/uikit/uitabaccessory)

**RNN handling:** Not yet exposed. Would need a new option (e.g., `bottomTabs.accessory: { component: { name, ... } }`) and a presenter to mount a React view inside a `UITabAccessory`.

### 5. Scroll-edge appearance and effects
iOS 26 makes `scrollEdgeAppearance` matter more (the bar fades to transparent when scrolled to the top). New `scrollEdgeEffect` on `UINavigationItem` controls the effect.

- `UINavigationItem.scrollEdgeEffect` — controls the fading/glass effect at scroll edges.
- `UINavigationBar.scrollEdgeAppearance` — appearance when at the scroll edge.
- `UITabBar.scrollEdgeAppearance` — same for tab bar.
- Apple: [UIScrollEdgeEffect](https://developer.apple.com/documentation/uikit/uiscrolledgeeffect) (iOS 26)

**RNN handling:**
- `scrollEdgeAppearance` exposed via top bar options — see `website/docs/api/options-scrollEdgeAppearance.mdx`.
- iOS 26 `scrollEdgeEffect` added in PR #8281 (`Add scrollEdgeEffect option`).
- Title customization on scroll edge: PRs #8207, #8239.

### 6. Search bar placement (UINavigationItemSearchBarPlacement)
iOS 26 introduces three placements:

- `UINavigationItemSearchBarPlacementStacked` — under the title, classic.
- `UINavigationItemSearchBarPlacementIntegrated` — inline with the title bar, expanded.
- `UINavigationItemSearchBarPlacementIntegratedButton` — collapsed to a button until tapped.

Property: `UINavigationItem.preferredSearchBarPlacement`. Availability-gated, not present pre-iOS-26.

**RNN handling:**
- `ios/RNNSearchBarPlacement.mm` / `.h` — JS-facing enum.
- `ios/UIViewController+RNNOptions.mm` (`setSearchBar...`): maps RNN's `SearchBarPlacementIntegrated` to integrated or integrated-button depending on `focus`. Falls through to stacked otherwise.
- PR #8211 — skip integrated placement when not available (pre-26 safety).

### 7. Shared background ("Platter") on bar button items
iOS 26 wraps every `UIBarButtonItem` in a shared Liquid Glass platter. Custom views and pre-styled `UIButton`s get double-decorated.

- `UIBarButtonItem.hidesSharedBackground: Bool` — opt out of the platter.
- Custom views need a fixed bar-item size (44pt) so the navbar reserves the slot before React mounts the content; otherwise the navbar relayouts after mount and the custom view snaps into place.

**RNN handling:**
- `ios/RNNUIBarButtonItem.mm`: defaults `hidesSharedBackground = YES` on iOS 26 for both icon buttons and custom (React) view buttons. Pins custom view to 44x44.
- `ios/RNNReactButtonView.mm`: width/height constraints + center translation post-mount to keep the React view aligned inside the reserved 44pt slot.
- New option: `RNNButtonOptions.hideSharedBackground` (also on `RNNBackButtonOptions`) — see `src/interfaces/Options.ts`.
- PR #8300 — centering fix.

### 7a. `UIBarButtonItemGroup` factory APIs and prominent style (iOS 26)

iOS 26 added direct factories on `UIBarButtonItem` that build groups inline. These are the cleanest way to make the shared platter intentional rather than relying on UIKit's implicit clustering.

```
UIBarButtonItem.creatingFixedGroup() -> UIBarButtonItemGroup
UIBarButtonItem.creatingMovableGroup(customizationIdentifier:) -> UIBarButtonItemGroup
UIBarButtonItem.creatingOptionalGroup(customizationIdentifier:isInDefaultCustomization:) -> UIBarButtonItemGroup
```

Plus a new style:

- `UIBarButtonItem.style = .prominent` — primary-action styling, tints the platter with the bar's `tintColor` at full saturation. Use sparingly (one prominent item per bar).

When grouping is intentional, UIKit renders the group under a single `_UIBarItemGroupPlatter` (see §7b/§9) instead of one platter per item. `hidesSharedBackground` still applies per item, so you can mix glassed and bare items inside one group.

**RNN handling:** Not exposed today. If a future option models a logical action cluster, build it as a `UIBarButtonItemGroup` rather than a sequence of items so the platter coalesces correctly.

References:
- [`UIBarButtonItemGroup`](https://developer.apple.com/documentation/uikit/uibarbuttonitemgroup)
- [`UIBarButtonItem.Style`](https://developer.apple.com/documentation/uikit/uibarbuttonitem/style)

### 7b. Internal view hierarchy: `UIBarButtonItem` on iOS 26

`UIBarButtonItem` is **not a `UIView`** — it's a model object. UIKit materializes it into a private view hierarchy inside the parent `UINavigationBar` or `UIToolbar`. The exact class names are private and can change; the **layering and ordering are stable** and worth knowing for layout debugging.

Approximate structure for a single bar button item (Liquid Glass, iOS 26):

```
UINavigationBar / UIToolbar
└── _UIBarBackground               (bar-wide background, Liquid Glass material)
└── _UIButtonBarStackView          (horizontal stack of items, leading or trailing group)
    └── _UIModernBarButton         (host view per item; one per UIBarButtonItem)
        ├── _UIBarItemPlatter      (shared Liquid Glass background; the "platter")
        │   └── CAFilter / glass layers (UIGlassEffect-backed)
        └── content view           (image, title label, OR customView)
```

Key facts that affect RNN code:

- **The platter is per-item by default**, but adjacent items inside the same `UIBarButtonItemGroup` (or implicit leading/trailing cluster) can be coalesced into a **single shared platter** spanning several items. This is the "shared background" that `hidesSharedBackground` opts out of.
- **`hidesSharedBackground = YES`** removes the `_UIBarItemPlatter` from the host view; the content view becomes the direct child of `_UIModernBarButton`. The host view is **still 44pt minimum**.
- **The host view (`_UIModernBarButton`) reserves the slot**, not the customView. UIKit sizes the host using:
  - System images: intrinsic size of the symbol + padding.
  - Title-only items: text width + padding.
  - **Custom views: the customView's intrinsic content size at the time the bar performs layout.** This is why RNN pins the custom React view to 44×44 in `RNNUIBarButtonItem.mm` — without explicit constraints, React mounts asynchronously, the customView's intrinsic size starts at zero, the host reserves zero width, the bar lays out, then React reports its real size and the host has to relayout. The visible artifact is the button "popping in" or being misplaced after the push transition's snapshot already captured the wrong frame.
- **Transitions snapshot the bar.** During push/pop, UIKit takes a snapshot of the navigation bar for the cross-fade between the from/to nav items. If the customView hasn't laid out before the snapshot, the snapshot shows the wrong state. This is also why a React customView can appear blank during a transition while being fine on the destination screen.
- **Tinting flows through the platter, not the customView.** `tintColor` set on the bar item or bar applies to the system image inside the host view; it does not propagate into a React customView. Color the React content explicitly.

Practical debugging:

- Inspect the live hierarchy with `po [self.navigationItem.rightBarButtonItems.firstObject valueForKey:@"_view"]` in lldb, or with View Debugger → uncheck "Show layers only" to see the private classes.
- A button that's "there but invisible" usually means the host view has 0×0 frame (sizing race) or `hidesSharedBackground = NO` with a customView that paints its own chrome (double-decoration clips/overlaps).
- A button that "snaps into place" after the screen appears means the host view's intrinsic size changed after the bar's first layout pass.

References (public API only — internals are observed, not documented):
- [`UIBarButtonItem`](https://developer.apple.com/documentation/uikit/uibarbuttonitem)
- [`UIBarButtonItem.hidesSharedBackground`](https://developer.apple.com/documentation/uikit/uibarbuttonitem/hidessharedbackground)
- [`UIBarButtonItemGroup`](https://developer.apple.com/documentation/uikit/uibarbuttonitemgroup)

### 8. Tab bar minimize behavior (iOS 26)
The tab bar can auto-minimize during scroll.

- `UITabBarController.tabBarMinimizeBehavior` — `.automatic`, `.never`, `.onScrollDown`, `.onScrollUp`.
- `setNeedsUpdateOfTabBarMinimizeBehavior()` — re-evaluate.
- Apple: [UITabBarController](https://developer.apple.com/documentation/uikit/uitabbarcontroller)

**RNN handling:** Not yet exposed. Would be a new option on `bottomTabs`.

### 9. `UIToolbar` on iOS 26

`UIToolbar` is a real `UIView` (unlike `UIBarButtonItem`). Approximate internal hierarchy on iOS 26:

```
UIToolbar
├── _UIBarBackground                  (bar-wide Liquid Glass material;
│                                      configured via UIToolbarAppearance)
│   └── _UIBackdropView / glass layers
│   └── _UIBarBackgroundShadowView    (1pt hairline; controlled by shadowImage / shadowColor)
└── _UIToolbarContentView
    └── _UIButtonBarStackView         (horizontal stack of toolbar items)
        ├── _UIModernBarButton        (item host — see 7b for layer breakdown)
        ├── _UIModernBarButton
        ├── ... fixed/flexible spaces become layout gaps in the stack ...
        └── (optional) _UIBarItemGroupPlatter
              └── two or more _UIModernBarButton sharing one platter
                  (only when items are in the same UIBarButtonItemGroup)
```

What's actually public and load-bearing on iOS 26:

- **Appearance objects are the only safe way to style.** `UIToolbarAppearance` (standard / compact / scrollEdge) drives `_UIBarBackground`. Setting `barTintColor` / `backgroundImage` on `UIToolbar` directly is **silently overridden** by Liquid Glass unless `appearance.backgroundEffect = nil`.
- **Translucency is the default.** `configureWithDefaultBackground` on iOS 26 produces glass. To get an opaque fill: `configureWithOpaqueBackground` **plus** `backgroundEffect = nil` **plus** explicit `backgroundColor`. This mirrors what `BottomTabsAppearancePresenter.mm` does for the tab bar.
- **Items follow the same Platter rules as nav bar buttons.** A custom view in a toolbar item gets wrapped in a platter unless `hidesSharedBackground = YES`. The 44pt host-view sizing race applies identically.
- **Grouping with `UIBarButtonItemGroup`** lets adjacent items render under one shared platter (visually grouped pill). `customizationIdentifier` opts the toolbar into the user-customization sheet (iPadOS).
- **`scrollEdgeAppearance` matters here too.** When a scroll view is attached, the toolbar fades to transparent at the scroll edge using `UIToolbar.scrollEdgeAppearance`. Pre-iOS-26 this was iPad-only for many cases; iOS 26 applies it consistently.

Display mode and customization (iOS 26 / iPadOS 26):

- `UIToolbar.displayMode` — `.automatic`, `.expanded`, `.compact`. Drives whether items spread out or collapse into an overflow menu.
- `UIBarButtonItem.menuRepresentation` — the menu UIKit shows when the item is hoisted into the overflow control under `.compact`.
- `UIToolbar.beginCustomizingItems(...)` / `endCustomizingItems` — programmatic entry into the user-customization sheet for movable/optional groups.

RNN handling:

RNN does not expose `UIToolbar` as a first-class options surface today. Toolbar-shaped behavior in this repo comes from the navigation controller's bottom bar via `RNNStackController` and is configured indirectly through stack options. Any new toolbar feature should:

1. Add a presenter (e.g. `ToolbarPresenter`) mirroring `TopBarPresenter`'s appearance pattern.
2. Configure via `UIToolbarAppearance` only — never direct `barTintColor` / `backgroundImage`.
3. Use `UIBarButtonItemGroup` when the option models a logical cluster (e.g. "primary actions") so the platter coalesces correctly.
4. Apply `hidesSharedBackground` defaults for React custom views, same as `RNNUIBarButtonItem.mm` does today.

References:
- [`UIToolbar`](https://developer.apple.com/documentation/uikit/uitoolbar)
- [`UIToolbarAppearance`](https://developer.apple.com/documentation/uikit/uitoolbarappearance)
- [`UIBarButtonItemGroup`](https://developer.apple.com/documentation/uikit/uibarbuttonitemgroup)
- WWDC25: "What's new in UIKit" (toolbar/group section), "Build a UIKit app with the new design"

### 10. Preferred transition
- `UINavigationController.preferredTransition` — control push/pop animation style (iOS 26 expansion).
- `UIViewController.preferredTransition` — zoom transitions.

**RNN handling:** Not exposed. Stack push/pop currently uses defaults.

### 11. SF Symbols 7
iOS 26 ships SF Symbols 7. Bar buttons that use system images get the new variants automatically. Custom symbol configurations should re-test on iOS 26.

Animated symbols are part of the iOS 26 bar story:

- `NSSymbolEffect` family — `.bounce`, `.pulse`, `.variableColor`, `.replace`, `.breathe`, `.scale`, `.appear`, `.disappear`, `.wiggle` (iOS 18+; new variants in 26).
- Apply via the bar item's image: configure `UIImageView` produced by UIKit, or use `UIView.addSymbolEffect(_:options:animated:)` on the host view returned by `UIBarButtonItem.value(forKey: "_view")` (private — prefer using `UIButton` with a configured image inside a custom bar item).
- WWDC25: "What's new in SF Symbols 7".

### 12. `UIButton` glass configurations
`UIButton.Configuration` got first-class glass factories in iOS 26. Anything RNN renders that's *not* a bar button (custom views inside a screen, overlays, modal headers, bottom accessories) should adopt these instead of hand-rolling chrome.

```
UIButton.Configuration.glass()           — translucent Liquid Glass pill
UIButton.Configuration.prominentGlass()  — tinted, more saturated variant
UIButton.Configuration.plain()
UIButton.Configuration.gray()
UIButton.Configuration.tinted()
UIButton.Configuration.filled()
UIButton.Configuration.borderless()
```

On iOS 26 the legacy configurations (`plain` / `gray` / `tinted` / `filled` / `borderless`) automatically repaint with the Liquid Glass material — you don't opt in, you opt *out* via `UIDesignRequiresCompatibility`.

Configuration knobs that affect build-up:
- `cornerStyle` — `.fixed` / `.dynamic` / `.capsule` / `.small` / `.medium` / `.large`. `.dynamic` adopts concentric corners (see §15).
- `buttonSize` — `.mini` / `.small` / `.medium` / `.large`.
- `baseBackgroundColor` / `baseForegroundColor` — tint multiplier on glass.

Approximate internal hierarchy for a configuration-based button on iOS 26:

```
UIButton (configuration-based)
├── _UIButtonBackgroundView         (the platter / glass body)
│   ├── UIVisualEffectView          (UIGlassEffect — see §13)
│   └── tint overlay (CALayer)
└── _UIButtonContentStackView
    ├── UIImageView                 (leading or trailing image)
    ├── UILabel                     (title)
    └── UILabel                     (subtitle, when set)
```

**RNN handling:** Not exposed as an option today. RNN doesn't render its own button chrome inside screens — that's the React side's job — but if RNN ever needs a system-styled control (e.g. a "Done" button on a custom modal header it builds natively), use `UIButton.Configuration.glass()` rather than custom drawing.

References:
- [`UIButton.Configuration`](https://developer.apple.com/documentation/uikit/uibutton/configuration)
- [`UIButton.Configuration.glass()`](https://developer.apple.com/documentation/uikit/uibutton/configuration/glass())

### 13. `UIGlassEffect` and `UIGlassContainerEffect` (public Liquid Glass API)

The entire Liquid Glass material is exposed publicly. This is the API to use whenever RNN needs custom glass chrome — floating overlays, bottom accessories, custom navigation extensions, modal grabbers.

```
UIGlassEffect                        — UIVisualEffect subclass
UIGlassEffect.Style.regular          — default frosted glass
UIGlassEffect.Style.clear            — thinner, more transparent variant
UIGlassEffect.tintColor              — multiplied tint
UIGlassContainerEffect               — wraps multiple glass views; they morph/merge when adjacent
UIVisualEffectView(effect: UIGlassEffect())  — host
```

Behavioral rules:
- Glass **refracts and blurs** content behind it; opacity is content-derived, not a static color.
- Two `UIVisualEffectView`s with `UIGlassEffect` placed inside the same `UIGlassContainerEffect` will **visually merge** into a single platter when they touch or overlap. This is what produces the "morphing" you see when a search bar collapses into a button, or when tabs minimize into a single pill.
- `tintColor` is **multiplied**, not painted — fully saturated colors look pastel. Use bold colors only when you want a noticeably tinted glass.
- Glass adapts to dark / light mode automatically; do not hand-set background colors on the visual effect view.

Build-up:

```
UIVisualEffectView (effect: UIGlassEffect)
├── _UIVisualEffectBackdropView      (CAFilter blur + refraction)
├── _UIVisualEffectSubview           (tint overlay)
└── contentView                      (your content; goes here, not on the effect view directly)
```

**RNN handling:** Not used yet. Any future custom chrome (e.g. a `bottomAccessory` host, a custom modal grabber, a floating action overlay) should be a `UIVisualEffectView` with `UIGlassEffect`, **not** a `UIView` with a manually configured `UIBlurEffect`. Pre-iOS-26 RNN can fall back to `UIBlurEffectStyle.systemMaterial` via the existing availability pattern.

References:
- [`UIGlassEffect`](https://developer.apple.com/documentation/uikit/uiglasseffect)
- [`UIGlassContainerEffect`](https://developer.apple.com/documentation/uikit/uiglasscontainereffect)
- [`UIVisualEffectView`](https://developer.apple.com/documentation/uikit/uivisualeffectview)
- WWDC25: "Meet Liquid Glass"

### 14. `UINavigationItem.style` — navigator / browser / editor

iOS 26 introduces three navigation item styles that reshape the entire navigation bar's internal layout. This is one of the bigger build-up changes in 26.

```
UINavigationItem.Style.navigator     — default; classic nav title + leading/trailing items
UINavigationItem.Style.browser       — URL-bar-like layout with a centered integrated field
UINavigationItem.Style.editor        — document editor; renamable title + subtitle + badge
```

Related properties (only meaningful when `style` ≠ `.navigator`):

- `UINavigationItem.documentProperties: UIDocumentProperties?` — file URL, subtitle, badge displayed in editor style.
- `UINavigationItem.renameDelegate` — inline rename UI for editor style.
- `UINavigationItem.titleMenuProvider` — context menu when tapping the title (iOS 16+, but actually visible by default in browser/editor styles on 26).

How the build-up changes:

- `.navigator` — standard `_UINavigationBarContentView` with title label centered.
- `.browser` — `_UINavigationBarContentView` swaps the title slot for an integrated URL field (`UISearchTextField`-shaped), with leading and trailing item clusters under shared platters.
- `.editor` — adds a vertical stack: title (renamable) + subtitle (file path) + badge, plus a primary action slot at the trailing edge.

**RNN handling:** Not exposed. A future RNN option could surface `topBar.style: 'navigator' | 'browser' | 'editor'`. Editor style is the more interesting one for document-shaped apps; browser style fits Wix preview-style flows.

References:
- [`UINavigationItem.Style`](https://developer.apple.com/documentation/uikit/uinavigationitem/style)
- [`UIDocumentProperties`](https://developer.apple.com/documentation/uikit/uidocumentproperties)
- WWDC25: "Build a UIKit app with the new design"

### 15. `UICornerConfiguration` and concentric corners

Concentric corners are a foundational iOS 26 design rule: a child element's corner radius should equal its parent's radius minus the inset, so curves stay visually parallel. Apple's own bar items, sheets, and buttons obey this; mixing fixed `cornerRadius` with a concentric container looks visibly wrong on 26.

```
UICornerConfiguration.uniformCornerRadius(_:)            // fixed radius
UICornerConfiguration.uniformCornerConfiguration(_:)     // with corner style (capsule, etc.)
UICornerConfiguration.concentric(minimum:)               // matches parent's curvature, floored at minimum
UIView.cornerConfiguration: UICornerConfiguration?       // applies to the view's layer
```

Rule of thumb: anything painted **inside** a glass element should be `.concentric(minimum: small)` rather than `.uniformCornerRadius(N)`. The system already uses concentric corners for bar item platters, sheet corners that morph with the device screen radius, and tab bar item shapes.

**RNN handling:** Not exposed. Any future RNN-native custom chrome that nests inside a glass element (e.g. a React custom view sitting inside an iOS 26 platter that RNN draws) should apply `.cornerConfiguration = .concentric(...)` on the wrapping UIView so it inherits the platter's curvature.

References:
- [`UICornerConfiguration`](https://developer.apple.com/documentation/uikit/uicornerconfiguration)
- [`UIView.cornerConfiguration`](https://developer.apple.com/documentation/uikit/uiview/cornerconfiguration)

### 16. `UISheetPresentationController` iOS 26 changes

RNN modals route through `UISheetPresentationController` when configured. iOS 26 changes its presentation in three meaningful ways:

- **`cornerConfiguration`** replaces `preferredCornerRadius` for finer control. Sheets can now have asymmetric corners and use `.concentric(minimum:)` to match the device's screen radius — the result is the visible "corner morph" when the sheet animates from the small detent to medium/large.
- **Default detents changed.** The small detent now overlaps the safe area more gracefully because the corner configuration animates with the detent height. Custom detents (`UISheetPresentationController.Detent.custom`) need to play nicely with this.
- **Sheets slide *under* the floating tab bar by default.** Pre-iOS-26 the tab bar pushed the sheet up; on 26 the sheet's small detent extends behind the tab bar. If a sheet must avoid covering the tab bar, opt out by configuring the detent height explicitly or by presenting from a context that excludes the tab bar.

Related: `UISheetPresentationController.prefersGrabberVisible` still controls the grabber; the grabber now uses Liquid Glass material.

**RNN handling:** RNN exposes modal presentation through stack options. iOS 26 sheet behavior surfaces automatically through UIKit defaults — but the "sheet over tab bar" change can visually regress apps that expected the pre-26 layout. If users report it, the fix is to adjust the detent or presentation context, not to fight UIKit.

References:
- [`UISheetPresentationController`](https://developer.apple.com/documentation/uikit/uisheetpresentationcontroller)
- WWDC25: "What's new in UIKit" (sheet section)

---

## Workflow: adding iOS 26 support for a new API

1. **Confirm the Apple API surface.** Read the relevant `developer.apple.com` page; check WWDC25 sessions for sample patterns.
2. **Pick the RNN seam.** Use the mapping above. Top bar → `TopBarPresenter` + `RNNTopBarOptions`. Bottom tab → `BottomTabsAppearancePresenter` / `RNNBottomTabsController` + `RNNBottomTabsOptions`. Bar buttons → `RNNUIBarButtonItem` + `RNNButtonOptions`.
3. **Add the JS option** in `lib/src/interfaces/Options.ts`. Document semantics, iOS-only marker, iOS 26 minimum.
4. **Parse it** in the matching `*Options.mm` (use the existing `[XParser parse:dict key:@"..."]` pattern). Add to `mergeOptions:`.
5. **Apply it** in the presenter, **always guarded** by `if (@available(iOS 26.0, *)) { ... }`. Honor `UIDesignRequiresCompatibility` if behavior would diverge for legacy-design apps.
6. **Add a playground screen** in `playground/src/screens/` exercising the option, both visible and toggle states.
7. **Add an e2e** in `playground/e2e/` using `device.getPlatform() === 'ios'` and an iOS version check. Follow existing baseline-then-action screenshot patterns.
8. **Update docs** in `website/docs/api/options-*.mdx`.
9. **Build Release** with Xcode 26.1, run on iPhone 17 Pro Max simulator, then **also** verify on an iOS 18 simulator for regression.

## Workflow: investigating an iOS 26 regression

1. Reproduce in Release on iOS 26 sim. Reproduce on iOS 18/17 — confirm it's 26-only.
2. Grep `if (@available(iOS 26.0, *))` and the property name (e.g. `backgroundEffect`, `hidesSharedBackground`, `preferredSearchBarPlacement`) — see if RNN already has a branch.
3. Check recent PRs touching the same file (`git log --oneline -- ios/<file>.mm | head -20`) — most iOS 26 regressions cluster.
4. Check `UIDesignRequiresCompatibility`: if the bug only happens with the new design, the fix must not break compatibility mode.
5. Confirm Fabric / New Architecture is still on (don't disable it to "fix" something).
6. Before changing code, summarize the hypothesis and proposed fix to the user (per user rule "show me first").

---

## Known iOS 26 trouble spots in this repo

| Area | File(s) | Last touched PR(s) |
|------|---------|-------------------|
| Custom React bar buttons (Platter, centering, lifecycle) | `RNNUIBarButtonItem.mm`, `RNNReactButtonView.mm` | #8300 |
| Tab item title misplaced on first layout | `RNNBottomTabsController.mm` (`rnn_cycleAllTabsThenRestoreInitialSelection`) | #8245, #8254, #8255 |
| Tab bar background color / glass | `BottomTabsAppearancePresenter.mm` | #8263, #8288 |
| Search bar placement | `UIViewController+RNNOptions.mm`, `RNNSearchBarPlacement.mm` | #8183, #8211 |
| Scroll edge effect on top bar | top bar options | #8281, #8239, #8207 |
| iPad back navigation (iOS 26) | nav controller | #8277 |
| `drawBehind` default on floating tab bar | `RNNBottomTabsOptions.mm` (`-shouldDrawBehind`) | — |

---

## Authoritative references

- HIG: [Designing for iOS 26](https://developer.apple.com/design/human-interface-guidelines/designing-for-ios), [Materials (Liquid Glass)](https://developer.apple.com/design/human-interface-guidelines/materials), [Tab bars](https://developer.apple.com/design/human-interface-guidelines/tab-bars), [Navigation bars](https://developer.apple.com/design/human-interface-guidelines/navigation-bars), [Toolbars](https://developer.apple.com/design/human-interface-guidelines/toolbars)
- API (bars and tabs): [UITabBarController](https://developer.apple.com/documentation/uikit/uitabbarcontroller), [UITab](https://developer.apple.com/documentation/uikit/uitab), [UITabAccessory](https://developer.apple.com/documentation/uikit/uitabaccessory), [UINavigationItem](https://developer.apple.com/documentation/uikit/uinavigationitem), [UINavigationItem.Style](https://developer.apple.com/documentation/uikit/uinavigationitem/style), [UIBarButtonItem.hidesSharedBackground](https://developer.apple.com/documentation/uikit/uibarbuttonitem/hidessharedbackground), [UIBarButtonItemGroup](https://developer.apple.com/documentation/uikit/uibarbuttonitemgroup), [UIScrollEdgeEffect](https://developer.apple.com/documentation/uikit/uiscrolledgeeffect), [UIBarAppearance](https://developer.apple.com/documentation/uikit/uibarappearance), [UIToolbar](https://developer.apple.com/documentation/uikit/uitoolbar)
- API (glass and shapes): [UIGlassEffect](https://developer.apple.com/documentation/uikit/uiglasseffect), [UIGlassContainerEffect](https://developer.apple.com/documentation/uikit/uiglasscontainereffect), [UICornerConfiguration](https://developer.apple.com/documentation/uikit/uicornerconfiguration), [UIButton.Configuration](https://developer.apple.com/documentation/uikit/uibutton/configuration), [UIButton.Configuration.glass()](https://developer.apple.com/documentation/uikit/uibutton/configuration/glass())
- API (presentation): [UISheetPresentationController](https://developer.apple.com/documentation/uikit/uisheetpresentationcontroller), [UIDocumentProperties](https://developer.apple.com/documentation/uikit/uidocumentproperties)
- WWDC25: "Meet Liquid Glass", "Get to know the new design system", "What's new in UIKit", "Build a UIKit app with the new design", "Elevate your tab and sidebar experience", "Make your UIKit app more flexible", "What's new in SF Symbols 7"
- Compatibility: [UIDesignRequiresCompatibility](https://developer.apple.com/documentation/bundleresources/information-property-list/uidesignrequirescompatibility)
