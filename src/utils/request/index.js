/**
 * @providesModule zowork-http-request
 * @flow
 */
import qs from 'qs';
var commonHeaders={

}

var setAccessToken=(token)=>{
    if(!token){
        return;
    }
    commonHeaders["Access-Token"]=token;
}
var setAuthrization=(authorization)=>{
    if(!authorization){
        return;
    }
    commonHeaders["Authorization"]=authorization;
}

var get=  function get(url,params,props){
    console.log("request url======"+url,qs.stringify(params),commonHeaders)
    return  fetch(url, {
        method: "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
            'X-Requested-With': 'XMLHttpRequest',
            ...commonHeaders
        },
        body:qs.stringify(params)
    }).then(response => response.json());
}
var post= function post(url,params,props){
    console.log("request url======"+url,qs.stringify(params),commonHeaders)
    return  fetch(url, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
            'X-Requested-With': 'XMLHttpRequest',
            ...commonHeaders
        },
        body:qs.stringify(params)
    }).then(response => response.json());
}


module.exports = {
    get,
    post,
    commonHeaders,
    setAccessToken,
    setAuthrization,
};