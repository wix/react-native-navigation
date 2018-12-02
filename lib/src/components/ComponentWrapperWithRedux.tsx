import * as React from 'react';
import {ComponentWrapper} from './ComponentWrapper';

export class ComponentWrapperWithRedux extends ComponentWrapper {
  private readonly reduxProvider: any;
  private readonly reduxStore: any;

  constructor(ReduxProvider: any, reduxStore: any) {
    super();
    this.reduxProvider = ReduxProvider;
    this.reduxStore = reduxStore;
  }

  wrapComponent(WrappedComponent: React.ComponentClass<any>): React.ComponentClass<any> {
    const ReduxProvider = this.reduxProvider;
    const store = this.reduxStore;
    class ReduxWrapper extends React.Component<any, any> {
      render() {
        return(
          <ReduxProvider store={store}>
            <WrappedComponent {...this.props} />
          </ReduxProvider>
        );
      }
    }
    require('hoist-non-react-statics')(ReduxWrapper, WrappedComponent);
    return ReduxWrapper;
  }
}
