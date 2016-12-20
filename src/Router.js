/**
 * Created by luolishu on 12/5/16.
 */

'use strict';
import * as DisplayMode from './DisplayMode';

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

        let displayMode=params.displayMode;
        if(!displayMode){
            displayMode="push"
        }
        displayMode=displayMode.toLowerCase();
        switch(displayMode){
            case DisplayMode.push:
                this.push(navigator,params);
                break;
            case DisplayMode.modal:
                this.showModal(navigator,params);
                break;
            case DisplayMode.lightbox:
                this.showLightBox(navigator,params);
                break;
            case DisplayMode.reset:
                this.resetTo(navigator,params);
                break;
        }

    }

    register(params= {}){
        let name=params.name;
        if (this[name]) {
            console.log(`name ${name} is already defined!`);
            throw new Error(`name ${name} is already defined!`);
        }
        let passProps={...params};
        this[name]=(navigator,props = {})=>{
            let navProps={...passProps,...props};
            this.navigateTo(navigator,{screen:name,...navProps});
        }
    }



}


export default new Router();