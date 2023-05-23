(window.webpackJsonp=window.webpackJsonp||[]).push([[161],{227:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return s})),n.d(t,"metadata",(function(){return c})),n.d(t,"toc",(function(){return l})),n.d(t,"default",(function(){return m}));var o=n(3),a=n(7),r=(n(0),n(627)),i=["components"],s={id:"style-theme",title:"Themes",sidebar_label:"Themes"},c={unversionedId:"docs/style-theme",id:"version-7.32.1/docs/style-theme",isDocsHomePage:!1,title:"Themes",description:"A theme is a type of style which is applied to the entire app. It allows you to declare",source:"@site/versioned_docs/version-7.32.1/docs/style-theme.mdx",slug:"/docs/style-theme",permalink:"/react-native-navigation/7.32.1/docs/style-theme",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.32.1/docs/style-theme.mdx",version:"7.32.1",sidebar_label:"Themes",sidebar:"version-7.32.1/docs",previous:{title:"TopBar Buttons",permalink:"/react-native-navigation/7.32.1/docs/stack-buttons"},next:{title:"StatusBar",permalink:"/react-native-navigation/7.32.1/docs/style-statusBar"}},l=[{value:"Dark mode",id:"dark-mode",children:[]},{value:"Conditional themes with Options Processor",id:"conditional-themes-with-options-processor",children:[]},{value:"Conditional themes with Layout Processor",id:"conditional-themes-with-layout-processor",children:[]},{value:"Changing theme dynamically",id:"changing-theme-dynamically",children:[]}],p={toc:l},d="wrapper";function m(e){var t=e.components,n=Object(a.a)(e,i);return Object(r.b)(d,Object(o.a)({},p,n,{components:t,mdxType:"MDXLayout"}),Object(r.b)("p",null,"A theme is a type of style which is applied to the entire app. It allows you to declare\na consistent style to all Navigation components such as the TopBar and BottomTabs and also\nto system elements like the StatusBar and NavigationBar."),Object(r.b)("h2",{id:"dark-mode"},"Dark mode"),Object(r.b)("p",null,'Dark mode support for navigation UI components can be done via colors, perferably via setting a "Theme" in the default options, as in the example below.'),Object(r.b)("p",null,"The trigger for the change is the system wide Dark Mode toggle."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-js"},"Navigation.setDefaultOptions({\n  topBar: {\n    background: {\n      color: {\n        light:'white',\n        dark:'black'\n      }\n    },\n    title:{\n      color: {\n        light:'black',\n        dark:'red'\n      }\n    }\n  }\n});\n\n## Applying a theme\n\nThemes are applied using `Navigation.setDefaultOptions()` which must be called **before** `Navigation.setRoot()` is called.\n\n```js\n// Set the default topBar background color to red\nNavigation.setDefaultOptions({\n  topBar: {\n    background: {\n      color: 'red'\n    }\n  }\n});\n\n// That stack's topBar background color will be red, as is set in default options\nNavigation.setRoot({\n  root: {\n    stack: {\n      children: [\n        ...\n      ]\n    }\n  }\n});\n")),Object(r.b)("h2",{id:"conditional-themes-with-options-processor"},"Conditional themes with Options Processor"),Object(r.b)("p",null,"Some style requirements can't be facilitated with the ",Object(r.b)("inlineCode",{parentName:"p"},"defaultOptions")," api. For example, an app may have a unique theme for screens displayed in a modal or require that modals have a default dismiss button in the TopBar. An Options Processor allow us to mutate the options object of each screen and layout right before they are displayed."),Object(r.b)("p",null,"Lets see how we can leverage this API to add a dismiss button to the TopBar when showing a modal."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-theme/option-processor.tsx",file:"./style-theme/option-processor.tsx"},"import {\n  CommandName,\n  Navigation,\n  NavigationButtonPressedEvent,\n  OptionsTopBar,\n} from 'react-native-navigation';\n\nNavigation.addOptionProcessor<OptionsTopBar>(\n  'topBar',\n  (topBar: OptionsTopBar, commandName: CommandName, _props: any): OptionsTopBar => {\n    if (commandName === CommandName.ShowModal) {\n      if (!topBar.leftButtons) {\n        topBar.leftButtons = [\n          {\n            id: 'dismissModalButton',\n            icon: require('dismissIcon.png'),\n            color: 'black',\n          },\n        ];\n      }\n    }\n    return topBar;\n  }\n);\n\n// Now that each modal has a dismiss button, let's handle the button press event and dismiss the modal when needed.\nNavigation.events().registerNavigationButtonPressedListener(\n  (event: NavigationButtonPressedEvent) => {\n    if (event.buttonId === 'dismissModalButton') {\n      Navigation.dismissModal(event.componentId);\n    }\n  }\n);\n")),Object(r.b)("p",null,"The Options Processors can also be used to set default options for buttons, here's how:"),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-theme/option-processor-defaults.tsx",file:"./style-theme/option-processor-defaults.tsx"},"import { CommandName, Navigation, OptionsTopBarButton } from 'react-native-navigation';\n\nNavigation.addOptionProcessor<OptionsTopBarButton>(\n  'topBar.rightButtons',\n  (rightButtons: OptionsTopBarButton[], commandName: CommandName): OptionsTopBarButton => {\n    return rightButtons.map((button) => ({\n      ...button,\n      fontFamily: 'helvetica',\n      fontSize: 16,\n      color: 'red'\n  }));\n);\n")),Object(r.b)("h2",{id:"conditional-themes-with-layout-processor"},"Conditional themes with Layout Processor"),Object(r.b)("p",null,"Layout Processor is similar in concept to the Options Processor discussed above. While Options Processor is invoked for each options object, Layout Processor is invoked once on the entire (layout)","['../api/layout-layout/']"," tree passed to the Navigation command."),Object(r.b)("p",null,"Through the Layout Processor we can access the layout and it's children, and mutate both options and passProps before the layout is displayed. In this example we iterate on the stack's children and set a dark TopBar color according to a ",Object(r.b)("inlineCode",{parentName:"p"},"theme")," property in the child's props."),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre",className:"language-tsx",metastring:"file=./style-theme/layout-processor.tsx",file:"./style-theme/layout-processor.tsx"},"import { Layout, Navigation } from 'react-native-navigation';\n\ninterface ScreenProps {\n  theme: 'light' | 'dark';\n}\n\nfunction isScreenProps(obj: unknown): obj is ScreenProps {\n  return typeof obj === 'object' && obj !== null && typeof (obj as ScreenProps).theme === 'string';\n}\n\nNavigation.addLayoutProcessor((layout: Layout, commandName: string) => {\n  layout.stack?.children?.forEach((child) => {\n    if (!child.component) {\n      return;\n    }\n    const props = child.component.passProps;\n    if (isScreenProps(props) && props.theme === 'dark') {\n      child.component.options = {\n        topBar: {\n          background: {\n            color: 'black',\n          },\n        },\n      };\n    }\n  });\n  return layout;\n});\n")),Object(r.b)("h2",{id:"changing-theme-dynamically"},"Changing theme dynamically"),Object(r.b)("p",null,"Apps can have multiple themes and sometimes you might need to change theme dynamically. To change current theme, simply call ",Object(r.b)("inlineCode",{parentName:"p"},"Navigation.setDefaultOptions()")," with updated theme options, following that with a call to ",Object(r.b)("inlineCode",{parentName:"p"},"Navigation.setRoot()"),". The reason we need to setRoot once more is because ",Object(r.b)("inlineCode",{parentName:"p"},"Navigation.setDefaultOptions()")," does not apply options to screens which had already been created."))}m.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return d})),n.d(t,"b",(function(){return b}));var o=n(0),a=n.n(o);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,o)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,o,a=function(e,t){if(null==e)return{};var n,o,a={},r=Object.keys(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(o=0;o<r.length;o++)n=r[o],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var l=a.a.createContext({}),p=function(e){var t=a.a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):s(s({},t),e)),n},d=function(e){var t=p(e.components);return a.a.createElement(l.Provider,{value:t},e.children)},m="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return a.a.createElement(a.a.Fragment,{},t)}},h=a.a.forwardRef((function(e,t){var n=e.components,o=e.mdxType,r=e.originalType,i=e.parentName,l=c(e,["components","mdxType","originalType","parentName"]),d=p(n),m=o,h=d["".concat(i,".").concat(m)]||d[m]||u[m]||r;return n?a.a.createElement(h,s(s({ref:t},l),{},{components:n})):a.a.createElement(h,s({ref:t},l))}));function b(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var r=n.length,i=new Array(r);i[0]=h;var s={};for(var c in t)hasOwnProperty.call(t,c)&&(s[c]=t[c]);s.originalType=e,s[m]="string"==typeof e?e:o,i[1]=s;for(var l=2;l<r;l++)i[l]=n[l];return a.a.createElement.apply(null,i)}return a.a.createElement.apply(null,n)}h.displayName="MDXCreateElement"}}]);