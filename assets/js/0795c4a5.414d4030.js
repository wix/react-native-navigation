(window.webpackJsonp=window.webpackJsonp||[]).push([[18],{627:function(e,t,n){"use strict";n.d(t,"a",(function(){return u})),n.d(t,"b",(function(){return O}));var r=n(0),a=n.n(r);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function p(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var c=a.a.createContext({}),s=function(e){var t=a.a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},u=function(e){var t=s(e.components);return a.a.createElement(c.Provider,{value:t},e.children)},b="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.a.createElement(a.a.Fragment,{},t)}},m=a.a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,o=e.originalType,i=e.parentName,c=p(e,["components","mdxType","originalType","parentName"]),u=s(n),b=r,m=u["".concat(i,".").concat(b)]||u[b]||d[b]||o;return n?a.a.createElement(m,l(l({ref:t},c),{},{components:n})):a.a.createElement(m,l({ref:t},c))}));function O(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var o=n.length,i=new Array(o);i[0]=m;var l={};for(var p in t)hasOwnProperty.call(t,p)&&(l[p]=t[p]);l.originalType=e,l[b]="string"==typeof e?e:r,i[1]=l;for(var c=2;c<o;c++)i[c]=n[c];return a.a.createElement.apply(null,i)}return a.a.createElement.apply(null,n)}m.displayName="MDXCreateElement"},81:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return l})),n.d(t,"metadata",(function(){return p})),n.d(t,"toc",(function(){return c})),n.d(t,"default",(function(){return b}));var r=n(3),a=n(7),o=(n(0),n(627));const i=["components"],l={id:"options-modal",title:"Modal Options",sidebar_label:"Modal"},p={unversionedId:"api/options-modal",id:"version-7.25.4/api/options-modal",isDocsHomePage:!1,title:"Modal Options",description:"`js",source:"@site/versioned_docs/version-7.25.4/api/options-modal.mdx",slug:"/api/options-modal",permalink:"/react-native-navigation/7.25.4/api/options-modal",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.25.4/api/options-modal.mdx",version:"7.25.4",sidebar_label:"Modal",sidebar:"version-7.25.4/api",previous:{title:"Layout Options",permalink:"/react-native-navigation/7.25.4/api/options-layout"},next:{title:"Navigation Bar Options",permalink:"/react-native-navigation/7.25.4/api/options-navigationBar"}},c=[{value:"<code>swipeToDismiss</code>",id:"swipetodismiss",children:[]}],s={toc:c},u="wrapper";function b(e){let{components:t}=e,n=Object(a.a)(e,i);return Object(o.b)(u,Object(r.a)({},s,n,{components:t,mdxType:"MDXLayout"}),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"const options = {\n  swipeToDismiss\n};\n")),Object(o.b)("h2",{id:"swipetodismiss"},Object(o.b)("inlineCode",{parentName:"h2"},"swipeToDismiss")),Object(o.b)("p",null,"Control wether this modal should be dismiss using swipe gesture when the ",Object(o.b)("inlineCode",{parentName:"p"},"modalPresentationStyle")," is ",Object(o.b)("inlineCode",{parentName:"p"},"pageSheet")),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Platform"),Object(o.b)("th",{parentName:"tr",align:null},"Default"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"boolean"),Object(o.b)("td",{parentName:"tr",align:null},"No"),Object(o.b)("td",{parentName:"tr",align:null},"Both"),Object(o.b)("td",{parentName:"tr",align:null},"true")))))}b.isMDXComponent=!0}}]);