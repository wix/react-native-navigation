(window.webpackJsonp=window.webpackJsonp||[]).push([[395],{466:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return b})),n.d(t,"metadata",(function(){return p})),n.d(t,"toc",(function(){return s})),n.d(t,"default",(function(){return d}));var a=n(3),r=n(7),o=(n(0),n(627)),l=n(631),c=n(632);const i=["components"],b={id:"stack",title:"Stack",sidebar_label:"Stack"},p={unversionedId:"api/stack",id:"version-6.12.2/api/stack",isDocsHomePage:!1,title:"Stack",description:"push()",source:"@site/versioned_docs/version-6.12.2/api/api-stack.mdx",slug:"/api/stack",permalink:"/react-native-navigation/6.12.2/api/stack",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-6.12.2/api/api-stack.mdx",version:"6.12.2",sidebar_label:"Stack",sidebar:"version-6.12.2/api",previous:{title:"Root",permalink:"/react-native-navigation/6.12.2/api/root"},next:{title:"Modal",permalink:"/react-native-navigation/6.12.2/api/modal"}},s=[{value:"<code>push()</code>",id:"push",children:[]},{value:"<code>pop()</code>",id:"pop",children:[]},{value:"<code>popToRoot()</code>",id:"poptoroot",children:[]},{value:"<code>popTo()</code>",id:"popto",children:[]},{value:"<code>setStackRoot()</code>",id:"setstackroot",children:[]}],u={toc:s},m="wrapper";function d(e){let{components:t}=e,n=Object(r.a)(e,i);return Object(o.b)(m,Object(a.a)({},u,n,{components:t,mdxType:"MDXLayout"}),Object(o.b)("h2",{id:"push"},Object(o.b)("inlineCode",{parentName:"h2"},"push()")),Object(o.b)("p",null,"Push a screen into the stack and update the display according to the screen options."),Object(o.b)("h4",{id:"parameters"},"Parameters"),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Name"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Description"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"componentId"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},"string"),Object(o.b)("td",{parentName:"tr",align:null},"The componentId of a screen pushed into the stack, or the id of the stack.")),Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"layout"),Object(o.b)("td",{parentName:"tr",align:null},"No"),Object(o.b)("td",{parentName:"tr",align:null},"Layout"),Object(o.b)("td",{parentName:"tr",align:null},"The layout being pushed into the stack. Any type of layout (except stack) can be pushed into stacks. Typically, Component layout is pushed into stacks but it's possible to push SideMenu or BottomTabs as well.")))),Object(o.b)("h4",{id:"example"},"Example"),Object(o.b)(l.a,{defaultValue:"component",values:[{label:"Component",value:"component"},{label:"Update options on push",value:"push"},{label:"Push other layouts",value:"otherLayout"}],mdxType:"Tabs"},Object(o.b)(c.a,{value:"component",mdxType:"TabItem"},"The most common use case - push a single React component.",Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.push(this.props.componentId, {\n  component: {\n    name: 'example.PushedScreen'\n  }\n});\n"))),Object(o.b)(c.a,{value:"push",mdxType:"TabItem"},"Options are applied when the screen becomes visible.",Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.push(this.props.componentId, {\n  component: {\n    name: 'example.PushedScreen',\n    options: {\n      topBar: {\n        title: {\n          text: 'Pushed screen title'\n        }\n      }\n    }\n  }\n});\n"))),Object(o.b)(c.a,{value:"otherLayout",mdxType:"TabItem"},"Any layout type can be pushed. In this example we push a SideMenu layout.",Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.push(this.props.componentId, {\n  sideMenu: {\n    left: {\n      component: {\n        name: 'drawerScreen'\n      }\n    },\n    center: {\n      component: {\n        name: 'centerScreen'\n      }\n    }\n  }\n});\n")))),Object(o.b)("h2",{id:"pop"},Object(o.b)("inlineCode",{parentName:"h2"},"pop()")),Object(o.b)("p",null,"Pop the top screen from the stack."),Object(o.b)("h4",{id:"parameters-1"},"Parameters"),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Name"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Description"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"componentId"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},"string"),Object(o.b)("td",{parentName:"tr",align:null},"The componentId of a screen pushed into the stack, or the stack id.")),Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"mergeOptions"),Object(o.b)("td",{parentName:"tr",align:null},"No"),Object(o.b)("td",{parentName:"tr",align:null},Object(o.b)("a",{parentName:"td",href:"/react-native-navigation/6.12.2/api/options-root"},"Options")),Object(o.b)("td",{parentName:"tr",align:null},"Options to be merged before popping the screen (optional).")))),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.pop(this.props.componentId);\n")),Object(o.b)("h2",{id:"poptoroot"},Object(o.b)("inlineCode",{parentName:"h2"},"popToRoot()")),Object(o.b)("p",null,"Pop all screens pushed into the stack."),Object(o.b)("h4",{id:"parameters-2"},"Parameters"),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Name"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Description"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"componentId"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},"string"),Object(o.b)("td",{parentName:"tr",align:null},"The componentId of a screen pushed into the stack, or the stack id.")),Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"mergeOptions"),Object(o.b)("td",{parentName:"tr",align:null},"No"),Object(o.b)("td",{parentName:"tr",align:null},Object(o.b)("a",{parentName:"td",href:"/react-native-navigation/6.12.2/api/options-root"},"Options")),Object(o.b)("td",{parentName:"tr",align:null},"Options to be merged before popping the screen (optional).")))),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.popToRoot(this.props.componentId);\n")),Object(o.b)("h2",{id:"popto"},Object(o.b)("inlineCode",{parentName:"h2"},"popTo()")),Object(o.b)("p",null,"Pop the stack to a given component."),Object(o.b)("h4",{id:"parameters-3"},"Parameters"),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Name"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Description"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"componentId"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},"string"),Object(o.b)("td",{parentName:"tr",align:null},"The destination componentId")),Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"mergeOptions"),Object(o.b)("td",{parentName:"tr",align:null},"No"),Object(o.b)("td",{parentName:"tr",align:null},Object(o.b)("a",{parentName:"td",href:"/react-native-navigation/6.12.2/api/options-root"},"Options")),Object(o.b)("td",{parentName:"tr",align:null},"Options to be merged before popping the screen (optional).")))),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.popTo(componentId);\n")),Object(o.b)("h2",{id:"setstackroot"},Object(o.b)("inlineCode",{parentName:"h2"},"setStackRoot()")),Object(o.b)("p",null,"Reset the stack to the given layout (accepts multiple children)."),Object(o.b)("h4",{id:"parameters-4"},"Parameters"),Object(o.b)("table",null,Object(o.b)("thead",{parentName:"table"},Object(o.b)("tr",{parentName:"thead"},Object(o.b)("th",{parentName:"tr",align:null},"Name"),Object(o.b)("th",{parentName:"tr",align:null},"Required"),Object(o.b)("th",{parentName:"tr",align:null},"Type"),Object(o.b)("th",{parentName:"tr",align:null},"Description"))),Object(o.b)("tbody",{parentName:"table"},Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"componentId"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},"string"),Object(o.b)("td",{parentName:"tr",align:null},"The componentId of a screen pushed into the stack, or the stack id.")),Object(o.b)("tr",{parentName:"tbody"},Object(o.b)("td",{parentName:"tr",align:null},"layout"),Object(o.b)("td",{parentName:"tr",align:null},"Yes"),Object(o.b)("td",{parentName:"tr",align:null},Object(o.b)("a",{parentName:"td",href:"/react-native-navigation/6.12.2/api/layout-layout"},"Layout")," or ",Object(o.b)("a",{parentName:"td",href:"/react-native-navigation/6.12.2/api/layout-layout"},"Layout"),"[]"),Object(o.b)("td",{parentName:"tr",align:null},"A single Component or array of components.")))),Object(o.b)("h4",{id:"example-1"},"Example"),Object(o.b)(l.a,{defaultValue:"single",values:[{label:"Single child",value:"single"},{label:"Multiple children",value:"multiple"}],mdxType:"Tabs"},Object(o.b)(c.a,{value:"single",mdxType:"TabItem"},Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setStackRoot(this.props.componentId, {\n  component: {\n    name: 'example.NewRootScreen'\n  }\n});\n"))),Object(o.b)(c.a,{value:"multiple",mdxType:"TabItem"},Object(o.b)("p",null,"In the example below we reset the stack with two components. The first one will be the root component and the second (",Object(o.b)("inlineCode",{parentName:"p"},"PushedScreen"),") will be displayed. Pressing the back button (either hardware or software) will pop it, revealing the root component - ",Object(o.b)("inlineCode",{parentName:"p"},"NewRootScreen"),"."),Object(o.b)("pre",null,Object(o.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setStackRoot(this.props.componentId, [\n  {\n    component: {\n      name: 'NewRootScreen',\n    }\n  },\n  {\n    component: {\n      name: 'PushedScreen',\n    }\n  }\n]);\n")))))}d.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return s})),n.d(t,"b",(function(){return O}));var a=n(0),r=n.n(a);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function l(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function c(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?l(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var b=r.a.createContext({}),p=function(e){var t=r.a.useContext(b),n=t;return e&&(n="function"==typeof e?e(t):c(c({},t),e)),n},s=function(e){var t=p(e.components);return r.a.createElement(b.Provider,{value:t},e.children)},u="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return r.a.createElement(r.a.Fragment,{},t)}},d=r.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,l=e.parentName,b=i(e,["components","mdxType","originalType","parentName"]),s=p(n),u=a,d=s["".concat(l,".").concat(u)]||s[u]||m[u]||o;return n?r.a.createElement(d,c(c({ref:t},b),{},{components:n})):r.a.createElement(d,c({ref:t},b))}));function O(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,l=new Array(o);l[0]=d;var c={};for(var i in t)hasOwnProperty.call(t,i)&&(c[i]=t[i]);c.originalType=e,c[u]="string"==typeof e?e:a,l[1]=c;for(var b=2;b<o;b++)l[b]=n[b];return r.a.createElement.apply(null,l)}return r.a.createElement.apply(null,n)}d.displayName="MDXCreateElement"},628:function(e,t,n){"use strict";function a(e){var t,n,r="";if("string"==typeof e||"number"==typeof e)r+=e;else if("object"==typeof e)if(Array.isArray(e))for(t=0;t<e.length;t++)e[t]&&(n=a(e[t]))&&(r&&(r+=" "),r+=n);else for(t in e)e[t]&&(r&&(r+=" "),r+=t);return r}t.a=function(){for(var e,t,n=0,r="";n<arguments.length;)(e=arguments[n++])&&(t=a(e))&&(r&&(r+=" "),r+=t);return r}},629:function(e,t,n){"use strict";var a=n(0),r=n(630);t.a=function(){const e=Object(a.useContext)(r.a);if(null==e)throw new Error("`useUserPreferencesContext` is used outside of `Layout` Component.");return e}},630:function(e,t,n){"use strict";var a=n(0);const r=Object(a.createContext)(void 0);t.a=r},631:function(e,t,n){"use strict";var a=n(0),r=n.n(a),o=n(629),l=n(628),c=n(55),i=n.n(c);const b=37,p=39;t.a=function(e){const{lazy:t,block:n,defaultValue:c,values:s,groupId:u,className:m}=e,{tabGroupChoices:d,setTabGroupChoices:O}=Object(o.a)(),[j,h]=Object(a.useState)(c),g=a.Children.toArray(e.children),N=[];if(null!=u){const e=d[u];null!=e&&e!==j&&s.some((t=>t.value===e))&&h(e)}const f=e=>{const t=e.target,n=N.indexOf(t),a=g[n].props.value;h(a),null!=u&&(O(u,a),setTimeout((()=>{(function(e){const{top:t,left:n,bottom:a,right:r}=e.getBoundingClientRect(),{innerHeight:o,innerWidth:l}=window;return t>=0&&r<=l&&a<=o&&n>=0})(t)||(t.scrollIntoView({block:"center",behavior:"smooth"}),t.classList.add(i.a.tabItemActive),setTimeout((()=>t.classList.remove(i.a.tabItemActive)),2e3))}),150))},v=e=>{var t;let n;switch(e.keyCode){case p:{const t=N.indexOf(e.target)+1;n=N[t]||N[0];break}case b:{const t=N.indexOf(e.target)-1;n=N[t]||N[N.length-1];break}}null===(t=n)||void 0===t||t.focus()};return r.a.createElement("div",{className:"tabs-container"},r.a.createElement("ul",{role:"tablist","aria-orientation":"horizontal",className:Object(l.a)("tabs",{"tabs--block":n},m)},s.map((e=>{let{value:t,label:n}=e;return r.a.createElement("li",{role:"tab",tabIndex:j===t?0:-1,"aria-selected":j===t,className:Object(l.a)("tabs__item",i.a.tabItem,{"tabs__item--active":j===t}),key:t,ref:e=>N.push(e),onKeyDown:v,onFocus:f,onClick:f},n)}))),t?Object(a.cloneElement)(g.filter((e=>e.props.value===j))[0],{className:"margin-vert--md"}):r.a.createElement("div",{className:"margin-vert--md"},g.map(((e,t)=>Object(a.cloneElement)(e,{key:t,hidden:e.props.value!==j})))))}},632:function(e,t,n){"use strict";var a=n(0),r=n.n(a);t.a=function(e){let{children:t,hidden:n,className:a}=e;return r.a.createElement("div",{role:"tabpanel",hidden:n,className:a},t)}}}]);