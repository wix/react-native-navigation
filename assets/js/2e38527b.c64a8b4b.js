(window.webpackJsonp=window.webpackJsonp||[]).push([[107],{173:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return c})),t.d(n,"metadata",(function(){return l})),t.d(n,"toc",(function(){return p})),t.d(n,"default",(function(){return m}));var a=t(3),o=t(7),i=(t(0),t(627)),r=t(633),s=["components"],c={id:"basic-navigation",title:"Basic navigation",sidebar_label:"Basic navigation"},l={unversionedId:"docs/basic-navigation",id:"docs/basic-navigation",isDocsHomePage:!1,title:"Basic navigation",description:"Generally, any mobile app consists of various destinations which display some content to the user. And in vast majority of cases using an app means navigating between these destinations. React-native-navigation provides ways for you to layout your content on user screen in a logical and performant manner.",source:"@site/docs/docs/docs-basic-navigation.mdx",slug:"/docs/basic-navigation",permalink:"/react-native-navigation/next/docs/basic-navigation",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/docs/docs/docs-basic-navigation.mdx",version:"current",sidebar_label:"Basic navigation",sidebar:"docs",previous:{title:"Launching the app",permalink:"/react-native-navigation/next/docs/app-launch"},next:{title:"Advanced navigation",permalink:"/react-native-navigation/next/docs/advanced-navigation"}},p=[{value:"Creating a stack",id:"creating-a-stack",children:[]},{value:"Specifying options",id:"specifying-options",children:[]},{value:"Navigating in a stack",id:"navigating-in-a-stack",children:[]},{value:"App theme",id:"app-theme",children:[]},{value:"Summary",id:"summary",children:[]}],u={toc:p},d="wrapper";function m(e){var n=e.components,t=Object(o.a)(e,s);return Object(i.b)(d,Object(a.a)({},u,t,{components:n,mdxType:"MDXLayout"}),Object(i.b)("p",null,"Generally, any mobile app consists of various destinations which display some content to the user. And in vast majority of cases using an app means navigating between these destinations. React-native-navigation provides ways for you to layout your content on user screen in a logical and performant manner."),Object(i.b)("p",null,"React Native Navigation's stack layout lets you push screens, and also navigate back to previous screens. Screens pushed into the stack hide the previous screen in the stack, making the user focus on a single screen at a time. Let's look at a stack layout that provides basic navigation for an app."),Object(i.b)("h2",{id:"creating-a-stack"},"Creating a stack"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Home Screen</Text>\n    </View>\n  );\n};\nNavigation.registerComponent('Home', () => HomeScreen);\n\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(i.b)("p",null,"Running this code will render a screen with an empty TopBar and your HomeScreen component (shown below). The TopBar is part of the stack layout, we'll work on configuring it later."),Object(i.b)("img",{width:"40%",src:Object(r.a)("img/stack1.png")}),Object(i.b)("h2",{id:"specifying-options"},"Specifying options"),Object(i.b)("p",null,"You can specify options of each layout (Stack, component pushed into a stack, etc.) to configure various parameters like the TopBar title or color. Lets spice things up by changing the TopBar color and while we're at it, also move the title to the TopBar."),Object(i.b)("p",null,"Lets change the Home screen declaration to the following and reload the app to see the changes."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-jsx"},"const HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n}\n")),Object(i.b)("p",null,"Our app should now look like this:"),Object(i.b)("img",{width:"40%",src:Object(r.a)("img/stackOptions.png")}),Object(i.b)("h2",{id:"navigating-in-a-stack"},"Navigating in a stack"),Object(i.b)("p",null,"In the previous section we created a stack and initialized it with a single child. We'll now learn how to add another child to the stack. From now on, we'll call the action of adding children to the stack 'push', and removing children - 'pop'."),Object(i.b)("p",null,"In order to push another screen to the stack, we will add a button to the home screen and call ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.push"),". The 'push' command accepts two parameters, the first is the id used to indicate into which stack to push the screen and the second is the screen we're pushing. After pushing a screen, a back button is added automatically to the TopBar so the users can navigate easily back to the previous screen."),Object(i.b)("p",null,"You can read more about the stack layout ",Object(i.b)("a",{parentName:"p",href:"/react-native-navigation/next/docs/stack"},"here")," or dive right into the API ",Object(i.b)("a",{parentName:"p",href:"../api/layout-stack"},"here"),"."),Object(i.b)("div",{className:"admonition admonition-info alert alert--info"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"componentId")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"Each React component registered with ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.registerComponent")," is assigned a unique ",Object(i.b)("inlineCode",{parentName:"p"},"componentId")," by Navigation. This unique id is used with Navigation commands (like the push command) to indicate into which stack we'd like to push. In this case, by using the componentId of the Home screen, we are telling Navigation to push into the stack containing the Home screen."))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.push(props.componentId, {\n  component: {\n    name: 'Settings', // Push the screen registered with the 'Settings' key\n    options: { // Optional options object to configure the screen\n      topBar: {\n        title: {\n          text: 'Settings' // Set the TopBar title of the new Screen\n        }\n      }\n    }\n  }\n});\n")),Object(i.b)("p",null,"Lets change our code to the following snippet below and reload the app. Now our Home should contain a button which when pressed, pushes a new screen into the stack. We've successfully navigated to a new screen! \ud83d\udc4f"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\n// Home screen declaration\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n      <Button\n        title='Push Settings Screen'\n        color='#710ce3'\n        onPress={() => Navigation.push(props.componentId, {\n          component: {\n            name: 'Settings',\n            options: {\n              topBar: {\n                title: {\n                  text: 'Settings'\n                }\n              }\n            }\n          }\n        })}/>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n};\n\n// Settings screen declaration - this is the screen we'll be pushing into the stack\nconst SettingsScreen = () => {\n  return (\n    <View style={styles.root}>\n      <Text>Settings Screen</Text>\n    </View>\n  );\n}\n\nNavigation.registerComponent('Home', () => HomeScreen);\nNavigation.registerComponent('Settings', () => SettingsScreen);\n\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(i.b)("img",{width:"40%",src:Object(r.a)("img/stack2.gif")}),Object(i.b)("h2",{id:"app-theme"},"App theme"),Object(i.b)("p",null,"Our app is growing and already contains two screens. This introduces a new problem - while the home screen has a nice purple TopBar, our settings screen seems pretty dull as it still uses the default system look and feel."),Object(i.b)("p",null,"Currently, our style declaration applies only to the Home screen, so lets apply it to all of our screens by using ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.setDefaultOptions"),"."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setDefaultOptions({\n  statusBar: {\n    backgroundColor: '#4d089a'\n  },\n  topBar: {\n    title: {\n      color: 'white'\n    },\n    backButton: {\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n});\n")),Object(i.b)("p",null,"We need to add this snippet before registering the ",Object(i.b)("inlineCode",{parentName:"p"},"registerAppLaunchedListener"),". This way we ensure our theme is applied when our root is set. Our code should now look like this:"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-jsx"},"// In index.js of a new project\nimport React from 'react';\nimport { View, Text, Button, StyleSheet } from 'react-native';\nimport { Navigation } from 'react-native-navigation';\n\n// Home screen declaration\nconst HomeScreen = (props) => {\n  return (\n    <View style={styles.root}>\n      <Text>Hello React Native Navigation \ud83d\udc4b</Text>\n      <Button\n        title='Push Settings Screen'\n        color='#710ce3'\n        onPress={() => Navigation.push(props.componentId, {\n          component: {\n            name: 'Settings',\n            options: {\n              topBar: {\n                title: {\n                  text: 'Settings'\n                }\n              }\n            }\n          }\n        })}/>\n    </View>\n  );\n};\nHomeScreen.options = {\n  topBar: {\n    title: {\n      text: 'Home',\n    }\n  }\n};\n\n// Settings screen declaration - this is the screen we'll be pushing into the stack\nconst SettingsScreen = () => {\n  return (\n    <View style={styles.root}>\n      <Text>Settings Screen</Text>\n    </View>\n  );\n}\n\nNavigation.registerComponent('Home', () => HomeScreen);\nNavigation.registerComponent('Settings', () => SettingsScreen);\n\n\nNavigation.setDefaultOptions({\n  statusBar: {\n    backgroundColor: '#4d089a'\n  },\n  topBar: {\n    title: {\n      color: 'white'\n    },\n    backButton: {\n      color: 'white'\n    },\n    background: {\n      color: '#4d089a'\n    }\n  }\n});\nNavigation.events().registerAppLaunchedListener(async () => {\n  Navigation.setRoot({\n    root: {\n      stack: {\n        children: [\n          {\n            component: {\n              name: 'Home'\n            }\n          }\n        ]\n      }\n    }\n  });\n});\n\nconst styles = StyleSheet.create({\n  root: {\n    flex: 1,\n    alignItems: 'center',\n    justifyContent: 'center',\n    backgroundColor: 'whitesmoke'\n  }\n});\n")),Object(i.b)("p",null,"Lets run our code again - now our design is consistent across both screens."),Object(i.b)("img",{width:"40%",src:Object(r.a)("img/stack3.gif")}),Object(i.b)("h2",{id:"summary"},"Summary"),Object(i.b)("p",null,"We've learned the basics of navigation with React Native Navigation by implementing a stack and pushing screens into it.\nWe've also learned a few methods of applying styles to our screens and layouts with the Options mechanism."),Object(i.b)("ul",null,Object(i.b)("li",{parentName:"ul"},"Navigation.setRoot() sets the root layout of our app. The initial root has to be set from an ",Object(i.b)("inlineCode",{parentName:"li"},"AppLaunchedEvent")," callback."),Object(i.b)("li",{parentName:"ul"},"Options can be applied directly to components."),Object(i.b)("li",{parentName:"ul"},"Screens can be added to a stack with the ",Object(i.b)("inlineCode",{parentName:"li"},"Navigation.push()")," command."),Object(i.b)("li",{parentName:"ul"},"Components registered with ",Object(i.b)("inlineCode",{parentName:"li"},"Navigation.registerComponent()")," are injected with a ",Object(i.b)("inlineCode",{parentName:"li"},"componentId")," which is used when navigating."),Object(i.b)("li",{parentName:"ul"},"Themes are applied via the ",Object(i.b)("inlineCode",{parentName:"li"},"Navigation.setDefaultOptions()")," command.")),Object(i.b)("p",null,"In the next section we'll explore a more advance navigation patterns using BottomTabs layout and also see how, and why, multiple roots are set."))}m.isMDXComponent=!0},627:function(e,n,t){"use strict";t.d(n,"a",(function(){return u})),t.d(n,"b",(function(){return g}));var a=t(0),o=t.n(a);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function r(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);n&&(a=a.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,a)}return t}function s(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?r(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):r(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function c(e,n){if(null==e)return{};var t,a,o=function(e,n){if(null==e)return{};var t,a,o={},i=Object.keys(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||(o[t]=e[t]);return o}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)t=i[a],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(o[t]=e[t])}return o}var l=o.a.createContext({}),p=function(e){var n=o.a.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):s(s({},n),e)),t},u=function(e){var n=p(e.components);return o.a.createElement(l.Provider,{value:n},e.children)},d="mdxType",m={inlineCode:"code",wrapper:function(e){var n=e.children;return o.a.createElement(o.a.Fragment,{},n)}},h=o.a.forwardRef((function(e,n){var t=e.components,a=e.mdxType,i=e.originalType,r=e.parentName,l=c(e,["components","mdxType","originalType","parentName"]),u=p(t),d=a,h=u["".concat(r,".").concat(d)]||u[d]||m[d]||i;return t?o.a.createElement(h,s(s({ref:n},l),{},{components:t})):o.a.createElement(h,s({ref:n},l))}));function g(e,n){var t=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var i=t.length,r=new Array(i);r[0]=h;var s={};for(var c in n)hasOwnProperty.call(n,c)&&(s[c]=n[c]);s.originalType=e,s[d]="string"==typeof e?e:a,r[1]=s;for(var l=2;l<i;l++)r[l]=t[l];return o.a.createElement.apply(null,r)}return o.a.createElement.apply(null,t)}h.displayName="MDXCreateElement"},633:function(e,n,t){"use strict";t.d(n,"b",(function(){return i})),t.d(n,"a",(function(){return r}));var a=t(16),o=t(634);function i(){var e=Object(a.default)().siteConfig,n=void 0===e?{}:e,t=n.baseUrl,i=void 0===t?"/":t,r=n.url;return{withBaseUrl:function(e,n){return function(e,n,t,a){var i=void 0===a?{}:a,r=i.forcePrependBaseUrl,s=void 0!==r&&r,c=i.absolute,l=void 0!==c&&c;if(!t)return t;if(t.startsWith("#"))return t;if(Object(o.b)(t))return t;if(s)return n+t;var p=t.startsWith(n)?t:n+t.replace(/^\//,"");return l?e+p:p}(r,i,e,n)}}}function r(e,n){return void 0===n&&(n={}),(0,i().withBaseUrl)(e,n)}},634:function(e,n,t){"use strict";function a(e){return!0===/^(\w*:|\/\/)/.test(e)}function o(e){return void 0!==e&&!a(e)}t.d(n,"b",(function(){return a})),t.d(n,"a",(function(){return o}))}}]);