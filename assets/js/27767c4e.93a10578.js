(window.webpackJsonp=window.webpackJsonp||[]).push([[88],{154:function(t,e,n){"use strict";n.r(e),n.d(e,"frontMatter",(function(){return c})),n.d(e,"metadata",(function(){return l})),n.d(e,"toc",(function(){return s})),n.d(e,"default",(function(){return p}));var a=n(3),r=n(7),o=(n(0),n(627));const i=["components"],c={id:"stack-backButton",title:"The Back button",sidebar_label:"Back button"},l={unversionedId:"docs/stack-backButton",id:"docs/stack-backButton",isDocsHomePage:!1,title:"The Back button",description:"The back button is added automatically when two or more screens are pushed into the stack.",source:"@site/docs/docs/stack-backButton.mdx",slug:"/docs/stack-backButton",permalink:"/react-native-navigation/next/docs/stack-backButton",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/docs/docs/stack-backButton.mdx",version:"current",sidebar_label:"Back button"},s=[{value:"Styling the back button",id:"styling-the-back-button",children:[]},{value:"Controling visibility",id:"controling-visibility",children:[]},{value:"Changing visbility programmatically",id:"changing-visbility-programmatically",children:[]},{value:"Handling the back button",id:"handling-the-back-button",children:[]}],b={toc:s},u="wrapper";function p(t){let{components:e}=t,n=Object(r.a)(t,i);return Object(o.b)(u,Object(a.a)({},b,n,{components:e,mdxType:"MDXLayout"}),Object(o.b)("p",null,"The back button is added automatically when two or more screens are pushed into the stack."),Object(o.b)("h2",{id:"styling-the-back-button"},"Styling the back button"),Object(o.b)("p",null,"The back button's style can be customised by declaring a backButton options object. This configuration can be part of a screen's static options, or default options."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"backButton: {\n  color: 'red',\n  icon: require('../../img/customChevron.png')\n}\n")),Object(o.b)("h2",{id:"controling-visibility"},"Controling visibility"),Object(o.b)("p",null,"The back buttons visbility can be controlled with the visible property."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"backButton: {\n  visible: false\n}\n")),Object(o.b)("h2",{id:"changing-visbility-programmatically"},"Changing visbility programmatically"),Object(o.b)("p",null,"Back button visibility can be changed dynamically using the mergeOptions command. When using a screen's componentId, the change will affect only that specific screen. But when using the stack's id, the change will affect all screens pushed into the stack."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.mergeOptions(this.props.componentId, {\n  backButton: {\n    visible: false\n  }\n});\n")),Object(o.b)("h2",{id:"handling-the-back-button"},"Handling the back button"),Object(o.b)("p",null,"Handling the back button is not possible. However, you can set a left button with a chevron and handle it like you'd handle any other button and calling ",Object(o.b)("inlineCode",{parentName:"p"},"Navigation.pop")," when desired."))}p.isMDXComponent=!0},627:function(t,e,n){"use strict";n.d(e,"a",(function(){return u})),n.d(e,"b",(function(){return g}));var a=n(0),r=n.n(a);function o(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function i(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(t);e&&(a=a.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,a)}return n}function c(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?i(Object(n),!0).forEach((function(e){o(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}function l(t,e){if(null==t)return{};var n,a,r=function(t,e){if(null==t)return{};var n,a,r={},o=Object.keys(t);for(a=0;a<o.length;a++)n=o[a],e.indexOf(n)>=0||(r[n]=t[n]);return r}(t,e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(t);for(a=0;a<o.length;a++)n=o[a],e.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(t,n)&&(r[n]=t[n])}return r}var s=r.a.createContext({}),b=function(t){var e=r.a.useContext(s),n=e;return t&&(n="function"==typeof t?t(e):c(c({},e),t)),n},u=function(t){var e=b(t.components);return r.a.createElement(s.Provider,{value:e},t.children)},p="mdxType",d={inlineCode:"code",wrapper:function(t){var e=t.children;return r.a.createElement(r.a.Fragment,{},e)}},h=r.a.forwardRef((function(t,e){var n=t.components,a=t.mdxType,o=t.originalType,i=t.parentName,s=l(t,["components","mdxType","originalType","parentName"]),u=b(n),p=a,h=u["".concat(i,".").concat(p)]||u[p]||d[p]||o;return n?r.a.createElement(h,c(c({ref:e},s),{},{components:n})):r.a.createElement(h,c({ref:e},s))}));function g(t,e){var n=arguments,a=e&&e.mdxType;if("string"==typeof t||a){var o=n.length,i=new Array(o);i[0]=h;var c={};for(var l in e)hasOwnProperty.call(e,l)&&(c[l]=e[l]);c.originalType=t,c[p]="string"==typeof t?t:a,i[1]=c;for(var s=2;s<o;s++)i[s]=n[s];return r.a.createElement.apply(null,i)}return r.a.createElement.apply(null,n)}h.displayName="MDXCreateElement"}}]);