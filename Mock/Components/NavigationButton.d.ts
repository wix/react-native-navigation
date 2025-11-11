import React from 'react';
import { OptionsTopBarButton } from 'src';
interface ButtonProps {
    button: OptionsTopBarButton;
    componentId: string;
}
export declare const NavigationButton: {
    new (props: ButtonProps): {
        ref: undefined;
        render(): React.JSX.Element;
        renderButtonComponent(): React.JSX.Element;
        invokeOnClick(stateNode: any): void;
        context: unknown;
        setState<K extends never>(state: {} | ((prevState: Readonly<{}>, props: Readonly<ButtonProps>) => {} | Pick<{}, K> | null) | Pick<{}, K> | null, callback?: (() => void) | undefined): void;
        forceUpdate(callback?: (() => void) | undefined): void;
        readonly props: Readonly<ButtonProps>;
        state: Readonly<{}>;
        refs: {
            [key: string]: React.ReactInstance;
        };
        componentDidMount?(): void;
        shouldComponentUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): boolean;
        componentWillUnmount?(): void;
        componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void;
        getSnapshotBeforeUpdate?(prevProps: Readonly<ButtonProps>, prevState: Readonly<{}>): any;
        componentDidUpdate?(prevProps: Readonly<ButtonProps>, prevState: Readonly<{}>, snapshot?: any): void;
        componentWillMount?(): void;
        UNSAFE_componentWillMount?(): void;
        componentWillReceiveProps?(nextProps: Readonly<ButtonProps>, nextContext: any): void;
        UNSAFE_componentWillReceiveProps?(nextProps: Readonly<ButtonProps>, nextContext: any): void;
        componentWillUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): void;
        UNSAFE_componentWillUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): void;
    };
    new (props: ButtonProps, context: any): {
        ref: undefined;
        render(): React.JSX.Element;
        renderButtonComponent(): React.JSX.Element;
        invokeOnClick(stateNode: any): void;
        context: unknown;
        setState<K extends never>(state: {} | ((prevState: Readonly<{}>, props: Readonly<ButtonProps>) => {} | Pick<{}, K> | null) | Pick<{}, K> | null, callback?: (() => void) | undefined): void;
        forceUpdate(callback?: (() => void) | undefined): void;
        readonly props: Readonly<ButtonProps>;
        state: Readonly<{}>;
        refs: {
            [key: string]: React.ReactInstance;
        };
        componentDidMount?(): void;
        shouldComponentUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): boolean;
        componentWillUnmount?(): void;
        componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void;
        getSnapshotBeforeUpdate?(prevProps: Readonly<ButtonProps>, prevState: Readonly<{}>): any;
        componentDidUpdate?(prevProps: Readonly<ButtonProps>, prevState: Readonly<{}>, snapshot?: any): void;
        componentWillMount?(): void;
        UNSAFE_componentWillMount?(): void;
        componentWillReceiveProps?(nextProps: Readonly<ButtonProps>, nextContext: any): void;
        UNSAFE_componentWillReceiveProps?(nextProps: Readonly<ButtonProps>, nextContext: any): void;
        componentWillUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): void;
        UNSAFE_componentWillUpdate?(nextProps: Readonly<ButtonProps>, nextState: Readonly<{}>, nextContext: any): void;
    };
    contextType?: React.Context<any> | undefined;
};
export {};
