(window.webpackJsonp=window.webpackJsonp||[]).push([[539],{612:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return r})),t.d(n,"metadata",(function(){return s})),t.d(n,"toc",(function(){return c})),t.d(n,"default",(function(){return u}));var a=t(3),o=(t(0),t(627)),i=t(633);const r={id:"basic-navigation",title:"Basic navigation",sidebar_label:"Basic navigation"},s={unversionedId:"docs/basic-navigation",id:"version-7.32.1/docs/basic-navigation",isDocsHomePage:!1,title:"Basic navigation",description:"Generally, any mobile app consists of various destinations which display some content to the user. And in vast majority of cases using an app means navigating between these destinations. React-native-navigation provides ways for you to layout your content on user screen in a logical and performant manner.",source:"@site/versioned_docs/version-7.32.1/docs/docs-basic-navigation.mdx",slug:"/docs/basic-navigation",permalink:"/react-native-navigation/7.32.1/docs/basic-navigation",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.32.1/docs/docs-basic-navigation.mdx",version:"7.32.1",sidebar_label:"Basic navigation",sidebar:"version-7.32.1/docs",previous:{title:"Launching the app",permalink:"/react-native-navigation/7.32.1/docs/app-launch"},next:{title:"Advanced navigation",permalink:"/react-native-navigation/7.32.1/docs/advanced-navigation"}},c=[{value:"Creating a stack",id:"creating-a-stack",children:[]},{value:"Specifying options",id:"specifying-options",children:[]},{value:"Navigating in a stack",id:"navigating-in-a-stack",children:[]},{value:"App theme",id:"app-theme",children:[]},{value:"Summary",id:"summary",children:[]}],l={toc:c},p="wrapper";function u(e){let{components:n,...t}=e;return Object(o.b)(p,Object(a.a)({},l,t,{components:n,mdxType:"MDXLayout"}),Object(o.b)("p",null,"Generally, any mobile app consists of various destinations which display some content to the user. And in vast majority of cases using an app means navigating between these destinations. React-native-navigation provides ways for you to layout your content on user screen in a logical and performant manner."),Object(o.b)("p",null,"React Native Navigation's stack layout lets you push screens, and also navigate back to previous screens. Screens pushed into the stack hide the previous screen in the stack, making the user focus on a single screen at a time. Let's look at a stack layout that provides basic navigation for an app."),Object(o.b)("h2",{id:"creating-a-stack"},"Creating a stack"),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Home Screen</Text>\n    </View>\n  );\n};\nNavigation.registerComponent('Home', () => HomeScreen);\n\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(o.b)("p",null,"Running this code will render a screen with an empty TopBar and your HomeScreen component (shown below). The TopBar is part of the stack layout, we'll work on configuring it later."),Object(o.b)("img",{width:"40%",src:Object(i.a)("img/stack1.png")}),Object(o.b)("h2",{id:"specifying-options"},"Specifying options"),Object(o.b)("p",null,"You can specify options of each layout (Stack, component pushed into a stack, etc.) to configure various parameters like the TopBar title or color. Lets spice things up by changing the TopBar color and while we're at it, also move the title to the TopBar."),Object(o.b)("p",null,"Lets change the Home screen declaration to the following and reload the app to see the changes."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-jsx"},"const HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n}\n")),Object(o.b)("p",null,"Our app should now look like this:"),Object(o.b)("img",{width:"40%",src:Object(i.a)("img/stackOptions.png")}),Object(o.b)("h2",{id:"navigating-in-a-stack"},"Navigating in a stack"),Object(o.b)("p",null,"In the previous section we created a stack and initialized it with a single child. We'll now learn how to add another child to the stack. From now on, we'll call the action of adding children to the stack 'push', and removing children - 'pop'."),Object(o.b)("p",null,"In order to push another screen to the stack, we will add a button to the home screen and call ",Object(o.b)("inlineCode",{parentName:"p"},"Navigation.push"),". The 'push' command accepts two parameters, the first is the id used to indicate into which stack to push the screen and the second is the screen we're pushing. After pushing a screen, a back button is added automatically to the TopBar so the users can navigate easily back to the previous screen."),Object(o.b)("p",null,"You can read more about the stack layout ",Object(o.b)("a",{parentName:"p",href:"/react-native-navigation/7.32.1/docs/stack"},"here")," or dive right into the API ",Object(o.b)("a",{parentName:"p",href:"../api/layout-stack"},"here"),"."),Object(o.b)("div",{className:"admonition admonition-info alert alert--info"},Object(o.b)("div",{parentName:"div",className:"admonition-heading"},Object(o.b)("h5",{parentName:"div"},Object(o.b)("span",{parentName:"h5",className:"admonition-icon"},Object(o.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(o.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"componentId")),Object(o.b)("div",{parentName:"div",className:"admonition-content"},Object(o.b)("p",{parentName:"div"},"Each React component registered with ",Object(o.b)("inlineCode",{parentName:"p"},"Navigation.registerComponent")," is assigned a unique ",Object(o.b)("inlineCode",{parentName:"p"},"componentId")," by Navigation. This unique id is used with Navigation commands (like the push command) to indicate into which stack we'd like to push. In this case, by using the componentId of the Home screen, we are telling Navigation to push into the stack containing the Home screen."))),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.push(props.componentId, {\n  component: {\n    name: 'Settings', // Push the screen registered with the 'Settings' key\n    options: { // Optional options object to configure the screen\n      topBar: {\n        title: {\n          text: 'Settings' // Set the TopBar title of the new Screen\n        }\n      }\n    }\n  }\n});\n")),Object(o.b)("p",null,"Lets change our code to the following snippet below and reload the app. Now our Home should contain a button which when pressed, pushes a new screen into the stack. We've successfully navigated to a new screen! \ud83d\udc4f"),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\n// Home screen declaration\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n      <Button\n        title='Push Settings Screen'\n        color='#710ce3'\n        onPress={() => Navigation.push(props.componentId, {\n          component: {\n            name: 'Settings',\n            options: {\n              topBar: {\n                title: {\n                  text: 'Settings'\n                }\n              }\n            }\n          }\n        })}/>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n};\n\n// Settings screen declaration - this is the screen we'll be pushing into the stack\nconst SettingsScreen = () => {\n  return (\n    <View style={styles.root}>\n      <Text>Settings Screen</Text>\n    </View>\n  );\n}\n\nNavigation.registerComponent('Home', () => HomeScreen);\nNavigation.registerComponent('Settings', () => SettingsScreen);\n\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(o.b)("img",{width:"40%",src:Object(i.a)("img/stack2.gif")}),Object(o.b)("h2",{id:"app-theme"},"App theme"),Object(o.b)("p",null,"Our app is growing and already contains two screens. This introduces a new problem - while the home screen has a nice purple TopBar, our settings screen seems pretty dull as it still uses the default system look and feel."),Object(o.b)("p",null,"Currently, our style declaration applies only to the Home screen, so lets apply it to all of our screens by using ",Object(o.b)("inlineCode",{parentName:"p"},"Navigation.setDefaultOptions"),"."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setDefaultOptions({\n  statusBar: {\n    backgroundColor: '#4d089a'\n  },\n  topBar: {\n    title: {\n      color: 'white'\n    },\n    backButton: {\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n});\n")),Object(o.b)("p",null,"We need to add this snippet before registering the ",Object(o.b)("inlineCode",{parentName:"p"},"registerAppLaunchedListener"),". This way we ensure our theme is applied when our root is set. Our code should now look like this:"),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\n// Home screen declaration\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n      <Button\n        title='Push Settings Screen'\n        color='#710ce3'\n        onPress={() => Navigation.push(props.componentId, {\n          component: {\n            name: 'Settings',\n            options: {\n              topBar: {\n                title: {\n                  text: 'Settings'\n                }\n              }\n            }\n          }\n        })}/>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n    }\n  }\n};\n\n// Settings screen declaration - this is the screen we'll be pushing into the stack\nconst SettingsScreen = () => {\n  return (\n    <View style={styles.root}>\n      <Text>Settings Screen</Text>\n    </View>\n  );\n}\n\nNavigation.registerComponent('Home', () => HomeScreen);\nNavigation.registerComponent('Settings', () => SettingsScreen);\n\n\nNavigation.setDefaultOptions({\n  statusBar: {\n    backgroundColor: '#4d089a'\n  },\n  topBar: {\n    title: {\n      color: 'white'\n    },\n    backButton: {\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n});\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(o.b)("p",null,"Lets run our code again - now our design is consistent across both screens."),Object(o.b)("img",{width:"40%",src:Object(i.a)("img/stack3.gif")}),Object(o.b)("h2",{id:"summary"},"Summary"),Object(o.b)("p",null,"We've learned the basics of navigation with React Native Navigation by implementing a stack and pushing screens into it.\nWe've also learned a few methods of applying styles to our screens and layouts with the Options mechanism."),Object(o.b)("ul",null,Object(o.b)("li",{parentName:"ul"},"Navigation.setRoot() sets the root layout of our app. The initial root has to be set from an ",Object(o.b)("inlineCode",{parentName:"li"},"AppLaunchedEvent")," callback."),Object(o.b)("li",{parentName:"ul"},"Options can be applied directly to components."),Object(o.b)("li",{parentName:"ul"},"Screens can be added to a stack with the ",Object(o.b)("inlineCode",{parentName:"li"},"Navigation.push()")," command."),Object(o.b)("li",{parentName:"ul"},"Components registered with ",Object(o.b)("inlineCode",{parentName:"li"},"Navigation.registerComponent()")," are injected with a ",Object(o.b)("inlineCode",{parentName:"li"},"componentId")," which is used when navigating."),Object(o.b)("li",{parentName:"ul"},"Themes are applied via the ",Object(o.b)("inlineCode",{parentName:"li"},"Navigation.setDefaultOptions()")," command.")),Object(o.b)("p",null,"In the next section we'll explore a more advance navigation patterns using BottomTabs layout and also see how, and why, multiple roots are set."))}u.isMDXComponent=!0},627:function(e,n,t){"use strict";t.d(n,"a",(function(){return u})),t.d(n,"b",(function(){return g}));var a=t(0),o=t.n(a);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function r(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);n&&(a=a.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,a)}return t}function s(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?r(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):r(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function c(e,n){if(null==e)return{};var t,a,o=function(e,n){if(null==e)return{};var t,a,o={},i=Object.keys(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||(o[t]=e[t]);return o}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(o[t]=e[t])}return o}var l=o.a.createContext({}),p=function(e){var n=o.a.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):s(s({},n),e)),t},u=function(e){var n=p(e.components);return o.a.createElement(l.Provider,{value:n},e.children)},d="mdxType",m={inlineCode:"code",wrapper:function(e){var n=e.children;return o.a.createElement(o.a.Fragment,{},n)}},h=o.a.forwardRef((function(e,n){var t=e.components,a=e.mdxType,i=e.originalType,r=e.parentName,l=c(e,["components","mdxType","originalType","parentName"]),u=p(t),d=a,h=u["".concat(r,".").concat(d)]||u[d]||m[d]||i;return t?o.a.createElement(h,s(s({ref:n},l),{},{components:t})):o.a.createElement(h,s({ref:n},l))}));function g(e,n){var t=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var i=t.length,r=new Array(i);r[0]=h;var s={};for(var c in n)hasOwnProperty.call(n,c)&&(s[c]=n[c]);s.originalType=e,s[d]="string"==typeof e?e:a,r[1]=s;for(var l=2;l<i;l++)r[l]=t[l];return o.a.createElement.apply(null,r)}return o.a.createElement.apply(null,t)}h.displayName="MDXCreateElement"},633:function(e,n,t){"use strict";t.d(n,"b",(function(){return i})),t.d(n,"a",(function(){return r}));var a=t(16),o=t(634);function i(){const{siteConfig:{baseUrl:e="/",url:n}={}}=Object(a.default)();return{withBaseUrl:(t,a)=>function(e,n,t,a){let{forcePrependBaseUrl:i=!1,absolute:r=!1}=void 0===a?{}:a;if(!t)return t;if(t.startsWith("#"))return t;if(Object(o.b)(t))return t;if(i)return n+t;const s=t.startsWith(n)?t:n+t.replace(/^\//,"");return r?e+s:s}(n,e,t,a)}}function r(e,n){void 0===n&&(n={});const{withBaseUrl:t}=i();return t(e,n)}},634:function(e,n,t){"use strict";function a(e){return!0===/^(\w*:|\/\/)/.test(e)}function o(e){return void 0!==e&&!a(e)}t.d(n,"b",(function(){return a})),t.d(n,"a",(function(){return o}))}}]);