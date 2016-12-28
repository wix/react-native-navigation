import React,{Component} from 'react';
import { View, Text } from 'react-native';
import BaseConfigComponent from '../BaseConfigComponent';
import Bootstrap from '../bootstrap';
export default class Root extends BaseConfigComponent{

    constructor(props){
        super(props);
    }

    render(){
        let appStyle=this.props.appStyle;
        let tabsStyle=this.props.tabsStyle;
        Bootstrap.setAppStyle(appStyle);
        Bootstrap.setTabsStyle(tabsStyle);
        return null;
    }






}