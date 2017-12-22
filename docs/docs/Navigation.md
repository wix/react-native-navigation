<a name="Navigation"></a>

## Navigation

* [Navigation](#Navigation)
    * [.registerContainer(containerName, getContainerFunc)](#Navigation+registerContainer)
    * [.setRoot(root)](#Navigation+setRoot)
    * [.setDefaultOptions(options)](#Navigation+setDefaultOptions)
    * [.setOptions(containerId, options)](#Navigation+setOptions)
    * [.showModal(params)](#Navigation+showModal)
    * [.dismissModal(containerId)](#Navigation+dismissModal)
    * [.dismissAllModals()](#Navigation+dismissAllModals)
    * [.push(containerId, params)](#Navigation+push)
    * [.pop(containerId, params)](#Navigation+pop)
    * [.popTo(containerId)](#Navigation+popTo)
    * [.popToRoot(containerId)](#Navigation+popToRoot)
    * [.events()](#Navigation+events)


* * *

<a name="Navigation+registerContainer"></a>

### navigation.registerContainer(containerName, getContainerFunc)
Every screen component in your app must be registered with a unique name. The component itself is a traditional React component extending React.Component.


| Param | Type | Description |
| --- | --- | --- |
| containerName | <code>string</code> | Unique container name |
| getContainerFunc | <code>function</code> | generator function, typically `() => require('./myContainer')` |


* * *

<a name="Navigation+setRoot"></a>

### navigation.setRoot(root)
Reset the navigation stack to a new screen (the stack root is changed).


| Param | Type |
| --- | --- |
| root | <code>Root</code> | 


* * *

<a name="Navigation+setDefaultOptions"></a>

### navigation.setDefaultOptions(options)
Set default options to all screens. Useful for declaring a consistent style across the app.


| Param | Type |
| --- | --- |
| options | <code>NavigationOptions</code> | 


* * *

<a name="Navigation+setOptions"></a>

### navigation.setOptions(containerId, options)
Change a containers navigation options


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>string</code> | The container's id. |
| options | <code>NavigationOptions</code> |  |


* * *

<a name="Navigation+showModal"></a>

### navigation.showModal(params)
Show a screen as a modal.


| Param | Type |
| --- | --- |
| params | <code>Object</code> | 


* * *

<a name="Navigation+dismissModal"></a>

### navigation.dismissModal(containerId)
Dismiss a modal by containerId. The dismissed modal can be anywhere in the stack.


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>String</code> | The container's id. |


* * *

<a name="Navigation+dismissAllModals"></a>

### navigation.dismissAllModals()
Dismiss all Modals


* * *

<a name="Navigation+push"></a>

### navigation.push(containerId, params)
Push a new screen into this screen's navigation stack.


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>String</code> | The container's id. |
| params | <code>\*</code> |  |


* * *

<a name="Navigation+pop"></a>

### navigation.pop(containerId, params)
Pop a container from the stack, regardless of it's position.


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>String</code> | The container's id. |
| params | <code>\*</code> |  |


* * *

<a name="Navigation+popTo"></a>

### navigation.popTo(containerId)
Pop the stack to a given container


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>String</code> | The container's id. |


* * *

<a name="Navigation+popToRoot"></a>

### navigation.popToRoot(containerId)
Pop the container's stack to root.


| Param | Type |
| --- | --- |
| containerId | <code>\*</code> | 


* * *

<a name="Navigation+events"></a>

### navigation.events()
Obtain the events registery instance

