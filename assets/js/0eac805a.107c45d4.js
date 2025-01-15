(window.webpackJsonp=window.webpackJsonp||[]).push([[41],{104:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return l})),n.d(t,"metadata",(function(){return b})),n.d(t,"toc",(function(){return c})),n.d(t,"default",(function(){return m}));var a=n(3),r=n(7),i=(n(0),n(627));const o=["components"],l={id:"layout-component",title:"Component",sidebar_label:"Component"},b={unversionedId:"api/layout-component",id:"version-7.7.0/api/layout-component",isDocsHomePage:!1,title:"Component",description:"`js",source:"@site/versioned_docs/version-7.7.0/api/layout-component.mdx",slug:"/api/layout-component",permalink:"/react-native-navigation/7.7.0/api/layout-component",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.7.0/api/layout-component.mdx",version:"7.7.0",sidebar_label:"Component",sidebar:"version-7.7.0/api",previous:{title:"Layout",permalink:"/react-native-navigation/7.7.0/api/layout-layout"},next:{title:"Stack",permalink:"/react-native-navigation/7.7.0/api/layout-stack"}},c=[{value:"<code>name</code>",id:"name",children:[]},{value:"<code>id</code>",id:"id",children:[]},{value:"<code>options</code>",id:"options",children:[]},{value:"<code>alignment</code>",id:"alignment",children:[]},{value:"<code>waitForRender</code>",id:"waitforrender",children:[]},{value:"<code>passProps</code>",id:"passprops",children:[]}],p={toc:c},d="wrapper";function m(e){let{components:t}=e,n=Object(r.a)(e,o);return Object(i.b)(d,Object(a.a)({},p,n,{components:t,mdxType:"MDXLayout"}),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},'{\n  name: "MyRegisteredComponent";\n}\n')),Object(i.b)("h2",{id:"name"},Object(i.b)("inlineCode",{parentName:"h2"},"name")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"string"),Object(i.b)("td",{parentName:"tr",align:null},"Yes"),Object(i.b)("td",{parentName:"tr",align:null},"Key used when registering the component with ",Object(i.b)("inlineCode",{parentName:"td"},"Navigation.registerComponent"),".")))),Object(i.b)("h2",{id:"id"},Object(i.b)("inlineCode",{parentName:"h2"},"id")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"string"),Object(i.b)("td",{parentName:"tr",align:null},"No"),Object(i.b)("td",{parentName:"tr",align:null},"Unique id used to interact with the view via the Navigation api, usually ",Object(i.b)("inlineCode",{parentName:"td"},"Navigation.mergeOptions")," which accepts the componentId as it's first argument.")))),Object(i.b)("h2",{id:"options"},Object(i.b)("inlineCode",{parentName:"h2"},"options")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},Object(i.b)("a",{parentName:"td",href:"/react-native-navigation/7.7.0/api/options-root"},"Options")),Object(i.b)("td",{parentName:"tr",align:null},"No"),Object(i.b)("td",{parentName:"tr",align:null},"dynamic options for the component")))),Object(i.b)("h2",{id:"alignment"},Object(i.b)("inlineCode",{parentName:"h2"},"alignment")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"enum('center', 'fill')"),Object(i.b)("td",{parentName:"tr",align:null},"No"),Object(i.b)("td",{parentName:"tr",align:null},"This option is relevant only to title components. ",Object(i.b)("inlineCode",{parentName:"td"},"fill")," will make the component stretch and consume all available space in the TopBar while ",Object(i.b)("inlineCode",{parentName:"td"},"center")," will center it in the middle of the TopBar. ",Object(i.b)("inlineCode",{parentName:"td"},"center")," is the default option in iOS while ",Object(i.b)("inlineCode",{parentName:"td"},"fill")," is the default for Android.")))),Object(i.b)("h2",{id:"waitforrender"},Object(i.b)("inlineCode",{parentName:"h2"},"waitForRender")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"boolean"),Object(i.b)("td",{parentName:"tr",align:null},"No"),Object(i.b)("td",{parentName:"tr",align:null},"Wait for this component to fully render before showing the screen.")))),Object(i.b)("p",null,"This option is useful for ensuring that both a child screen pushed into the stack and all of the TopBar components (title, background and buttons) are displayed to the user at the same time."),Object(i.b)("p",null,"To enable this option, ",Object(i.b)("inlineCode",{parentName:"p"},"waitForRender")," in the relevant screen animation option needs to be enabled as well."),Object(i.b)("div",{className:"admonition admonition-caution alert alert--warning"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"16",height:"16",viewBox:"0 0 16 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M8.893 1.5c-.183-.31-.52-.5-.887-.5s-.703.19-.886.5L.138 13.499a.98.98 0 0 0 0 1.001c.193.31.53.501.886.501h13.964c.367 0 .704-.19.877-.5a1.03 1.03 0 0 0 .01-1.002L8.893 1.5zm.133 11.497H6.987v-2.003h2.039v2.003zm0-3.004H6.987V5.987h2.039v4.006z"}))),"caution")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"This option might introduce delays when pushing screens and should be used with caution."))),Object(i.b)("h2",{id:"passprops"},Object(i.b)("inlineCode",{parentName:"h2"},"passProps")),Object(i.b)("table",null,Object(i.b)("thead",{parentName:"table"},Object(i.b)("tr",{parentName:"thead"},Object(i.b)("th",{parentName:"tr",align:null},"Type"),Object(i.b)("th",{parentName:"tr",align:null},"Required"),Object(i.b)("th",{parentName:"tr",align:null},"Description"))),Object(i.b)("tbody",{parentName:"table"},Object(i.b)("tr",{parentName:"tbody"},Object(i.b)("td",{parentName:"tr",align:null},"object"),Object(i.b)("td",{parentName:"tr",align:null},"No"),Object(i.b)("td",{parentName:"tr",align:null},"A JavaScript object with props accessible inside the component using ",Object(i.b)("inlineCode",{parentName:"td"},"this.props"),".")))))}m.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return d})),n.d(t,"b",(function(){return O}));var a=n(0),r=n.n(a);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function b(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var c=r.a.createContext({}),p=function(e){var t=r.a.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},d=function(e){var t=p(e.components);return r.a.createElement(c.Provider,{value:t},e.children)},m="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.a.createElement(r.a.Fragment,{},t)}},s=r.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,i=e.originalType,o=e.parentName,c=b(e,["components","mdxType","originalType","parentName"]),d=p(n),m=a,s=d["".concat(o,".").concat(m)]||d[m]||u[m]||i;return n?r.a.createElement(s,l(l({ref:t},c),{},{components:n})):r.a.createElement(s,l({ref:t},c))}));function O(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var i=n.length,o=new Array(i);o[0]=s;var l={};for(var b in t)hasOwnProperty.call(t,b)&&(l[b]=t[b]);l.originalType=e,l[m]="string"==typeof e?e:a,o[1]=l;for(var c=2;c<i;c++)o[c]=n[c];return r.a.createElement.apply(null,o)}return r.a.createElement.apply(null,n)}s.displayName="MDXCreateElement"}}]);