(window.webpackJsonp=window.webpackJsonp||[]).push([[503],{576:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return s})),n.d(t,"metadata",(function(){return c})),n.d(t,"toc",(function(){return p})),n.d(t,"default",(function(){return m}));var o=n(3),a=(n(0),n(627)),i=n(631),r=n(632);const s={id:"style-options",title:"Styling with options",sidebar_label:"Options"},c={unversionedId:"docs/style-options",id:"version-7.25.4/docs/style-options",isDocsHomePage:!1,title:"Styling with options",description:"A Screen's look and feel is configured with an Options object. Options can be applied to screens in three ways. Each method of applying options has benefits and draw backs which are important be aware of so we can use the right tool for the job.",source:"@site/versioned_docs/version-7.25.4/docs/style-screens.mdx",slug:"/docs/style-options",permalink:"/react-native-navigation/7.25.4/docs/style-options",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.25.4/docs/style-screens.mdx",version:"7.25.4",sidebar_label:"Options",sidebar:"version-7.25.4/docs",previous:{title:"Overlay",permalink:"/react-native-navigation/7.25.4/docs/overlay"},next:{title:"TopBar Buttons",permalink:"/react-native-navigation/7.25.4/docs/stack-buttons"}},p=[{value:"Command options",id:"command-options",children:[]},{value:"Static options",id:"static-options",children:[{value:"Handle props in static options",id:"handle-props-in-static-options",children:[]}]},{value:"Merge options",id:"merge-options",children:[]}],l={toc:p},d="wrapper";function m(e){let{components:t,...n}=e;return Object(a.b)(d,Object(o.a)({},l,n,{components:t,mdxType:"MDXLayout"}),Object(a.b)("p",null,"A Screen's look and feel is configured with an ",Object(a.b)("a",{parentName:"p",href:"../api/options-root"},"Options")," object. Options can be applied to screens in three ways. Each method of applying options has benefits and draw backs which are important be aware of so we can use the right tool for the job."),Object(a.b)("h2",{id:"command-options"},"Command options"),Object(a.b)("p",null,"Options can be passed to layouts as part of a command. For example when pushing a screen. Options passed in commands are typically dynamic options which are determined at runtime."),Object(a.b)("p",null,"In the example below we're pushing a user profile screen and we'd like show the user name in the title. To do so, we set the title in the component's options."),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/command-options.tsx",file:"./style-screens/command-options.tsx"},"import { Navigation } from 'react-native-navigation';\n\nfunction showUserProfileScreen(user: User) {\n  Navigation.push(componentId, {\n    component: {\n      name: 'ProfileScreen',\n      passProps: { user },\n      options: {\n        topBar: {\n          title: {\n            text: user.name,\n          },\n        },\n      },\n    },\n  });\n}\n")),Object(a.b)("p",null,"Notice how the title is actually inferred from the ",Object(a.b)("inlineCode",{parentName:"p"},"user")," object which is set in ",Object(a.b)("inlineCode",{parentName:"p"},"passProps"),". While this works perfectly fine, declaring the title each time a screen is pushed is a bit tedious and error prone. Later on we'll see a more convenient method to declare dynamic options which are inferred from props."),Object(a.b)("h2",{id:"static-options"},"Static options"),Object(a.b)("p",null,"Static options are options that are declared statically ",Object(a.b)("strong",{parentName:"p"},"on")," the component class. Whenever a screen has a known predefined style, static options should be used as they are evaluated immediately when the screen is created."),Object(a.b)(i.a,{defaultValue:"class",values:[{label:"Class Component",value:"class"},{label:"Functional Component",value:"functional"}],mdxType:"Tabs"},Object(a.b)(r.a,{value:"class",mdxType:"TabItem"},Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/static-options-class.tsx",file:"./style-screens/static-options-class.tsx"},"import { NavigationComponent, Options } from 'react-native-navigation';\n\nclass MyScreen extends NavigationComponent {\n  static options: Options = {\n    topBar: {\n      title: {\n        text: 'My Screen',\n      },\n    },\n  };\n}\n"))),Object(a.b)(r.a,{value:"functional",mdxType:"TabItem"},Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/static-options-fn.tsx",file:"./style-screens/static-options-fn.tsx"},"import { View } from 'react-native';\nimport { NavigationComponentProps, NavigationFunctionComponent } from 'react-native-navigation';\n\nconst MyFunctionalScreen: NavigationFunctionComponent = (props: NavigationComponentProps) => {\n  return <View />;\n};\n\nMyFunctionalScreen.options = {\n  topBar: {\n    title: {\n      text: 'My Screen',\n    },\n  },\n};\n")))),Object(a.b)("h3",{id:"handle-props-in-static-options"},"Handle props in static options"),Object(a.b)("p",null,"Sometimes a screen's style includes properties defined in the calling scope where the screen is used. Like in the user profile screen we've seen above where the the user name is used as the TopBar title. As the user name is unique for each profile, it can't be defined explicitly in the static options."),Object(a.b)("p",null,"Luckily, we can access props from static options and set the title from static options! Let's see how this is done:"),Object(a.b)(i.a,{defaultValue:"class",values:[{label:"Class Component",value:"class"},{label:"Functional Component",value:"functional"}],mdxType:"Tabs"},Object(a.b)(r.a,{value:"class",mdxType:"TabItem"},Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/static-options-props-class.tsx",file:"./style-screens/static-options-props-class.tsx"},"import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';\n\ninterface Props extends NavigationComponentProps {\n  order: OrderDetails;\n}\n\nclass OrderScreen extends NavigationComponent<Props> {\n  static options(props: Props): Options {\n    return {\n      topBar: {\n        title: {\n          text: props.order.orderId,\n        },\n      },\n    };\n  }\n}\n"))),Object(a.b)(r.a,{value:"functional",mdxType:"TabItem"},Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/static-options-props-fn.tsx",file:"./style-screens/static-options-props-fn.tsx"},"import { View } from 'react-native';\nimport { NavigationComponentProps, NavigationFunctionComponent } from 'react-native-navigation';\n\ninterface Props extends NavigationComponentProps {\n  order: OrderDetails;\n}\n\nconst OrderScreen: NavigationFunctionComponent<Props> = (props: Props) => {\n  return <View />;\n};\n\nOrderScreen.options = (props: Props) => {\n  return {\n    topBar: {\n      title: {\n        text: props.order.orderId,\n      },\n    },\n  };\n};\n")))),Object(a.b)("p",null,"Following this approach we can determine options that are dependent on other external factors, such as experiments or A/B tests."),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/static-options-experiments.tsx",file:"./style-screens/static-options-experiments.tsx"},"import { NavigationComponent, Options } from 'react-native-navigation';\n\nclass ExperimentScreen extends NavigationComponent {\n  static options(): Options {\n    const ExperimentsManager = require('./ExperimentsManager');\n    const food = ExperimentsManager.isActive('VeganExperiment') ? 'Tofu' : 'Hamburger';\n\n    return {\n      topBar: {\n        title: {\n          text: `Hello ${food}!`,\n        },\n      },\n    };\n  }\n}\n")),Object(a.b)("h2",{id:"merge-options"},"Merge options"),Object(a.b)("p",null,"The ",Object(a.b)("inlineCode",{parentName:"p"},"mergeOptions")," command is used to update a layout's style. Keep in mind that the merge happens in native, and not ins JS. This meaning that if ",Object(a.b)("inlineCode",{parentName:"p"},"mergeOptions")," is called to update a screen's options, the static options declared on the React Component are not affected by mergeOptions."),Object(a.b)("p",null,"The following example shows how to update a TopBar background color to red."),Object(a.b)("pre",null,Object(a.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-screens/merge-options.tsx",file:"./style-screens/merge-options.tsx"},"import { Navigation } from 'react-native-navigation';\n\nNavigation.mergeOptions(componentId, {\n  topBar: {\n    background: {\n      color: 'red',\n    },\n  },\n});\n")),Object(a.b)("hr",null),Object(a.b)("div",{className:"admonition admonition-warning alert alert--danger"},Object(a.b)("div",{parentName:"div",className:"admonition-heading"},Object(a.b)("h5",{parentName:"div"},Object(a.b)("span",{parentName:"h5",className:"admonition-icon"},Object(a.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"12",height:"16",viewBox:"0 0 12 16"},Object(a.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M5.05.31c.81 2.17.41 3.38-.52 4.31C3.55 5.67 1.98 6.45.9 7.98c-1.45 2.05-1.7 6.53 3.53 7.7-2.2-1.16-2.67-4.52-.3-6.61-.61 2.03.53 3.33 1.94 2.86 1.39-.47 2.3.53 2.27 1.67-.02.78-.31 1.44-1.13 1.81 3.42-.59 4.78-3.42 4.78-5.56 0-2.84-2.53-3.22-1.25-5.61-1.52.13-2.03 1.13-1.89 2.75.09 1.08-1.02 1.8-1.86 1.33-.67-.41-.66-1.19-.06-1.78C8.18 5.31 8.68 2.45 5.05.32L5.03.3l.02.01z"}))),"warning")),Object(a.b)("div",{parentName:"div",className:"admonition-content"},Object(a.b)("p",{parentName:"div"},Object(a.b)("strong",{parentName:"p"},"Avoid using mergeOptions in a screen's constructor or in componentDidMount!")))),Object(a.b)("p",null,"When a screen first appears, it's ",Object(a.b)("inlineCode",{parentName:"p"},"constructor")," and ",Object(a.b)("inlineCode",{parentName:"p"},"componentDidMount")," functions are called followed by a ",Object(a.b)("inlineCode",{parentName:"p"},"componentDidAppear")," event. (read more about screen lifecycle ",Object(a.b)("a",{parentName:"p",href:"/react-native-navigation/7.25.4/docs/screen-lifecycle"},"here"),")."),Object(a.b)("p",null,"Developers might be tempted to call ",Object(a.b)("inlineCode",{parentName:"p"},"mergeOptions")," in constructor or componentDidMount to update options. Calling ",Object(a.b)("inlineCode",{parentName:"p"},"mergeOptions")," before the ",Object(a.b)("inlineCode",{parentName:"p"},"componentDidAppear")," event has been received has two very negative effects:"),Object(a.b)("ol",null,Object(a.b)("li",{parentName:"ol"},"Updating certain options can trigger additional layout and draw cycles which can have serious impact on performance."),Object(a.b)("li",{parentName:"ol"},"Options applied dynamically with the ",Object(a.b)("inlineCode",{parentName:"li"},"mergeOptions")," command might be handled in native ",Object(a.b)("strong",{parentName:"li"},"after")," the screen has already appeared. This will result in noticeable flickering and artifacts as options are updated after the initial options have been applied and are visible to the user.")))}m.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return d})),n.d(t,"b",(function(){return f}));var o=n(0),a=n.n(o);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,o)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,o,a=function(e,t){if(null==e)return{};var n,o,a={},i=Object.keys(e);for(o=0;o<i.length;o++)n=i[o],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(o=0;o<i.length;o++)n=i[o],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var p=a.a.createContext({}),l=function(e){var t=a.a.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):s(s({},t),e)),n},d=function(e){var t=l(e.components);return a.a.createElement(p.Provider,{value:t},e.children)},m="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return a.a.createElement(a.a.Fragment,{},t)}},b=a.a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,r=e.parentName,p=c(e,["components","mdxType","originalType","parentName"]),d=l(n),m=o,b=d["".concat(r,".").concat(m)]||d[m]||u[m]||i;return n?a.a.createElement(b,s(s({ref:t},p),{},{components:n})):a.a.createElement(b,s({ref:t},p))}));function f(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,r=new Array(i);r[0]=b;var s={};for(var c in t)hasOwnProperty.call(t,c)&&(s[c]=t[c]);s.originalType=e,s[m]="string"==typeof e?e:o,r[1]=s;for(var p=2;p<i;p++)r[p]=n[p];return a.a.createElement.apply(null,r)}return a.a.createElement.apply(null,n)}b.displayName="MDXCreateElement"},628:function(e,t,n){"use strict";function o(e){var t,n,a="";if("string"==typeof e||"number"==typeof e)a+=e;else if("object"==typeof e)if(Array.isArray(e))for(t=0;t<e.length;t++)e[t]&&(n=o(e[t]))&&(a&&(a+=" "),a+=n);else for(t in e)e[t]&&(a&&(a+=" "),a+=t);return a}t.a=function(){for(var e,t,n=0,a="";n<arguments.length;)(e=arguments[n++])&&(t=o(e))&&(a&&(a+=" "),a+=t);return a}},629:function(e,t,n){"use strict";var o=n(0),a=n(630);t.a=function(){const e=Object(o.useContext)(a.a);if(null==e)throw new Error("`useUserPreferencesContext` is used outside of `Layout` Component.");return e}},630:function(e,t,n){"use strict";var o=n(0);const a=Object(o.createContext)(void 0);t.a=a},631:function(e,t,n){"use strict";var o=n(0),a=n.n(o),i=n(629),r=n(628),s=n(55),c=n.n(s);const p=37,l=39;t.a=function(e){const{lazy:t,block:n,defaultValue:s,values:d,groupId:m,className:u}=e,{tabGroupChoices:b,setTabGroupChoices:f}=Object(i.a)(),[h,v]=Object(o.useState)(s),g=o.Children.toArray(e.children),O=[];if(null!=m){const e=b[m];null!=e&&e!==h&&d.some((t=>t.value===e))&&v(e)}const y=e=>{const t=e.target,n=O.indexOf(t),o=g[n].props.value;v(o),null!=m&&(f(m,o),setTimeout((()=>{(function(e){const{top:t,left:n,bottom:o,right:a}=e.getBoundingClientRect(),{innerHeight:i,innerWidth:r}=window;return t>=0&&a<=r&&o<=i&&n>=0})(t)||(t.scrollIntoView({block:"center",behavior:"smooth"}),t.classList.add(c.a.tabItemActive),setTimeout((()=>t.classList.remove(c.a.tabItemActive)),2e3))}),150))},j=e=>{var t;let n;switch(e.keyCode){case l:{const t=O.indexOf(e.target)+1;n=O[t]||O[0];break}case p:{const t=O.indexOf(e.target)-1;n=O[t]||O[O.length-1];break}}null===(t=n)||void 0===t||t.focus()};return a.a.createElement("div",{className:"tabs-container"},a.a.createElement("ul",{role:"tablist","aria-orientation":"horizontal",className:Object(r.a)("tabs",{"tabs--block":n},u)},d.map((e=>{let{value:t,label:n}=e;return a.a.createElement("li",{role:"tab",tabIndex:h===t?0:-1,"aria-selected":h===t,className:Object(r.a)("tabs__item",c.a.tabItem,{"tabs__item--active":h===t}),key:t,ref:e=>O.push(e),onKeyDown:j,onFocus:y,onClick:y},n)}))),t?Object(o.cloneElement)(g.filter((e=>e.props.value===h))[0],{className:"margin-vert--md"}):a.a.createElement("div",{className:"margin-vert--md"},g.map(((e,t)=>Object(o.cloneElement)(e,{key:t,hidden:e.props.value!==h})))))}},632:function(e,t,n){"use strict";var o=n(0),a=n.n(o);t.a=function(e){let{children:t,hidden:n,className:o}=e;return a.a.createElement("div",{role:"tabpanel",hidden:n,className:o},t)}}}]);