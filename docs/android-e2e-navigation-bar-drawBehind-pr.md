# Android edge-to-edge: `navigationBar.drawBehind`

## Summary

Adds `navigationBar.drawBehind` so apps on Android 15+ edge-to-edge can draw content behind the system navigation bar while keeping it visible (gesture pill or 3-button bar). Fixes incorrect bottom insets and bottom-tabs padding when the nav bar should be transparent or use a scrim overlay instead of reserving layout space.

## Problem

On API 35+ with edge-to-edge enabled, apps could not get the combination users expect:

| Desired behavior | What happened before |
| ---------------- | -------------------- |
| Transparent nav bar + gesture pill visible | System contrast scrim and/or RNN’s opaque `navBarBackgroundView` blocked transparency |
| Content extends to the bottom of the screen | Bottom layout inset was always applied for the navigation bar |
| Bottom tabs flush with screen bottom | Extra gray padding below the tab bar from `systemBars` insets |

Workarounds were limited:

- `navigationBar.visible: false` — hides the pill (not acceptable when gestures should stay visible)
- Opaque `backgroundColor` without draw-behind — content inset above a solid bar

## Solution

New option: **`navigationBar.drawBehind`** (Android only).

```js
// Transparent system nav area, pill visible, no bottom content inset
navigationBar: {
  visible: true,
  drawBehind: true,
  backgroundColor: 'transparent',
}

// Opaque scrim over content behind the nav bar, pill visible
navigationBar: {
  visible: true,
  drawBehind: true,
  backgroundColor: '#20303C',
}
```

`drawBehind: true` with `visible: true` (or omitted):

- Hides RNN’s nav-bar overlay view and sets the window navigation bar color to transparent
- Skips navigation-bar bottom inset for screen content and bottom tabs (IME inset still applies)
- Optionally paints a scrim via `backgroundColor` when not transparent

Traditional behavior is unchanged when `drawBehind` is false/omitted and `backgroundColor` is opaque: content is inset above the navigation bar.

`shouldDrawBehind()` also treats a transparent `backgroundColor` as draw-behind when `drawBehind` is not explicitly set to `false`.

## Implementation

| Area | Change |
| ---- | ------ |
| **`NavigationBarOptions`** | Parse/merge `drawBehind`; `shouldDrawBehind()`, `isDrawBehindAndVisible()` |
| **`NavigationActivity.activateEdgeToEdge()`** | `EdgeToEdge.enable()`, `setDecorFitsSystemWindows(false)`, disable nav-bar contrast enforcement, transparent window nav/status colors |
| **`SystemUiUtils`** | `setNavigationBarContrastEnforced()`; overlay hide via `hideOverlay` on `setNavigationBarBackgroundColor()`; shared `getContentBottomSystemBarInset()` / `getBottomTabsSystemBarPadding()` |
| **`Presenter`** | Apply transparent vs scrim vs traditional nav bar; refresh insets on merge |
| **`ComponentViewController`** | Skip nav-bar bottom inset when draw-behind + visible under E2E |
| **`BottomTabsController`** | Skip bottom `systemBars` padding when draw-behind + visible; refresh insets on child nav-bar option changes |
| **`BottomTabsPresenter`** | Skip syncing tab color to nav bar when `shouldDrawBehind()` |
| **Types / docs** | `Options.ts`, `options-navigationBar.mdx`, `style-edge-to-edge.mdx` |

## API

```ts
export interface NavigationBarOptions {
  backgroundColor?: Color;
  visible?: boolean;
  drawBehind?: boolean;
}
```

See [Navigation Bar options](https://github.com/wix/react-native-navigation/blob/master/website/docs/api/options-navigationBar.mdx) and [Edge-to-Edge guide](https://github.com/wix/react-native-navigation/blob/master/website/docs/docs/style-edge-to-edge.mdx).

## App setup (consumers)

Edge-to-edge must be enabled in the host app:

1. Theme: `android:windowOptOutEdgeToEdgeEnforcement` = `false` (API 35+)
2. Override `NavigationActivity.enableEdgeToEdge()` and call `activateEdgeToEdge()` when appropriate

Wix Engine apps still need a separate change: flip per-brand opt-out and remove legacy `window.statusBarColor` usage in `MainActivity` — not included in this PR.

## Limitations

- **Cannot hide only the gesture pill** while keeping the nav bar “visible” — Android does not expose that. Use `visible: false` to hide the entire system navigation bar (pill included).
- **`navigationBar` is Android-only** — no iOS equivalent (use `bottomTabs.drawBehind` / `bottomTabs.visible` on iOS).
- **Bottom tabs + draw-behind**: tab bar content does not extend behind the system nav bar; only the extra gray gap from incorrect insets is removed.

## Test plan

- [x] `PresenterTest.applyNavigationBarDrawBehind_usesTransparentOverlay`
- [x] Manual repro on API 35 emulator, gesture navigation (`navigation_mode=2`):
  - **A** — `drawBehind: true`, `backgroundColor: 'transparent'`, `visible: true` → transparent nav area, pill visible
  - **B** — `visible: false` → pill gone
  - **C** — opaque color, no `drawBehind` → black bar, content inset above
  - **D** — `drawBehind: true`, opaque top-bar color → scrim + pill, content behind
- [ ] CI Android unit tests
- [ ] Regression: non-E2E apps (`windowOptOutEdgeToEdgeEnforcement` default / opt-out `true`) unchanged

## Breaking changes

None. New option only; default behavior matches previous releases when `drawBehind` is not set.
