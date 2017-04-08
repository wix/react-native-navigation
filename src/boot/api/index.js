import React,{Component} from 'react';
import { View, Text } from 'react-native';
import ApiRegistry from '../registry';
export default class Api extends Component{


    constructor(props){
        super(props);
    }

    render(){
        let component=this.props.component;
        let groupProps=this.props.parentProps;
        let name=this.props.name;
        let path=this.props.path;
        let method=this.props.method;
        let baseUrl=this.props.baseUrl;

        let params={name:name,path:path,method:method,baseUrl:baseUrl};
        if(groupProps){
            params.groupName=groupProps.name;
            params.groupPath=groupProps.path;
        }
        if(!params.method){
            params.method="get";
        }
        let props={...this.props,...params};


        ApiRegistry.registApi(props);
        return null;
    }






}