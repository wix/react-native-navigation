(window.webpackJsonp=window.webpackJsonp||[]).push([[169],{235:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return r})),n.d(t,"metadata",(function(){return i})),n.d(t,"toc",(function(){return s})),n.d(t,"default",(function(){return u}));var a=n(3),o=(n(0),n(627));const r={id:"style-constants",title:"Constants",sidebar_label:"Constants"},i={unversionedId:"docs/style-constants",id:"version-7.37.0/docs/style-constants",isDocsHomePage:!1,title:"Constants",description:"React Native Navigation exposes a set of constants which can be used to get the dimensions of various navigation elements on the screen: StatusBar, TopBar and BottomTabs.",source:"@site/versioned_docs/version-7.37.0/docs/style-constants.mdx",slug:"/docs/style-constants",permalink:"/react-native-navigation/docs/style-constants",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.37.0/docs/style-constants.mdx",version:"7.37.0",sidebar_label:"Constants",sidebar:"version-7.37.0/docs",previous:{title:"Changing fonts",permalink:"/react-native-navigation/docs/style-fonts"},next:{title:"Community libraries",permalink:"/react-native-navigation/docs/community-libraries"}},s=[{value:"When are constants evaluated",id:"when-are-constants-evaluated",children:[]},{value:"API",id:"api",children:[]}],c={toc:s},l="wrapper";function u(e){let{components:t,...n}=e;return Object(o.b)(l,Object(a.a)({},c,n,{components:t,mdxType:"MDXLayout"}),Object(o.b)("p",null,"React Native Navigation exposes a set of constants which can be used to get the dimensions of various navigation elements on the screen: StatusBar, TopBar and BottomTabs."),Object(o.b)("h2",{id:"when-are-constants-evaluated"},"When are constants evaluated"),Object(o.b)("p",null,"Some of the values exposed through the constants API are actually evaluated only after the UI is created (",Object(o.b)("inlineCode",{parentName:"p"},"setRoot")," has been called) and therefore are not accessible through static getters."),Object(o.b)("p",null,"For example, if you need to get the height of the BottomTabs, you'll first need to have BottomTabs visible on the screen and only then retrieve their height via the constants API."),Object(o.b)("div",{className:"admonition admonition-important alert alert--info"},Object(o.b)("div",{parentName:"div",className:"admonition-heading"},Object(o.b)("h5",{parentName:"div"},Object(o.b)("span",{parentName:"h5",className:"admonition-icon"},Object(o.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(o.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"important")),Object(o.b)("div",{parentName:"div",className:"admonition-content"},Object(o.b)("p",{parentName:"div"},"We recommend you don't cache the actual constants object returned by ",Object(o.b)("inlineCode",{parentName:"p"},"await Navigation.constants()")," or ",Object(o.b)("inlineCode",{parentName:"p"},"Navigation.constantsSync()")," as it might not be accurate later on when, for example, a new root is set or orientation changes."))),Object(o.b)("h2",{id:"api"},"API"),Object(o.b)("p",null,"As explained above, constants are evaluated in native each time the API is invoked. There are two methods for accessing constants:"),Object(o.b)("ul",null,Object(o.b)("li",{parentName:"ul"},Object(o.b)("inlineCode",{parentName:"li"},"Navigation.constants()"),", which is async and thus returns a promise"),Object(o.b)("li",{parentName:"ul"},Object(o.b)("inlineCode",{parentName:"li"},"Navigation.constantsSync()"),", which returns the constants synchronously, blocking the main thread")),Object(o.b)("p",null,"Where possible, it is better to use the async method, since this will not block the main thread."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"const { Navigation } = require('react-native-navigation');\nconst {\n  statusBarHeight,\n  topBarHeight,\n  bottomTabsHeight\n} = await Navigation.constants();\n")),Object(o.b)("p",null,"But, if you need to access the constants synchronously (e.g. within the ",Object(o.b)("inlineCode",{parentName:"p"},"render()")," method of a React component), you can do so as follows:"),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"const { Navigation } = require('react-native-navigation');\nconst {\n  statusBarHeight,\n  topBarHeight,\n  bottomTabsHeight\n} = Navigation.constantsSync();\n")))}u.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return b})),n.d(t,"b",(function(){return h}));var a=n(0),o=n.n(a);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var l=o.a.createContext({}),u=function(e){var t=o.a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):s(s({},t),e)),n},b=function(e){var t=u(e.components);return o.a.createElement(l.Provider,{value:t},e.children)},d="mdxType",p={inlineCode:"code",wrapper:function(e){var t=e.children;return o.a.createElement(o.a.Fragment,{},t)}},m=o.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,i=e.parentName,l=c(e,["components","mdxType","originalType","parentName"]),b=u(n),d=a,m=b["".concat(i,".").concat(d)]||b[d]||p[d]||r;return n?o.a.createElement(m,s(s({ref:t},l),{},{components:n})):o.a.createElement(m,s({ref:t},l))}));function h(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,i=new Array(r);i[0]=m;var s={};for(var c in t)hasOwnProperty.call(t,c)&&(s[c]=t[c]);s.originalType=e,s[d]="string"==typeof e?e:a,i[1]=s;for(var l=2;l<r;l++)i[l]=n[l];return o.a.createElement.apply(null,i)}return o.a.createElement.apply(null,n)}m.displayName="MDXCreateElement"}}]);