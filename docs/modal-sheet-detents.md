# Modal sheet detents (iOS & Android)

Cross-platform API for `pageSheet` / `formSheet` modals with configurable snap heights.

Public API docs: [Modal options](../website/docs/api/options-modal.mdx) · [modalPresentationStyle](../website/docs/api/options-root.mdx#modalpresentationstyle)

Playground: **Navigation** tab → **Sheet detents** (`playground/src/screens/SheetModalScreen.tsx`).

E2E: `playground/e2e/SheetDetents.test.js` (Detox — 4 tests on Android, 3 on iOS plus 1 Android-only skipped on iOS).

## Example

```js
Navigation.showModal({
  stack: {
    children: [{
      component: {
        name: 'MySheet',
        options: {
          modalPresentationStyle: 'pageSheet',
          modal: {
            detents: ['medium', { id: 'compact', height: 220 }, 'large'],
            selectedDetent: 'medium',
            prefersGrabberVisible: true,
            swipeToDismiss: true,
            // iOS only:
            largestUndimmedDetent: 'medium',
          },
        },
      },
    }],
  },
});

// Runtime
Navigation.mergeOptions(componentId, {
  modal: { selectedDetent: 'large' },
});
```

## Android behavior summary

| Configuration | Result |
| --- | --- |
| Default modal (no `pageSheet`) | Full-screen modal |
| `pageSheet` only | `BottomSheetBehavior` wrapper, Material default height |
| `pageSheet` + `modal.detents` | Bottom sheet with mapped snap points |
| `formSheet` | Same as `pageSheet` on Android |
| `modal` options without `pageSheet` | Bottom sheet only if `detents`, `selectedDetent`, or `prefersGrabberVisible` is set — **not** `largestUndimmedDetent` alone |

Detent mapping:

- `'large'` → expanded
- `'medium'` → half-expanded (50%)
- `{ id, height }` → `peekHeight` in dp, `selectedDetent` uses the custom `id`

Native: `ModalPresenter`, `ModalBottomSheetPresenter`, `ModalOptions`, `ModalSheetDetentParser`.

Underlying content stays attached (no `componentDidDisappear` on the presenter).

## iOS behavior summary

Uses `UISheetPresentationController` via `RNNModalOptions` / `RNNSheetDetentOptions`.

- `medium` and custom detents require iOS 16+
- `largestUndimmedDetent` is iOS-only (parsed on Android but not applied)

Sheet options are applied on show and on `mergeOptions` when any sheet-related `modal` field is present in the merge payload.

## Parity gaps

| Area | iOS | Android |
| --- | --- | --- |
| `largestUndimmedDetent` | Supported | Ignored |
| `swipeToDismiss` | Dismisses modal | `isHideable` only; no auto `dismissModal` |
| Grabber | System | Custom pill, TopBar-aware positioning |
| Custom detent unit | Points | dp |

Full table: [options-modal.mdx](../website/docs/api/options-modal.mdx).
