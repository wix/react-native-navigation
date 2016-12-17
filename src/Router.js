/**
 * Created by luolishu on 12/5/16.
 */

'use strict';


class Router{
    constructor(component){
        this.component=component
        this.navigator=component.props.navigator;
    }
    setNavigator(navigator){
        this.navigator=navigator;
    }

    pop(params= {}){
        this.navigator.pop(params);
    }
    popToRoot(params= {}){
        this.navigator.popToRoot(params);
    }
    push(params= {}){
        this.navigator.push(params);
    }
    resetTo(params= {}){
        this.navigator.resetTo(params);
    }
    showModal(params= {}){
        this.navigator.showModal(params);
    }
    dismissModal(params= {}){
        this.navigator.dismissModal(params);
    }
    dismissAllModals(params= {}){
        this.navigator.dismissAllModals(params);
    }
    showLightBox(params = {}){
        this.navigator.showLightBox(params);
    }
    dismissLightBox(params = {}){
        this.navigator.dismissLightBox(params);
    }

    register(params= {}){
        let key=params.key;
        if (this[key]) {
            console.log(`Key ${key} is already defined!`);
            throw new Error(`Key ${key} is already defined!`);
        }
        this[key]=(props = {})=>{
            alert("")
        }
    }



}


export default new Router();