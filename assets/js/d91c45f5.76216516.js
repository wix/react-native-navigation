(window.webpackJsonp=window.webpackJsonp||[]).push([[465],{536:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return s})),n.d(t,"metadata",(function(){return u})),n.d(t,"toc",(function(){return p})),n.d(t,"default",(function(){return m}));var a=n(3),o=n(7),r=(n(0),n(627)),i=n(631),l=n(632),c=["components"],s={id:"style-orientation",title:"Orientation",sidebar_label:"Orientation"},u={unversionedId:"docs/style-orientation",id:"version-7.32.1/docs/style-orientation",isDocsHomePage:!1,title:"Orientation",description:"Locking orientation",source:"@site/versioned_docs/version-7.32.1/docs/style-orientation.mdx",slug:"/docs/style-orientation",permalink:"/react-native-navigation/7.32.1/docs/style-orientation",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.32.1/docs/style-orientation.mdx",version:"7.32.1",sidebar_label:"Orientation",sidebar:"version-7.32.1/docs",previous:{title:"StatusBar",permalink:"/react-native-navigation/7.32.1/docs/style-statusBar"},next:{title:"Animations",permalink:"/react-native-navigation/7.32.1/docs/style-animations"}},p=[{value:"Locking orientation",id:"locking-orientation",children:[]},{value:"Changing orientation dynamically",id:"changing-orientation-dynamically",children:[]}],d={toc:p},b="wrapper";function m(e){var t=e.components,n=Object(o.a)(e,c);return Object(r.b)(b,Object(a.a)({},d,n,{components:t,mdxType:"MDXLayout"}),Object(r.b)("h2",{id:"locking-orientation"},"Locking orientation"),Object(r.b)("p",null,"Orientation can be locked either in the layout level, or across the app via default options. You can declare orientations you'd like your app to support in default options."),Object(r.b)(i.a,{defaultValue:"defaultOptions",values:[{label:"Locking orientation in default options",value:"defaultOptions"},{label:"Locking root layout orientation",value:"root"},{label:"Showing landscape modal",value:"modal"}],mdxType:"Tabs"},Object(r.b)(l.a,{value:"defaultOptions",mdxType:"TabItem"},Object(r.b)("p",null,"Setting orientation in default options will affect all screens in all hierarchy levels. It's still possible to override orientation for specific screens."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setDefaultOptions({\n  layout: {\n    orientation: ['portrait'],\n  },\n});\n"))),Object(r.b)(l.a,{value:"root",mdxType:"TabItem"},Object(r.b)("p",null,"Applying orientation in the root level will affect all screens in the root hierarchy level. It ",Object(r.b)("strong",{parentName:"p"},"will not")," apply to modals."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setRoot({\n  root: {\n    bottomTabs: {\n      options: {\n        layout: {\n          orientation: ['portrait']\n        }\n      },\n      children: [\n        ...\n      ]\n    }\n  }\n});\n"))),Object(r.b)(l.a,{value:"modal",mdxType:"TabItem"},Object(r.b)("p",null,"The following example demonstrates how to show a modal in landscape orientation."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-js"},"Navigation.showModal({\n  component: {\n    name: 'VideoPlayer'\n    options: {\n      layout: {\n        orientation: ['landscape']\n      }\n    }\n  }\n});\n")))),Object(r.b)("h2",{id:"changing-orientation-dynamically"},"Changing orientation dynamically"),Object(r.b)("p",null,"Changing orientation dynamically through ",Object(r.b)("inlineCode",{parentName:"p"},"Navigation.mergeOption()")," is supported only on Android."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-js"},"Navigation.mergeOptions(this.props.componentId, {\n  layout: {\n    orientation: ['landscape'],\n  },\n});\n")))}m.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return p})),n.d(t,"b",(function(){return f}));var a=n(0),o=n.n(a);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)n=r[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var s=o.a.createContext({}),u=function(e){var t=o.a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},p=function(e){var t=u(e.components);return o.a.createElement(s.Provider,{value:t},e.children)},d="mdxType",b={inlineCode:"code",wrapper:function(e){var t=e.children;return o.a.createElement(o.a.Fragment,{},t)}},m=o.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,r=e.originalType,i=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),p=u(n),d=a,m=p["".concat(i,".").concat(d)]||p[d]||b[d]||r;return n?o.a.createElement(m,l(l({ref:t},s),{},{components:n})):o.a.createElement(m,l({ref:t},s))}));function f(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var r=n.length,i=new Array(r);i[0]=m;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l[d]="string"==typeof e?e:a,i[1]=l;for(var s=2;s<r;s++)i[s]=n[s];return o.a.createElement.apply(null,i)}return o.a.createElement.apply(null,n)}m.displayName="MDXCreateElement"},628:function(e,t,n){"use strict";function a(e){var t,n,o="";if("string"==typeof e||"number"==typeof e)o+=e;else if("object"==typeof e)if(Array.isArray(e))for(t=0;t<e.length;t++)e[t]&&(n=a(e[t]))&&(o&&(o+=" "),o+=n);else for(t in e)e[t]&&(o&&(o+=" "),o+=t);return o}t.a=function(){for(var e,t,n=0,o="";n<arguments.length;)(e=arguments[n++])&&(t=a(e))&&(o&&(o+=" "),o+=t);return o}},629:function(e,t,n){"use strict";var a=n(0),o=n(630);t.a=function(){var e=Object(a.useContext)(o.a);if(null==e)throw new Error("`useUserPreferencesContext` is used outside of `Layout` Component.");return e}},630:function(e,t,n){"use strict";var a=n(0),o=Object(a.createContext)(void 0);t.a=o},631:function(e,t,n){"use strict";var a=n(0),o=n.n(a),r=n(629),i=n(628),l=n(55),c=n.n(l);var s=37,u=39;t.a=function(e){var t=e.lazy,n=e.block,l=e.defaultValue,p=e.values,d=e.groupId,b=e.className,m=Object(r.a)(),f=m.tabGroupChoices,v=m.setTabGroupChoices,y=Object(a.useState)(l),g=y[0],O=y[1],h=a.Children.toArray(e.children),j=[];if(null!=d){var w=f[d];null!=w&&w!==g&&p.some((function(e){return e.value===w}))&&O(w)}var N=function(e){var t=e.target,n=j.indexOf(t),a=h[n].props.value;O(a),null!=d&&(v(d,a),setTimeout((function(){var e,n,a,o,r,i,l,s;(e=t.getBoundingClientRect(),n=e.top,a=e.left,o=e.bottom,r=e.right,i=window,l=i.innerHeight,s=i.innerWidth,n>=0&&r<=s&&o<=l&&a>=0)||(t.scrollIntoView({block:"center",behavior:"smooth"}),t.classList.add(c.a.tabItemActive),setTimeout((function(){return t.classList.remove(c.a.tabItemActive)}),2e3))}),150))},x=function(e){var t,n;switch(e.keyCode){case u:var a=j.indexOf(e.target)+1;n=j[a]||j[0];break;case s:var o=j.indexOf(e.target)-1;n=j[o]||j[j.length-1]}null===(t=n)||void 0===t||t.focus()};return o.a.createElement("div",{className:"tabs-container"},o.a.createElement("ul",{role:"tablist","aria-orientation":"horizontal",className:Object(i.a)("tabs",{"tabs--block":n},b)},p.map((function(e){var t=e.value,n=e.label;return o.a.createElement("li",{role:"tab",tabIndex:g===t?0:-1,"aria-selected":g===t,className:Object(i.a)("tabs__item",c.a.tabItem,{"tabs__item--active":g===t}),key:t,ref:function(e){return j.push(e)},onKeyDown:x,onFocus:N,onClick:N},n)}))),t?Object(a.cloneElement)(h.filter((function(e){return e.props.value===g}))[0],{className:"margin-vert--md"}):o.a.createElement("div",{className:"margin-vert--md"},h.map((function(e,t){return Object(a.cloneElement)(e,{key:t,hidden:e.props.value!==g})}))))}},632:function(e,t,n){"use strict";var a=n(0),o=n.n(a);t.a=function(e){var t=e.children,n=e.hidden,a=e.className;return o.a.createElement("div",{role:"tabpanel",hidden:n,className:a},t)}}}]);