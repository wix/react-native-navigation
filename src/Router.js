/**
 * Created by luolishu on 12/5/16.
 */

'use strict';


class Router{

    pop(navigator,params= {}){
        navigator.pop(params);
    }
    popToRoot(navigator,params= {}){
        navigator.popToRoot(params);
    }
    push(navigator,params= {}){
        navigator.push(params);
    }
    resetTo(navigator,params= {}){
        navigator.resetTo(params);
    }
    showModal(navigator,params= {}){
        navigator.showModal(params);
    }
    dismissModal(navigator,params= {}){
        navigator.dismissModal(params);
    }
    dismissAllModals(navigator,params= {}){
        navigator.dismissAllModals(params);
    }
    showLightBox(navigator,params = {}){
        navigator.showLightBox(params);
    }
    dismissLightBox(navigator,params = {}){
        navigator.dismissLightBox(params);
    }

    navigateTo(navigator,params){
        this.push(navigator,params);
    }

    register(params= {}){
        let key=params.key;
        if (this[key]) {
            console.log(`Key ${key} is already defined!`);
            throw new Error(`Key ${key} is already defined!`);
        }
        this[key]=(navigator,props = {})=>{
            this.navigateTo(navigator,{screen:key,...props});
        }
    }



}


export default new Router();