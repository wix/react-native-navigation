<a name="Navigation"></a>

## Navigation

* [Navigation](#Navigation)
    * [.registerContainer(containerName, getContainerFunc)](#Navigation+registerContainer)
    * [.setRoot(root)](#Navigation+setRoot)
    * [.setOptions(containerId, options)](#Navigation+setOptions)


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

<a name="Navigation+setOptions"></a>

### navigation.setOptions(containerId, options)
Change a containers navigation options


| Param | Type | Description |
| --- | --- | --- |
| containerId | <code>string</code> | Unique container name |
| options | <code>NavigationOptions</code> |  |


* * *

