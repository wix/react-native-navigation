"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ComponentWrapper = void 0;
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const react_lifecycles_compat_1 = require("react-lifecycles-compat");
const hoist_non_react_statics_1 = tslib_1.__importDefault(require("hoist-non-react-statics"));
class ComponentWrapper {
    wrap(componentName, OriginalComponentGenerator, store, componentEventsObserver, concreteComponentProvider = OriginalComponentGenerator, ReduxProvider, reduxStore) {
        const GeneratedComponentClass = OriginalComponentGenerator();
        class WrappedComponent extends React.Component {
            static getDerivedStateFromProps(nextProps, prevState) {
                return {
                    allProps: {
                        ...nextProps,
                        ...store.getPropsForId(prevState.componentId),
                    },
                };
            }
            _isMounted = false;
            get isMounted() {
                return this._isMounted;
            }
            constructor(props) {
                super(props);
                this._assertComponentId();
                this.state = {
                    componentId: props.componentId,
                    allProps: {},
                };
                store.setComponentInstance(props.componentId, this);
            }
            setProps(newProps, callback) {
                this.setState((prevState) => ({
                    allProps: {
                        ...prevState.allProps,
                        ...newProps,
                    },
                }), callback);
            }
            componentDidMount() {
                this._isMounted = true;
                store.consumePendingProps(this.state.componentId);
            }
            componentWillUnmount() {
                store.clearComponent(this.state.componentId);
                componentEventsObserver.unmounted(this.state.componentId);
            }
            render() {
                return (React.createElement(GeneratedComponentClass, { ...this.state.allProps, componentId: this.state.componentId, componentName: componentName }));
            }
            _assertComponentId() {
                if (!this.props.componentId) {
                    throw new Error(`Component ${componentName} does not have a componentId!`);
                }
            }
        }
        (0, react_lifecycles_compat_1.polyfill)(WrappedComponent);
        (0, hoist_non_react_statics_1.default)(WrappedComponent, concreteComponentProvider === OriginalComponentGenerator
            ? GeneratedComponentClass
            : concreteComponentProvider());
        return ReduxProvider
            ? this.wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore)
            : WrappedComponent;
    }
    wrapWithRedux(WrappedComponent, ReduxProvider, reduxStore) {
        class ReduxWrapper extends React.Component {
            render() {
                return (React.createElement(ReduxProvider, { store: reduxStore },
                    React.createElement(WrappedComponent, { ...this.props })));
            }
        }
        (0, hoist_non_react_statics_1.default)(ReduxWrapper, WrappedComponent);
        return ReduxWrapper;
    }
}
exports.ComponentWrapper = ComponentWrapper;
