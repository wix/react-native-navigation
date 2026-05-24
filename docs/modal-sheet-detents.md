# Modal sheet detents (iOS & Android)

Cross-platform API for `pageSheet` / `formSheet` modals with configurable snap heights.

Public API docs: [Modal options](../website/docs/api/options-modal.mdx) · [modalPresentationStyle](../website/docs/api/options-root.mdx#modalpresentationstyle)

Playground: **Navigation** tab → **Sheet detents** (`playground/src/screens/SheetModalScreen.tsx`).

E2E: `playground/e2e/SheetDetents.test.js` (Detox, iOS and Android).

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

Native: `ModalPresenter`, `ModalBottomSheetPresenter`, `ModalOptions` / `ModalSheetDetentParser`.

## iOS behavior summary

Uses `UISheetPresentationController` via `RNNModalOptions` / `RNNSheetDetentOptions`.

- `medium` and custom detents require iOS 16+
- `largestUndimmedDetent` is iOS-only

## Parity gaps

See the platform table in [options-modal.mdx](../website/docs/api/options-modal.mdx).
