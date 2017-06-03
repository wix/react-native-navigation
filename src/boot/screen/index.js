import React,{Component} from 'react';
import { View, Text } from 'react-native';
import Navigation from '../../Navigation';
import Router from '../../Router';

import BaseConfigComponent from '../BaseConfigComponent'
export default class Screen extends BaseConfigComponent{


    constructor(props){
        super(props);
    }



    render(){
        let component=this.props.component;
        let name=this.props.name;
        delete this.props.parentProps;
        Navigation.registerComponent(name,()=>component);
        Router.register(this.props);
        return null;
    }






}