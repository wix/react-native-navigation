import React,{Component} from 'react';
import { View, Text } from 'react-native';
import BaseConfigComponent from '../BaseConfigComponent'
import {Navigation,Router} from 'react-native-navigation';
import Bootstrap from '../bootstrap';
export default class Tabbar extends BaseConfigComponent{


    constructor(props){
        super(props);
    }

    render(){
        let children=this.props.children;
        if(!children||children.length<=0){
            throw new Error("Tabbar must has more than one child,Please check your configuration!");
        }
        let tabs=[];
        let parentProps=this.props;
        delete parentProps.children;
        children.map((item,i)=>{
            let name=item.props.name;
            let component=item.props.component;
            Navigation.registerComponent(name,()=>component);
            Router.register(item.props);

            let tabProps={...parentProps,...item.props,screen:name,label:item.props.title};

            delete tabProps.component;
            tabs.push(tabProps);
        });
        Bootstrap.setTabs(tabs);
        return null;
    }






}