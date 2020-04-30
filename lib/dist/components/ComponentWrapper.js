"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const merge_1 = tslib_1.__importDefault(require("lodash/merge"));
const react_lifecycles_compat_1 = require("react-lifecycles-compat");
const hoist_non_react_statics_1 = tslib_1.__importDefault(require("hoist-non-react-statics"));
class ComponentWrapper {
    wrap(componentName, OriginalComponentGenerator, store, componentEventsObserver, concreteComponentProvider = OriginalComponentGenerator, ReduxProvider, reduxStore) {
        const GeneratedComponentClass = OriginalComponentGenerator();
        class WrappedComponent extends React.Component {
            static getDerivedStateFromProps(nextProps, prevState) {
                return {
                    allProps: merge_1.default({}, nextProps, store.getPropsForId(prevState.componentId))
                };
            }
            constructor(props) {
                super(props);
                this._assertComponentId();
                this.state = {
                    componentId: props.componentId,
                    allProps: {}
                };
                store.setComponentInstance(props.componentId, this);
            }
            setProps(newProps) {
                this.setState({ allProps: newProps });
            }
            componentWillUnmount() {
                store.clearComponent(this.state.componentId);
                componentEventsObserver.unmounted(this.state.componentId);
            }
            render() {
                return (<GeneratedComponentClass {...this.state.allProps} componentId={this.state.componentId}/>);
            }
            _assertComponentId() {
                if (!this.props.componentId) {
                    throw new Error(`Component ${componentName} does not have a componentId!`);
                }
            }
        }
        react_lifecycles_compat_1.polyfill(WrappedComponent);
        hoist_non_react_statics_1.default(WrappedComponent, concreteComponentProvider());
        return ReduxProvider ? this.wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore) : WrappedComponent;
    }
    wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore) {
        class ReduxWrapper extends React.Component {
            render() {
                return (<ReduxProvider store={reduxStore}>
            <WrappedComponent {...this.props}/>
          </ReduxProvider>);
            }
        }
        hoist_non_react_statics_1.default(ReduxWrapper, WrappedComponent);
        return ReduxWrapper;
    }
}
exports.ComponentWrapper = ComponentWrapper;
