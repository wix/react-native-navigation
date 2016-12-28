import {reaction} from 'mobx';
import {Navigation} from 'react-native-navigation';
class Bootstrap {
    constructor() {
        this.start = this.start.bind(this);
        this.parse = this.parse.bind(this);
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


    parse(component) {
        let children = component.props.children;
        let parentProps = {...component.props};
        delete parentProps.children;
        if (!children) {
            return;
        }
        if (children.length) {
            children.map((item, i)=> {
                let props = {...parentProps, ...item.props}
                let itemObj = new item.type(props);
                itemObj.render();
                if (itemObj.props.children) {
                    this.parse(itemObj);
                }
            });
        } else {
            if (!children.render) {
                children = new children.type(children.props);
            }
            children.render();
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

    start(props) {
        if (!props) {
            throw new Error("start method argument must not null or undefined,props={componenent:Object}")
        }
        if (!props.component) {
            throw new Error("component must not null,props={componenent:Object}")
        }
        let Component = props.component;
        delete props.component;

        let root = new Component(props).render();



        this.parse(root);

        this.appStyle=root.props.appStyle;
        this.tabsStyle=root.props.tabsStyle;
        this.rootProps=root.props;
        delete this.rootProps.children;
        this.startTabBasedApp();


    }
}

export default new Bootstrap();