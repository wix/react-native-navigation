(window.webpackJsonp=window.webpackJsonp||[]).push([[272],{338:function(e,n,t){"use strict";t.r(n),t.d(n,"frontMatter",(function(){return c})),t.d(n,"metadata",(function(){return u})),t.d(n,"toc",(function(){return l})),t.d(n,"default",(function(){return d}));var r=t(3),o=t(7),i=(t(0),t(627));const a=["components"],c={id:"sideMenu-api",title:"Side Menu",sidebar_label:"Side Menu"},u={unversionedId:"api/sideMenu-api",id:"version-7.32.1/api/sideMenu-api",isDocsHomePage:!1,title:"Side Menu",description:"This layout allows to implement sidemenus, which can be opened by swiping from one side towards the other side.",source:"@site/versioned_docs/version-7.32.1/api/api-sideMenu.mdx",slug:"/api/sideMenu-api",permalink:"/react-native-navigation/7.32.1/api/sideMenu-api",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.32.1/api/api-sideMenu.mdx",version:"7.32.1",sidebar_label:"Side Menu"},l=[{value:"<code>center: Layout</code>",id:"center-layout",children:[]},{value:"<code>left?: Layout</code>",id:"left-layout",children:[]},{value:"<code>right?: Layout</code>",id:"right-layout",children:[]}],p={toc:l},s="wrapper";function d(e){let{components:n}=e,t=Object(o.a)(e,a);return Object(i.b)(s,Object(r.a)({},p,t,{components:n,mdxType:"MDXLayout"}),Object(i.b)("p",null,"This layout allows to implement sidemenus, which can be opened by swiping from one side towards the other side."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"{\n  left: {\n    component: {}\n  },\n  center: {\n    stack: {\n      options: {},\n      children: [{\n        component: {}\n      }]\n    }\n  },\n  right: {\n    component: {}\n  }\n}\n")),Object(i.b)("h2",{id:"center-layout"},Object(i.b)("inlineCode",{parentName:"h2"},"center: Layout")),Object(i.b)("p",null,"Center component, contains the main application."),Object(i.b)("h2",{id:"left-layout"},Object(i.b)("inlineCode",{parentName:"h2"},"left?: Layout")),Object(i.b)("p",null,"Contain the left component layout."),Object(i.b)("h2",{id:"right-layout"},Object(i.b)("inlineCode",{parentName:"h2"},"right?: Layout")),Object(i.b)("p",null,"Contain the right component layout."))}d.isMDXComponent=!0},627:function(e,n,t){"use strict";t.d(n,"a",(function(){return s})),t.d(n,"b",(function(){return m}));var r=t(0),o=t.n(r);function i(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function a(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);n&&(r=r.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,r)}return t}function c(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?a(Object(t),!0).forEach((function(n){i(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):a(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function u(e,n){if(null==e)return{};var t,r,o=function(e,n){if(null==e)return{};var t,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)t=i[r],n.indexOf(t)>=0||(o[t]=e[t]);return o}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)t=i[r],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(o[t]=e[t])}return o}var l=o.a.createContext({}),p=function(e){var n=o.a.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):c(c({},n),e)),t},s=function(e){var n=p(e.components);return o.a.createElement(l.Provider,{value:n},e.children)},d="mdxType",b={inlineCode:"code",wrapper:function(e){var n=e.children;return o.a.createElement(o.a.Fragment,{},n)}},f=o.a.forwardRef((function(e,n){var t=e.components,r=e.mdxType,i=e.originalType,a=e.parentName,l=u(e,["components","mdxType","originalType","parentName"]),s=p(t),d=r,f=s["".concat(a,".").concat(d)]||s[d]||b[d]||i;return t?o.a.createElement(f,c(c({ref:n},l),{},{components:t})):o.a.createElement(f,c({ref:n},l))}));function m(e,n){var t=arguments,r=n&&n.mdxType;if("string"==typeof e||r){var i=t.length,a=new Array(i);a[0]=f;var c={};for(var u in n)hasOwnProperty.call(n,u)&&(c[u]=n[u]);c.originalType=e,c[d]="string"==typeof e?e:r,a[1]=c;for(var l=2;l<i;l++)a[l]=t[l];return o.a.createElement.apply(null,a)}return o.a.createElement.apply(null,t)}f.displayName="MDXCreateElement"}}]);