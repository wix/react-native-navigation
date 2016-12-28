import React,{Component} from 'react';
import { View, Text } from 'react-native';
import {Navigation,Router} from 'react-native-navigation';
import Bootstrap from '../bootstrap';
import BaseConfigComponent from '../BaseConfigComponent'
export default class Drawer extends BaseConfigComponent{


    constructor(props){
        super(props);
    }

    render(){
        let component=this.props.component;
        let name=this.props.name;
        Navigation.registerComponent(name,()=>component);
        Router.register(this.props);
        let drawer={}
        let position=this.props.position;
        delete this.props.position;
        delete this.props.component;
        if(position=='left'){
            drawer={...this.props,left:{screen:name},}
        }
        if(position=='right'){
            drawer={...this.props,right:{screen:name},}
        }
        Bootstrap.setDrawer(drawer);
        return null;
    }






}