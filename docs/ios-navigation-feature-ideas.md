# iOS navigation feature ideas for RNN

Planning notes: what belongs in **react-native-navigation** vs adjacent packages, and which relatively new iOS APIs are worth exposing through RNN options.

Related internal reference: [`.cursor/skills/ios26-navigation/SKILL.md`](../.cursor/skills/ios26-navigation/SKILL.md) (iOS 26 bar/tab/sheet behavior and current RNN mapping).

---

## What RNN is for

RNN’s contract is **in-app native navigation**: stacks, tabs, modals, overlays, layout options, transitions, and native screen lifecycle (“where am I inside the app?”).

Features that live **outside** the navigation graph (lock screen, widgets, system notifications as a product surface) are generally **out of scope** for the core library.

---

## Relatively new iOS APIs that **fit** RNN scope

Items below are navigation, bars, stacks, modals, or tabs — not ambient OS UI. Status reflects the repo as of this doc (see `ios26-navigation` skill for iOS 26 detail).

### High fit (core RNN territory)




#### 2. Sheet detents and sheet chrome (`UISheetPresentationController`) — **implemented**

For `pageSheet` / form sheets: medium/large/custom detents, grabber, `largestUndimmedDetent`.

- **Today:** `modal.detents`, `modal.selectedDetent`, `modal.prefersGrabberVisible`, `modal.largestUndimmedDetent` (iOS), `modal.swipeToDismiss`. Android uses `BottomSheetBehavior` for `pageSheet` / `formSheet`; see [`website/docs/api/options-modal.mdx`](../website/docs/api/options-modal.mdx) and [`docs/modal-sheet-detents.md`](modal-sheet-detents.md). E2E: `playground/e2e/SheetDetents.test.js`.
- **Android gaps:** `largestUndimmedDetent` ignored; swipe-away does not auto-`dismissModal` (unlike iOS).
- **iOS 26:** sheets can slide under the floating tab bar; may need explicit detent/presentation context fixes (see skill §16).

#### 3. Modern tab bar model (iOS 18+, expanded in iOS 26)

`UITabBarController.tabs`, `UITab` / `UITabGroup`, `UISearchTab`, `mode = .tabSidebar`, `tabBarPlacement`, sidebar layout preferences.

- **Today:** legacy `viewControllers` API; `bottomTab.role: 'search'` (Liquid Glass search tab on iOS 26) exists, but not tab groups, sidebar mode, or top tab bar on iPad.
- **Shape:** new options on `bottomTabs` + code path in `RNNBottomTabsController`.

#### 4. Tab bar minimize on scroll (iOS 26)

`UITabBarController.tabBarMinimizeBehavior` (`.automatic`, `.never`, `.onScrollDown`, `.onScrollUp`).

- **Today:** not exposed; forced to `never` when custom tab item views are active.
- **Shape:** e.g. `bottomTabs.tabBarMinimizeBehavior`.

#### 5. Tab bar bottom accessory (iOS 26)

`UITabBarController.bottomAccessory` — persistent strip above tabs (e.g. Now Playing).

- **Today:** not exposed.
- **Shape:** e.g. `bottomTabs.accessory: { component: { name, passProps } }` → `UITabAccessory` hosting a React view.

#### 6. Top bar item grouping and prominent actions (iOS 26)

`UIBarButtonItemGroup` (fixed / movable / optional) and `UIBarButtonItem.style = .prominent`.

- **Today:** flat leading/trailing items; suboptimal Liquid Glass platter clustering on iOS 26.
- **Shape:** model action clusters in `topBar` options; build `UIBarButtonItemGroup` in native code.

#### 7. `UINavigationItem.style` — navigator / browser / editor (iOS 26)

Reshapes the navigation bar (URL-field layout, document editor with rename/subtitle/badge).

- **Today:** not exposed.
- **Shape:** e.g. `topBar.style: 'navigator' | 'browser' | 'editor'` plus editor-specific fields.

### Medium fit (navigation-related, more niche)

| API | Why it fits RNN | Gap today |
|-----|-----------------|-----------|
| **First-class `UIToolbar` on stack** | `displayMode`, scroll-edge appearance, item groups | Toolbar only indirect via stack; no `ToolbarPresenter`-style surface |
| **Split view / column upgrades** | iPad master–detail / multi-column | Basic `splitView` layout exists; not newer inspector/column styles |
| **Popover on iPhone** | Anchored presentation from bar buttons | `popover` in `modalPresentationStyle`; anchor/source rect often incomplete |
| **Deep link → screen routing** | Universal links / `NSUserActivity` → `Navigation` APIs | App responsibility; RNN can document patterns and optional helpers |
| **Peek preview modernization** | List → preview → push | `OptionsPreview` is legacy 3D Touch peek; context-menu era differs |

### Already in flight / partially implemented

Recent **in-scope** iOS 26 work (polish on existing surfaces, not new subsystems):

- Liquid Glass materials on nav bar / tab bar (`backgroundEffect = nil` pattern)
- `drawBehind` default on iOS 26 for floating tab bar
- Search bar placement: `stacked` / `integrated` / `integratedButton`
- `scrollEdgeEffect` on top bar
- `hideSharedBackground` on bar buttons (custom React buttons)
- `bottomTab.role: 'search'` (Liquid Glass search tab)
- Tab bar first-layout fixes, appearance PRs (see skill “Known iOS 26 trouble spots”)

---


## Suggested priority (if picking RNN work)

Best ROI for “feels modern” without leaving the navigation lane:

1. **Sheet detents** — every bottom-sheet modal
2. **Tab sidebar + `tabs` API** (iPad / iOS 26) — larger project, aligns with Apple’s tab/sidebar direction

Live Activities — only with explicit product need, as **sibling package + deep-link hook**, not RNN core.

---

## Implementation workflow (when adding an in-scope API)

From `ios26-navigation` skill:

1. Confirm Apple API on developer.apple.com / WWDC.
2. Pick RNN seam (`TopBarPresenter`, `RNNBottomTabsController`, `RNNModalManager`, etc.).
3. Add option in `src/interfaces/Options.ts` (iOS-only / minimum version documented).
4. Parse in matching `*Options.mm`; apply in presenter with `@available` guards.
5. Honor `UIDesignRequiresCompatibility` where Liquid Glass behavior diverges.
6. Playground screen + iOS e2e + `website/docs/api/options-*.mdx`.
7. Verify **Release** on iOS 26 sim and regress on iOS 18.

---

## References

- [Enhancing your app with fluid transitions](https://developer.apple.com/documentation/uikit/enhancing-your-app-with-fluid-transitions)
- [UISheetPresentationController](https://developer.apple.com/documentation/uikit/uisheetpresentationcontroller)
- [UITabBarController.tabs](https://developer.apple.com/documentation/uikit/uitabbarcontroller/tabs)
- [UITabAccessory](https://developer.apple.com/documentation/uikit/uitabaccessory)
