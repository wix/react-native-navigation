(window.webpackJsonp=window.webpackJsonp||[]).push([[141],{207:function(e,t,n){"use strict";n.r(t),n.d(t,"frontMatter",(function(){return c})),n.d(t,"metadata",(function(){return b})),n.d(t,"toc",(function(){return u})),n.d(t,"default",(function(){return h}));var a=n(3),o=n(7),i=(n(0),n(627)),r=n(631),s=n(632),l=n(633);const d=["components"],c={id:"modal",title:"Modal",sidebar_label:"Modal"},b={unversionedId:"docs/modal",id:"version-7.25.4/docs/modal",isDocsHomePage:!1,title:"Modal",description:"In human-centered design, when we speak about a modal (or modality), we often refer to a set of techniques, aimed at bringing user attention to a specific event / screen / action / etc. Those often require user input. A pop-up on a website, a file deletion confirmation dialogue on your computer, or an alert asking you to enable location service on your phone - these can all be considered modals.",source:"@site/versioned_docs/version-7.25.4/docs/docs-modal.mdx",slug:"/docs/modal",permalink:"/react-native-navigation/7.25.4/docs/modal",editUrl:"https://github.com/wix/react-native-navigation/edit/master/website/versioned_docs/version-7.25.4/docs/docs-modal.mdx",version:"7.25.4",sidebar_label:"Modal",sidebar:"version-7.25.4/docs",previous:{title:"Root",permalink:"/react-native-navigation/7.25.4/docs/root"},next:{title:"Overlay",permalink:"/react-native-navigation/7.25.4/docs/overlay"}},u=[{value:"Presenting modals",id:"presenting-modals",children:[]},{value:"Adding a dismiss button",id:"adding-a-dismiss-button",children:[]},{value:"Transparent modals",id:"transparent-modals",children:[]},{value:"Preventing a Modal from being dismissed",id:"preventing-a-modal-from-being-dismissed",children:[{value:"Android",id:"android",children:[]},{value:"iOS",id:"ios",children:[]}]},{value:"Presentation Style",id:"presentation-style",children:[]}],p={toc:u},m="wrapper";function h(e){let{components:t}=e,n=Object(o.a)(e,d);return Object(i.b)(m,Object(a.a)({},p,n,{components:t,mdxType:"MDXLayout"}),Object(i.b)("p",null,"In human-centered design, when we speak about a modal (or modality), we often refer to a set of techniques, aimed at bringing user attention to a specific event / screen / action / etc. Those often require user input. A pop-up on a website, a file deletion confirmation dialogue on your computer, or an alert asking you to enable location service on your phone - these can all be considered modals."),Object(i.b)("p",null,"A modal is a term used in native iOS world (",Object(i.b)("a",{parentName:"p",href:"https://developer.apple.com/design/human-interface-guidelines/ios/app-architecture/modality/"},"full description here"),"), while on Android, ",Object(i.b)("a",{parentName:"p",href:"https://developer.android.com/guide/topics/ui/dialogs"},"dialogs")," are often used to create similar or identical behavior, alongside other techniques."),Object(i.b)("h2",{id:"presenting-modals"},"Presenting modals"),Object(i.b)("p",null,"Modal can be displayed by invoking the ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.showModal()")," command, as shown in the example below:"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"Navigation.showModal({\n  stack: {\n    children: [\n      {\n        component: {\n          name: 'MODAL_SCREEN',\n          options: {\n            topBar: {\n              title: {\n                text: 'Modal',\n              },\n            },\n          },\n        },\n      },\n    ],\n  },\n});\n")),Object(i.b)("p",null,"Or by declaring modal as a Component inside render function:"),Object(i.b)("div",{className:"admonition admonition-note alert alert--secondary"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.3 5.69a.942.942 0 0 1-.28-.7c0-.28.09-.52.28-.7.19-.18.42-.28.7-.28.28 0 .52.09.7.28.18.19.28.42.28.7 0 .28-.09.52-.28.7a1 1 0 0 1-.7.3c-.28 0-.52-.11-.7-.3zM8 7.99c-.02-.25-.11-.48-.31-.69-.2-.19-.42-.3-.69-.31H6c-.27.02-.48.13-.69.31-.2.2-.3.44-.31.69h1v3c.02.27.11.5.31.69.2.2.42.31.69.31h1c.27 0 .48-.11.69-.31.2-.19.3-.42.31-.69H8V7.98v.01zM7 2.3c-3.14 0-5.7 2.54-5.7 5.68 0 3.14 2.56 5.7 5.7 5.7s5.7-2.55 5.7-5.7c0-3.15-2.56-5.69-5.7-5.69v.01zM7 .98c3.86 0 7 3.14 7 7s-3.14 7-7 7-7-3.12-7-7 3.14-7 7-7z"}))),"note")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"Available for Android only, for iOS please use Modal from ",Object(i.b)("inlineCode",{parentName:"p"},"react-native"),"."))),Object(i.b)("p",null,"(",Object(i.b)("a",{parentName:"p",href:"https://github.com/wix/react-native-navigation/blob/master/playground/src/screens/ModalScreen.tsx"},"Full code can be found in the Playground"),")."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-jsx"},"import { NavigationComponent, Modal as RNNModal } from 'react-native-navigation';\n.....\ntoggleModal = () => this.setState({ modalVisible: !this.state.modalVisible });\n.....\nrender() {\n  return (\n    .....\n  <RNNModal\n      onShow={/* optional callback */}\n      animationType={/* optional, 'none' | 'fade' | 'slide', default 'slide'*/}\n      blurOnUnmount={/* optional, default false */}\n      transparent={/* optional, default false, true behaves as overCurrentContext */}\n      visible={this.state.modalVisible}\n      onRequestClose={() => this.setState({ modalVisible: false })}\n      >\n          <Button\n            label=\"Dismiss declared Modal\"\n            testID={DISMISS_MODAL_BTN}\n            onPress={this.toggleModal}\n          />\n          ....\n    </RNNModal>\n    ....\n    );\n}\n")),Object(i.b)("p",null,"Here's how the Modal looks on both platforms."),Object(i.b)(r.a,{defaultValue:"ios",values:[{label:"iOS",value:"ios"},{label:"Android",value:"android"}],mdxType:"Tabs"},Object(i.b)(s.a,{value:"ios",mdxType:"TabItem"},Object(i.b)("img",{width:"40%",src:Object(l.a)("img/modal_ios.gif")})),Object(i.b)(s.a,{value:"android",mdxType:"TabItem"},Object(i.b)("img",{width:"40%",src:Object(l.a)("img/modal_android.gif")}))),Object(i.b)("h2",{id:"adding-a-dismiss-button"},"Adding a dismiss button"),Object(i.b)("p",null,"Modals should always have a dismiss button, on left-to-right devices it's typically placed as a left button in the TopBar."),Object(i.b)("p",null,"After we've added our dismiss button, we need to dismiss the modal when the button is pressed."),Object(i.b)("div",{className:"admonition admonition-note alert alert--secondary"},Object(i.b)("div",{parentName:"div",className:"admonition-heading"},Object(i.b)("h5",{parentName:"div"},Object(i.b)("span",{parentName:"h5",className:"admonition-icon"},Object(i.b)("svg",{parentName:"span",xmlns:"http://www.w3.org/2000/svg",width:"14",height:"16",viewBox:"0 0 14 16"},Object(i.b)("path",{parentName:"svg",fillRule:"evenodd",d:"M6.3 5.69a.942.942 0 0 1-.28-.7c0-.28.09-.52.28-.7.19-.18.42-.28.7-.28.28 0 .52.09.7.28.18.19.28.42.28.7 0 .28-.09.52-.28.7a1 1 0 0 1-.7.3c-.28 0-.52-.11-.7-.3zM8 7.99c-.02-.25-.11-.48-.31-.69-.2-.19-.42-.3-.69-.31H6c-.27.02-.48.13-.69.31-.2.2-.3.44-.31.69h1v3c.02.27.11.5.31.69.2.2.42.31.69.31h1c.27 0 .48-.11.69-.31.2-.19.3-.42.31-.69H8V7.98v.01zM7 2.3c-3.14 0-5.7 2.54-5.7 5.68 0 3.14 2.56 5.7 5.7 5.7s5.7-2.55 5.7-5.7c0-3.15-2.56-5.69-5.7-5.69v.01zM7 .98c3.86 0 7 3.14 7 7s-3.14 7-7 7-7-3.12-7-7 3.14-7 7-7z"}))),"note")),Object(i.b)("div",{parentName:"div",className:"admonition-content"},Object(i.b)("p",{parentName:"div"},"For the LeftButton to be visible, the screen must be displayed in a Stack."))),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"class ModalScreen extends React.Component {\n  // Set a dismiss button in static options\n  static options() {\n    return {\n      topBar: {\n        leftButtons: {\n          id: 'dismiss',\n          icon: require('./dismiss.png'),\n        },\n      },\n    };\n  }\n\n  constructor(props) {\n    super(props);\n    // Register to events\n    Navigation.events().bindComponent(this);\n  }\n\n  // Handle the button press event and dismiss the modal if needed\n  navigationButtonPressed({ buttonId }) {\n    if (buttonId === 'dismiss') {\n      Navigation.dismissModal(this.props.componentId);\n    }\n  }\n}\n")),Object(i.b)("h2",{id:"transparent-modals"},"Transparent modals"),Object(i.b)("p",null,"Showing transparent modals is a nice technique to increase immersiveness while keeping the user in the same context.\nWhen a modal is displayed the content behind it (either root or modal) is removed from hierarchy. While this behavior improves performance, it's undesired when\nshowing transparent modals as we need to see the content behind it."),Object(i.b)("p",null,"To configure this behaviour, we'll need to change the ",Object(i.b)("inlineCode",{parentName:"p"},"modalPresentationStyle")," option to ",Object(i.b)("inlineCode",{parentName:"p"},"overCurrentContext")," and change the layout background color to ",Object(i.b)("inlineCode",{parentName:"p"},"'transparent'"),"."),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"{\n  modalPresentationStyle: 'overCurrentContext',\n  layout: {\n    backgroundColor: 'transparent'\n  }\n}\n")),Object(i.b)("h2",{id:"preventing-a-modal-from-being-dismissed"},"Preventing a Modal from being dismissed"),Object(i.b)("p",null,"Preventing a modal from being dismissed is done differently for each platform. While preventing the user from dismissing the modal is possible,\nthe modal could still be dismissed programmatically by calling ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.dismissModal()")),Object(i.b)("p",null,"If the modal has a dismiss button, of course you'll need to handle it your self and avoid calling ",Object(i.b)("inlineCode",{parentName:"p"},"Navigation.dismissModal()"),"\nwhen the button is pressed."),Object(i.b)("h3",{id:"android"},"Android"),Object(i.b)("p",null,"On Android, modals can be dismissed with the hardware back button. You can handle the back press yourself by\ndisabling the hardware back button ",Object(i.b)("inlineCode",{parentName:"p"},"dismissModalOnPress")," option:"),Object(i.b)("pre",null,Object(i.b)("code",{parentName:"pre",className:"language-js"},"{\n  hardwareBackButton: {\n    dismissModalOnPress: false;\n  }\n}\n")),Object(i.b)("p",null,"As a result, pressing the hardware back button will not dismiss the modal and will dispatch ",Object(i.b)("a",{parentName:"p",href:"https://wix.github.io/react-native-navigation/api/events#navigationbuttonpressed-event"},Object(i.b)("inlineCode",{parentName:"a"},"navigationButtonPress"))," event with the default hardware back button id ",Object(i.b)("inlineCode",{parentName:"p"},"RNN.hardwareBackButton"),"."),Object(i.b)("h3",{id:"ios"},"iOS"),Object(i.b)("p",null,"While iOS devices don't have a hardware back button, PageSheet modals can be dismissed by swipe gesture\nfrom the top of the screen. To disable it, set ",Object(i.b)("a",{parentName:"p",href:"#"},"swipeToDismiss")," option to false."),Object(i.b)("h2",{id:"presentation-style"},"Presentation Style"),Object(i.b)("p",null,"The ",Object(i.b)("a",{parentName:"p",href:"/react-native-navigation/api/options-root#modalpresentationstyle"},"presentation style")," determines the look and feel of a screen displayed as modal."))}h.isMDXComponent=!0},627:function(e,t,n){"use strict";n.d(t,"a",(function(){return b})),n.d(t,"b",(function(){return h}));var a=n(0),o=n.n(a);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function r(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function s(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?r(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,a,o=function(e,t){if(null==e)return{};var n,a,o={},i=Object.keys(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(a=0;a<i.length;a++)n=i[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var d=o.a.createContext({}),c=function(e){var t=o.a.useContext(d),n=t;return e&&(n="function"==typeof e?e(t):s(s({},t),e)),n},b=function(e){var t=c(e.components);return o.a.createElement(d.Provider,{value:t},e.children)},u="mdxType",p={inlineCode:"code",wrapper:function(e){var t=e.children;return o.a.createElement(o.a.Fragment,{},t)}},m=o.a.forwardRef((function(e,t){var n=e.components,a=e.mdxType,i=e.originalType,r=e.parentName,d=l(e,["components","mdxType","originalType","parentName"]),b=c(n),u=a,m=b["".concat(r,".").concat(u)]||b[u]||p[u]||i;return n?o.a.createElement(m,s(s({ref:t},d),{},{components:n})):o.a.createElement(m,s({ref:t},d))}));function h(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var i=n.length,r=new Array(i);r[0]=m;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s[u]="string"==typeof e?e:a,r[1]=s;for(var d=2;d<i;d++)r[d]=n[d];return o.a.createElement.apply(null,r)}return o.a.createElement.apply(null,n)}m.displayName="MDXCreateElement"},628:function(e,t,n){"use strict";function a(e){var t,n,o="";if("string"==typeof e||"number"==typeof e)o+=e;else if("object"==typeof e)if(Array.isArray(e))for(t=0;t<e.length;t++)e[t]&&(n=a(e[t]))&&(o&&(o+=" "),o+=n);else for(t in e)e[t]&&(o&&(o+=" "),o+=t);return o}t.a=function(){for(var e,t,n=0,o="";n<arguments.length;)(e=arguments[n++])&&(t=a(e))&&(o&&(o+=" "),o+=t);return o}},629:function(e,t,n){"use strict";var a=n(0),o=n(630);t.a=function(){const e=Object(a.useContext)(o.a);if(null==e)throw new Error("`useUserPreferencesContext` is used outside of `Layout` Component.");return e}},630:function(e,t,n){"use strict";var a=n(0);const o=Object(a.createContext)(void 0);t.a=o},631:function(e,t,n){"use strict";var a=n(0),o=n.n(a),i=n(629),r=n(628),s=n(55),l=n.n(s);const d=37,c=39;t.a=function(e){const{lazy:t,block:n,defaultValue:s,values:b,groupId:u,className:p}=e,{tabGroupChoices:m,setTabGroupChoices:h}=Object(i.a)(),[f,v]=Object(a.useState)(s),g=a.Children.toArray(e.children),O=[];if(null!=u){const e=m[u];null!=e&&e!==f&&b.some((t=>t.value===e))&&v(e)}const j=e=>{const t=e.target,n=O.indexOf(t),a=g[n].props.value;v(a),null!=u&&(h(u,a),setTimeout((()=>{(function(e){const{top:t,left:n,bottom:a,right:o}=e.getBoundingClientRect(),{innerHeight:i,innerWidth:r}=window;return t>=0&&o<=r&&a<=i&&n>=0})(t)||(t.scrollIntoView({block:"center",behavior:"smooth"}),t.classList.add(l.a.tabItemActive),setTimeout((()=>t.classList.remove(l.a.tabItemActive)),2e3))}),150))},y=e=>{var t;let n;switch(e.keyCode){case c:{const t=O.indexOf(e.target)+1;n=O[t]||O[0];break}case d:{const t=O.indexOf(e.target)-1;n=O[t]||O[O.length-1];break}}null===(t=n)||void 0===t||t.focus()};return o.a.createElement("div",{className:"tabs-container"},o.a.createElement("ul",{role:"tablist","aria-orientation":"horizontal",className:Object(r.a)("tabs",{"tabs--block":n},p)},b.map((e=>{let{value:t,label:n}=e;return o.a.createElement("li",{role:"tab",tabIndex:f===t?0:-1,"aria-selected":f===t,className:Object(r.a)("tabs__item",l.a.tabItem,{"tabs__item--active":f===t}),key:t,ref:e=>O.push(e),onKeyDown:y,onFocus:j,onClick:j},n)}))),t?Object(a.cloneElement)(g.filter((e=>e.props.value===f))[0],{className:"margin-vert--md"}):o.a.createElement("div",{className:"margin-vert--md"},g.map(((e,t)=>Object(a.cloneElement)(e,{key:t,hidden:e.props.value!==f})))))}},632:function(e,t,n){"use strict";var a=n(0),o=n.n(a);t.a=function(e){let{children:t,hidden:n,className:a}=e;return o.a.createElement("div",{role:"tabpanel",hidden:n,className:a},t)}},633:function(e,t,n){"use strict";n.d(t,"b",(function(){return i})),n.d(t,"a",(function(){return r}));var a=n(16),o=n(634);function i(){const{siteConfig:{baseUrl:e="/",url:t}={}}=Object(a.default)();return{withBaseUrl:(n,a)=>function(e,t,n,a){let{forcePrependBaseUrl:i=!1,absolute:r=!1}=void 0===a?{}:a;if(!n)return n;if(n.startsWith("#"))return n;if(Object(o.b)(n))return n;if(i)return t+n;const s=n.startsWith(t)?n:t+n.replace(/^\//,"");return r?e+s:s}(t,e,n,a)}}function r(e,t){void 0===t&&(t={});const{withBaseUrl:n}=i();return n(e,t)}},634:function(e,t,n){"use strict";function a(e){return!0===/^(\w*:|\/\/)/.test(e)}function o(e){return void 0!==e&&!a(e)}n.d(t,"b",(function(){return a})),n.d(t,"a",(function(){return o}))}}]);