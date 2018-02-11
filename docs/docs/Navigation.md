<a name="Navigation"></a>

# Navigation

* [Navigation](#Navigation)
    * [.registerComponent(componentName, getComponentFunc)](#Navigation+registerComponent)
    * [.setRoot(root)](#Navigation+setRoot)
    * [.setDefaultOptions(options)](#Navigation+setDefaultOptions)
    * [.setOptions(componentId, options)](#Navigation+setOptions)
    * [.showModal(params)](#Navigation+showModal)
    * [.dismissModal(componentId)](#Navigation+dismissModal)
    * [.dismissAllModals()](#Navigation+dismissAllModals)
    * [.push(componentId, component)](#Navigation+push)
    * [.pop(componentId, params)](#Navigation+pop)
    * [.popTo(componentId)](#Navigation+popTo)
    * [.popToRoot(componentId)](#Navigation+popToRoot)
    * [.events()](#Navigation+events)


* * *

<a name="Navigation+registerComponent"></a>

## navigation.registerComponent(componentName, getComponentFunc)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.


| Param | Type | Description |
| --- | --- | --- |
| componentName | <code>string</code> | Unique component name |
| getComponentFunc | <code>function</code> | generator function, typically `() => require('./myComponent')` |


* * *

<a name="Navigation+setRoot"></a>

## navigation.setRoot(root)
Reset the navigation stack to a new screen (the stack root is changed).


| Param | Type |
| --- | --- |
| root | <a href="https://wix.github.io/react-native-navigation/v2/#/docs/Root">Root</a> |


* * *

<a name="Navigation+setDefaultOptions"></a>

## navigation.setDefaultOptions(options)
Set default options to all screens. Useful for declaring a consistent style across the app.


| Param | Type |
| --- | --- |
| options | <a href="https://wix.github.io/react-native-navigation/v2/#/docs/options/NavigationOptions">NavigationOptions</a> |


* * *

<a name="Navigation+setOptions"></a>

## navigation.setOptions(componentId, options)
Change a components navigation options


| Param | Type | Description |
| --- | --- | --- |
| componentId | <code>string</code> | The component's id. |
| options | <a href="https://wix.github.io/react-native-navigation/v2/#/docs/options/NavigationOptions">NavigationOptions</a> |  |


* * *

<a name="Navigation+showModal"></a>

## navigation.showModal(params)
Show a screen as a modal.


| Param | Type |
| --- | --- |
| params | <code>object</code> |


* * *

<a name="Navigation+dismissModal"></a>

## navigation.dismissModal(componentId)
Dismiss a modal by componentId. The dismissed modal can be anywhere in the stack.


| Param | Type | Description |
| --- | --- | --- |
| componentId | <code>string</code> | The component's id. |


* * *

<a name="Navigation+dismissAllModals"></a>

## navigation.dismissAllModals()
Dismiss all Modals


* * *

<a name="Navigation+push"></a>

## navigation.push(componentId, component)
Push a new screen into this screen's navigation stack.


| Param | Type | Description |
| --- | --- | --- |
| componentId | <code>string</code> | The component's id. |
| component | <a href="https://wix.github.io/react-native-navigation/v2/#/docs/Component">Component</a> |  |


* * *

<a name="Navigation+pop"></a>

## navigation.pop(componentId, params)
Pop a component from the stack, regardless of it's position.


| Param | Type | Description |
| --- | --- | --- |
| componentId | <code>string</code> | The component's id. |
| params | <code>*</code> |  |


* * *

<a name="Navigation+popTo"></a>

## navigation.popTo(componentId)
Pop the stack to a given component


| Param | Type | Description |
| --- | --- | --- |
| componentId | <code>string</code> | The component's id. |


* * *

<a name="Navigation+popToRoot"></a>

## navigation.popToRoot(componentId)
Pop the component's stack to root.


| Param | Type |
| --- | --- |
| componentId | <code>*</code> |


* * *

<a name="Navigation+events"></a>

## navigation.events()
Obtain the events registery instance
