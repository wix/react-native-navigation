# Element

## context

`context (any)`

---
## props

`props (Readonly<object> & Readonly<object>)`

---
## refs

`refs (object)`

---
## state

`state (Readonly<any>)`

---

## render

`render(): Element`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src/adapters/Element.tsx#L17)

---

## setState

`setState(state: function | S | object, callback: function): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L287)

---

## forceUpdate

`forceUpdate(callBack: function): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L292)

---

## componentDidMount

`componentDidMount(): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L376)

Called immediately after a compoment is mounted. Setting state here will trigger re-rendering.

---

## shouldComponentUpdate

`shouldComponentUpdate(nextProps: Readonly<object>, nextState: Readonly<any>, nextContext: any): boolean`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L387)

Called to determine whether the change in props and state should trigger a re-render.

---

## componentWillUnmount

`componentWillUnmount(): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L392)

Called immediately before a component is destroyed. Perform any necessary cleanup in this method, such as
cancelled network requests, or cleaning up any DOM elements created in `componentDidMount`.

---

## componentDidCatch

`componentDidCatch(error: Error, errorInfo: ErrorInfo): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L397)

Catches exceptions generated in descendant components. Unhandled exceptions will cause
the entire component tree to unmount.

---

## getSnapshotBeforeUpdate

`getSnapshotBeforeUpdate(prevProps: Readonly<object>, prevState: Readonly<any>): SS | null`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L423)

Runs before React applies the result of `render` to the document, and
returns an object to be given to componentDidUpdate. Useful for saving
things such as scroll position before `render` causes changes to it.

---

## componentDidUpdate

`componentDidUpdate(prevProps: Readonly<object>, prevState: Readonly<any>, snapshot: SS): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L429)

Called immediately after updating occurs. Not called for the initial render.

---

## componentWillMount

`componentWillMount(): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L444)

Called immediately before mounting occurs, and before `Component#render`.
Avoid introducing any side-effects or subscriptions in this method.

---

## UNSAFE_componentWillMount

`UNSAFE_componentWillMount(): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L458)

Called immediately before mounting occurs, and before `Component#render`.
Avoid introducing any side-effects or subscriptions in this method.

---

## componentWillReceiveProps

`componentWillReceiveProps(nextProps: Readonly<object>, nextContext: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L473)

Called when the component may be receiving new props.
React may call this even if props have not changed, so be sure to compare new and existing
props if you only want to handle changes.

---

## UNSAFE_componentWillReceiveProps

`UNSAFE_componentWillReceiveProps(nextProps: Readonly<object>, nextContext: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L490)

Called when the component may be receiving new props.
React may call this even if props have not changed, so be sure to compare new and existing
props if you only want to handle changes.

---

## componentWillUpdate

`componentWillUpdate(nextProps: Readonly<object>, nextState: Readonly<any>, nextContext: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L503)

Called immediately before rendering when new props or state is received. Not called for the initial render.

---

## UNSAFE_componentWillUpdate

`UNSAFE_componentWillUpdate(nextProps: Readonly<object>, nextState: Readonly<any>, nextContext: any): void`

[source](https://github.com/wix/react-native-navigation/blob/v2/lib/src//Users/danielzlotin/dev/react-native-navigation/node_modules/@types/react/index.d.ts#L518)

Called immediately before rendering when new props or state is received. Not called for the initial render.

---


