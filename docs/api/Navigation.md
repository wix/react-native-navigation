# Navigation

## Element

`Element (React.ComponentType<object>)`

---
## store

`store (Store)`

---

## registerComponent

`registerComponent(componentName: string, getComponentClassFunc: ComponentProvider): ComponentType<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L52)

Every navigation component in your app must be registered with a unique name.
The component itself is a traditional React component extending React.Component.

---

## registerComponentWithRedux

`registerComponentWithRedux(componentName: string, getComponentClassFunc: ComponentProvider, ReduxProvider: any, reduxStore: any): ComponentType<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L60)

Utility helper function like registerComponent,
wraps the provided component with a react-redux Provider with the passed redux store

---

## setRoot

`setRoot(layout: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L67)

Reset the app to a new layout

---

## setDefaultOptions

`setDefaultOptions(options: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L74)

Set default options to all screens. Useful for declaring a consistent style across the app.

---

## mergeOptions

`mergeOptions(componentId: string, options: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L81)

Change a component's navigation options

---

## showModal

`showModal(layout: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L88)

Show a screen as a modal.

---

## dismissModal

`dismissModal(componentId: string): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L95)

Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.

---

## dismissAllModals

`dismissAllModals(): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L102)

Dismiss all Modals

---

## push

`push(componentId: string, layout: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L109)

Push a new layout into this screen's navigation stack.

---

## pop

`pop(componentId: string, params: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L116)

Pop a component from the stack, regardless of it's position.

---

## popTo

`popTo(componentId: string): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L123)

Pop the stack to a given component

---

## popToRoot

`popToRoot(componentId: string): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L130)

Pop the component's stack to root.

---

## setStackRoot

`setStackRoot(componentId: string, layout: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L137)

Sets new root component to stack.

---

## showOverlay

`showOverlay(layout: any): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L144)

Show overlay on top of the entire app

---

## dismissOverlay

`dismissOverlay(componentId: string): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L151)

dismiss overlay by componentId

---

## getLaunchArgs

`getLaunchArgs(): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L158)

Resolves arguments passed on launch

---

## events

`events(): EventsRegistry`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L165)

Obtain the events registry instance

---

## constants

`constants(): Promise<any>`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/Navigation.ts#L172)

Constants coming from native

---


