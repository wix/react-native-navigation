/// <reference types="react" />
declare module 'react-lifecycles-compat' {
    import * as React from 'react';
    function polyfill(component: React.ComponentClass<any>): void;
}
