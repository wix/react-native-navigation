(window.webpackJsonp=window.webpackJsonp||[]).push([[492],{565:function(e,t,a){"use strict";a.r(t),a.d(t,"frontMatter",(function(){return l})),a.d(t,"metadata",(function(){return c})),a.d(t,"toc",(function(){return p})),a.d(t,"default",(function(){return u}));var n=a(3),r=a(7),i=(a(0),a(627));const o=["components"],l={id:"overlay",title:"Overlay",sidebar_label:"Overlay"},c={unversionedId:"api/overlay",id:"version-7.7.0/api/overlay",isDocsHomePage:!1,title:"Overlay",description:"showOverlay()",source:"@site/versioned_docs/version-7.7.0/api/api-overlay.mdx",slug:"/api/overlay",permalink:"/react-native-navigation/7.7.0/api/overlay",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.7.0/api/api-overlay.mdx",version:"7.7.0",sidebar_label:"Overlay",sidebar:"version-7.7.0/api",previous:{title:"Modal",permalink:"/react-native-navigation/7.7.0/api/modal"},next:{title:"Layout",permalink:"/react-native-navigation/7.7.0/api/layout-layout"}},p=[{value:"<code>showOverlay()</code>",id:"showoverlay",children:[]},{value:"<code>dismissOverlay()</code>",id:"dismissoverlay",children:[]},{value:"<code>dismissAllOverlays()</code>",id:"dismissalloverlays",children:[]}],b={toc:p},s="wrapper";function u(e){let{components:t}=e,a=Object(r.a)(e,o);return Object(i.b)(s,Object(n.a)({},b,a,{components:t,mdxType:"MDXLayout"}),Object(i.b)("h2",{id:"showoverlay"},Object(i.b)("inlineCode",{parentName:"h2"},"showOverlay()")),Object(i.b)("p",null,"Shows a component as an overlay."),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Name"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"layout"),Object(i.b)("td",{parentName:"tr",align:null},"Yes"),Object(i.b)("td",{parentName:"tr",align:null},Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/layout-layout"},"Layout")),Object(i.b)("td",{parentName:"tr",align:null},"Any type of layout. Typically, Component layout is used in Overlays but it's possible to load any other layout (",Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/layout-bottomTabs"},"BottomTabs"),", ",Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/layout-stack"},"Stack"),", ",Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/layout-sideMenu"},"SideMenu"),", ",Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/layout-component"},"Component"),")")))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.showOverlay({\n  component: {\n    name: 'example.Overlay',\n    options: {\n      layout: {\n            componentBackgroundColor: 'transparent',\n          },\n      overlay: {\n        interceptTouchOutside: true\n      }\n    }\n  }\n});\n")),Object(i.b)("h2",{id:"dismissoverlay"},Object(i.b)("inlineCode",{parentName:"h2"},"dismissOverlay()")),Object(i.b)("p",null,"Dismisses an overlay matching the given overlay componentId."),Object(i.b)("h4",{id:"parameters"},"Parameters"),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Name"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"componentId"),Object(i.b)("td",{parentName:"tr",align:null},"Yes"),Object(i.b)("td",{parentName:"tr",align:null},"string"),Object(i.b)("td",{parentName:"tr",align:null},"The component id presented in Overlay")))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.dismissOverlay(this.props.componentId);\n")),Object(i.b)("h2",{id:"dismissalloverlays"},Object(i.b)("inlineCode",{parentName:"h2"},"dismissAllOverlays()")),Object(i.b)("p",null,"Dismisses all overlays."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.dismissAllOverlays();\n")))}u.isMDXComponent=!0},627:function(e,t,a){"use strict";a.d(t,"a",(function(){return s})),a.d(t,"b",(function(){return y}));var n=a(0),r=a.n(n);function i(e,t,a){return t in e?Object.defineProperty(e,t,{value:a,enumerable:!0,configurable:!0,writable:!0}):e[t]=a,e}function o(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,n)}return a}function l(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?o(Object(a),!0).forEach((function(t){i(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):o(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}function c(e,t){if(null==e)return{};var a,n,r=function(e,t){if(null==e)return{};var a,n,r={},i=Object.keys(e);for(n=0;n<i.length;n++)a=i[n],t.indexOf(a)>=0||(r[a]=e[a]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(n=0;n<i.length;n++)a=i[n],t.indexOf(a)>=0||Object.prototype.propertyIsEnumerable.call(e,a)&&(r[a]=e[a])}return r}var p=r.a.createContext({}),b=function(e){var t=r.a.useContext(p),a=t;return e&&(a="function"==typeof e?e(t):l(l({},t),e)),a},s=function(e){var t=b(e.components);return r.a.createElement(p.Provider,{value:t},e.children)},u="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return r.a.createElement(r.a.Fragment,{},t)}},m=r.a.forwardRef((function(e,t){var a=e.components,n=e.mdxType,i=e.originalType,o=e.parentName,p=c(e,["components","mdxType","originalType","parentName"]),s=b(a),u=n,m=s["".concat(o,".").concat(u)]||s[u]||d[u]||i;return a?r.a.createElement(m,l(l({ref:t},p),{},{components:a})):r.a.createElement(m,l({ref:t},p))}));function y(e,t){var a=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var i=a.length,o=new Array(i);o[0]=m;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l[u]="string"==typeof e?e:n,o[1]=l;for(var p=2;p<i;p++)o[p]=a[p];return r.a.createElement.apply(null,o)}return r.a.createElement.apply(null,a)}m.displayName="MDXCreateElement"}}]);