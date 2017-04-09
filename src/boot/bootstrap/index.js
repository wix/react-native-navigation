import {Settings} from 'react-native';
import {reaction} from 'mobx';
import Navigation from '../../Navigation';
import Router from '../../Router';
class Bootstrap {
    constructor() {
        this.start = this.start.bind(this);
        this.parseScreens = this.parseScreens.bind(this);
        this.setTabs = this.setTabs.bind(this);
        this.setAppStyle = this.setAppStyle.bind(this);
        this.setTabsStyle = this.setTabsStyle.bind(this);
        this.setDrawer = this.setDrawer.bind(this);
        this.appStyle = {};
        this.tabs = [];
        this.tabsStyle = {};
        this.drawer = null;
        this.rootProps={};

    }

    setAppStyle(style) {
        this.appStyle = {...this.appStyle, ...style};
    }

    setTabsStyle(style) {
        this.tabsStyle = {...this.tabsStyle, ...style};
    }

    setDrawer(params) {
        this.drawer = {...this.drawer, ...params};
    }

    setTabs(items) {
        this.tabs = items;
    }


    parseScreens(component) {
        let children = component.props.children;
        let parentProps = {...component.props};
        delete parentProps.children;
        if (!children) {
            return;
        }
        if (children.length) {
            children.map((item, i)=> {
                let props = {...parentProps, ...item.props,parentProps:parentProps}
                let itemObj = new item.type(props);
                itemObj.render();
                if (itemObj.props.children) {
                    this.parseScreens(itemObj);
                }
            });
        } else {
            if (!children.render) {
                let props = {...parentProps, ...children.props,parentProps:parentProps} 
                children = new children.type(props);
            }
            children.render();
            if(children.props.children){
                this.parseScreens(children);
            }
        }
    }
    parseApis(component) {
        let children = component.props.children;
        let parentProps = {...component.props};
        delete parentProps.children;
        if (!children) {
            return;
        }
        if (children.length) {
            children.map((item, i)=> {
                let props = {...parentProps, ...item.props,parentProps:parentProps}
                let itemObj = new item.type(props);
                itemObj.render();
                if (itemObj.props.children) {
                    this.parseApis(itemObj);
                }
            });
        } else {
            if (!children.render) {
                let props = {...parentProps,...children.props, parentProps:parentProps}
                children = new children.type(props);
            }

            if(children.props.children){
                this.parseApis(children);
            }else {
                children.render();
            }
        }
    }

    startTabBasedApp() {
        let luanchParams = {
                ...this.rootProps,
            tabs: this.tabs,
            drawer: this.drawer
        }
        if(this.appStyle){
            luanchParams.appStyle=this.appStyle;
        }
        if(this.tabsStyle){
            luanchParams.tabsStyle=this.tabsStyle;
        }
        delete luanchParams.children;
        Navigation.startTabBasedApp(luanchParams);

    }


    initScreens(props){
        if (!props.screens) {
            throw new Error("screens must not null,props={screens:Object}")
        }
        let Screens = props.screens;
        delete props.screens;

        let root = new Screens(props).render();
        this.parseScreens(root);

        this.appStyle=root.props.appStyle;
        this.tabsStyle=root.props.tabsStyle;
        this.rootProps=root.props;
        delete this.rootProps.children;
        this.startTabBasedApp();
    }
    initApis(props){
        if (!props.apis) {
            throw new Error("apis must not null,props={apis:Object}")
        }
        let ApiComponent = props.apis;
        delete props.apis;
        let root = new ApiComponent(props).render();
        this.parseApis(root);
    }
    start(props,callback){
        console.log("bootstrap============props============",props)
        if (!props) {
            throw new Error("start method argument must not null or undefined,props={componenent:Object}")
        }
        let children=props.children;
        this.initApis(props);//初始化api配置
        if(callback){
            callback();
        }
        props.children=children;
        this.initScreens(props);
    }
}

export default new Bootstrap();