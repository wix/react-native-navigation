declare module 'react-lifecycles-compat' {
  import * as React from 'react';

  export function polyfill(component: React.ComponentClass<any>): void;
}
