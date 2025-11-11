import React from 'react';
import { OptionsTopBar, OptionsTopBarBackButton, OptionsTopBarButton } from 'src';
import ParentNode from '../Layouts/ParentNode';
export interface TopBarProps {
    layoutNode: ParentNode;
    topBarOptions?: OptionsTopBar;
    backButtonOptions?: OptionsTopBarBackButton;
}
export declare const TopBar: {
    new (props: TopBarProps): {
        render(): React.JSX.Element | null;
        shouldRenderBackButton(layoutNode: ParentNode): boolean;
        renderButtons(buttons?: OptionsTopBarButton[]): React.JSX.Element[];
        renderBackButton(): React.JSX.Element;
        renderComponent(id: string, name: string, testID?: string): React.JSX.Element;
        context: unknown;
        setState<K extends never>(state: {} | ((prevState: Readonly<{}>, props: Readonly<TopBarProps>) => {} | Pick<{}, K> | null) | Pick<{}, K> | null, callback?: (() => void) | undefined): void;
        forceUpdate(callback?: (() => void) | undefined): void;
        readonly props: Readonly<TopBarProps>;
        state: Readonly<{}>;
        refs: {
            [key: string]: React.ReactInstance;
        };
        componentDidMount?(): void;
        shouldComponentUpdate?(nextProps: Readonly<TopBarProps>, nextState: Readonly<{}>, nextContext: any): boolean;
        componentWillUnmount?(): void;
        componentDidCatch?(error: Error, errorInfo: React.ErrorInfo): void;
        getSnapshotBeforeUpdate?(prevProps: Readonly<TopBarProps>, prevState: Readonly<{}>): any;
        componentDidUpdate?(prevProps: Readonly<TopBarProps>, prevState: Readonly<{}>, snapshot?: any): void;
        componentWillMount?(): void;
        UNSAFE_componentWillMount?(): void;
        componentWillReceiveProps?(nextProps: Readonly<TopBarProps>, nextContext: any): void;
        UNSAFE_componentWillReceiveProps?(nextProps: Readonly<TopBarProps>, nextContext: any): void;
        componentWillUpdate?(nextProps: Readonly<TopBarProps>, nextState: Readonly<{}>, nextContext: any): void;
        UNSAFE_componentWillUpdate?(nextProps: Readonly<TopBarProps>, nextState: Readonly<{}>, nextContext: any): void;
    };
    contextType?: React.Context<any> | undefined;
};
