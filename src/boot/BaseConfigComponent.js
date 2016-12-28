import React,{Component} from 'react';
import { View, Text } from 'react-native';
export default class BaseConfigComponent extends Component{
    static propTypes = {
        drawer:React.PropTypes.object,
        tabsStyle:React.PropTypes.object,
        appStyle:React.PropTypes.object,
        icon:React.PropTypes.object,
        selectedIcon: React.PropTypes.object,
        hideNavbar: React.PropTypes.bool,
        hideTabbar: React.PropTypes.bool,
        name:React.PropTypes.string,
        title:React.PropTypes.string,
        style: View.propTypes.style,
        navigatorStyle:React.PropTypes.object,
        component: React.PropTypes.object,
        navigatorButtons:React.PropTypes.object,
    };

    constructor(props){
        super(props);
    }

}