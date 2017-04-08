
import {get,post} from 'zowork-http-request';

class ApiRegistry{




    request(params,props){
        let url=props.baseUrl+(props.groupPath?props.groupPath:"/")+props.path;
         switch(props.method.toLowerCase()){
             case 'get':
                 return get(url,params,props);
                 break;
             case 'post':
                 return post(url,params,props);
                 break;
             default:
                 return get(url,params,props);
         }
    }



    registApi(props){
        let groupName=props.groupName;
        let name=props.name;
        let registry=this;
        if(groupName){
            if(!this[groupName]){
                this[groupName]={};
            }
            this[groupName][name]=(params)=>{
                result =registry.request(params,props);
                if(props.callback){
                    props.callback(result);
                }
                return result;
            }
        }else{
            this[name]=(params)=>{
                result =registry.request(params,props);
                if(props.callback){
                    props.callback(result);
                }
                return result;
            }
        }
    }
}

export default new ApiRegistry();