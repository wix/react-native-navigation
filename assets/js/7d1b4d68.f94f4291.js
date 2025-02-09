(window.webpackJsonp=window.webpackJsonp||[]).push([[274],{340:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return o})),n.d(t,"metadata",(function(){return r})),n.d(t,"toc",(function(){return c})),n.d(t,"default",(function(){return l}));var a=n(3),i=(n(0),n(627));const o={id:"app-launch",title:"Launching the app",sidebar_label:"Launching the app"},r={unversionedId:"docs/app-launch",id:"version-7.25.4/docs/app-launch",isDocsHomePage:!1,title:"Launching the app",description:"When your app is launched for the first time, the bundle is parsed and executed. At this point you need to display your UI. To do so, listen to the appLaunched event and call Navigation.setRoot when that event is received.",source:"@site/versioned_docs/version-7.25.4/docs/docs-app-launch.mdx",slug:"/docs/app-launch",permalink:"/react-native-navigation/7.25.4/docs/app-launch",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.25.4/docs/docs-app-launch.mdx",version:"7.25.4",sidebar_label:"Launching the app",sidebar:"version-7.25.4/docs",previous:{title:"Showcases",permalink:"/react-native-navigation/7.25.4/docs/showcases"},next:{title:"Basic navigation",permalink:"/react-native-navigation/7.25.4/docs/basic-navigation"}},c=[{value:"Difference between the platforms",id:"difference-between-the-platforms",children:[{value:"iOS",id:"ios",children:[]},{value:"Android",id:"android",children:[]}]}],s={toc:c},p="wrapper";function l(e){let{components:t,...n}=e;return Object(i.b)(p,Object(a.a)({},s,n,{components:t,mdxType:"MDXLayout"}),Object(i.b)("p",null,"When your app is launched for the first time, the bundle is parsed and executed. At this point you need to display your UI. To do so, listen to the ",Object(i.b)("inlineCode",{parentName:"p"},"appLaunched")," event and call ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.setRoot")," when that event is received."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.events().registerAppLaunchedListener(() => {\n  // Each time the event is received you should call Navigation.setRoot\n});\n")),Object(i.b)("div",{className:"admonition admonition-important alert alert--info"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"}))),"important")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"Register the app launched listener as soon as possible - this should be one of the first lines in your ",Object(i.b)("inlineCode",{parentName:"p"},"index.js")," file."))),Object(i.b)("p",null,'If you\'re observing a "white screen" or a hanging splash screen after relaunching your app, it probably means ',Object(i.b)("inlineCode",{parentName:"p"},"Navigation.setRoot")," isn't called as soon as the app is launched. Perhaps the listener was registered too late."),Object(i.b)("h2",{id:"difference-between-the-platforms"},"Difference between the platforms"),Object(i.b)("p",null,"When your app is launched, RN makes sure JS context (which is what enables you to execute JavaScript code) is running. There are quite a few differences between iOS and Android in this regard."),Object(i.b)("h3",{id:"ios"},"iOS"),Object(i.b)("p",null,"Whenever an app is put into the background, the app process can potentially be destroyed by the system. If this destruction takes place, the JS context will be destroyed alongside with the app process."),Object(i.b)("h3",{id:"android"},"Android"),Object(i.b)("p",null,"An Android application is typically bound to two contexts:"),Object(i.b)("ol",null,Object(i.b)("li",{parentName:"ol"},"Application context - bound to the app process"),Object(i.b)("li",{parentName:"ol"},"Activity context - bound to app UI")),Object(i.b)("p",null,"React's JS Context is executed on and bound to the Application context. This means that even when the Activity context (and thus the UI) is destroyed, React's JS Context remains active until the Application (and your process) are destroyed by the system."),Object(i.b)("div",{className:"admonition admonition-caution alert alert--warning"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"16",height:"16",viewBox:"0 0 16 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M8.893 1.5c-.183-.31-.52-.5-.887-.5s-.703.19-.886.5L.138 13.499a.98.98 0 0 0 0 1.001c.193.31.53.501.886.501h13.964c.367 0 .704-.19.877-.5a1.03 1.03 0 0 0 .01-1.002L8.893 1.5zm.133 11.497H6.987v-2.003h2.039v2.003zm0-3.004H6.987V5.987h2.039v4.006z"}))),"caution")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"Because of this, when your app returns to foreground, JS Context might still exist on Android, even when the Activity and UI contexts have been destroyed - meaning you might not need to initialise all of your app logic; instead, you'll only need to call ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.setRoot")," to repopulate the UI context. This circumstance can easily be determined by setting a flag on your app's first ",Object(i.b)("inlineCode",{parentName:"p"},"appLaunched")," event, and checking the value of that flag on subsequent ",Object(i.b)("inlineCode",{parentName:"p"},"appLaunched")," events -- if the flag is true in your ",Object(i.b)("inlineCode",{parentName:"p"},"appLaunched")," callback, you need to call ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.setRoot")," to repopulate the UI."))))}l.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return d})),n.d(t,"b",(function(){return m}));var a=n(0),i=n.n(a);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,a,i=function(e,t){if(null==e)return{};var n,a,i={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var p=i.a.createContext({}),l=function(e){var t=i.a.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},d=function(e){var t=l(e.components);return i.a.createElement(p.Provider,{value:t},e.children)},u="mdxType",b={inlineCode:"code",wrapper:function(e){var t=e.children;return i.a.createElement(i.a.Fragment,{},t)}},h=i.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,r=e.parentName,p=s(e,["components","mdxType","originalType","parentName"]),d=l(n),u=a,h=d["".concat(r,".").concat(u)]||d[u]||b[u]||o;return n?i.a.createElement(h,c(c({ref:t},p),{},{components:n})):i.a.createElement(h,c({ref:t},p))}));function m(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,r=new Array(o);r[0]=h;var c={};for(var s in t)hasOwnProperty.call(t,s)&&(c[s]=t[s]);c.originalType=e,c[u]="string"==typeof e?e:a,r[1]=c;for(var p=2;p<o;p++)r[p]=n[p];return i.a.createElement.apply(null,r)}return i.a.createElement.apply(null,n)}h.displayName="MDXCreateElement"}}]);