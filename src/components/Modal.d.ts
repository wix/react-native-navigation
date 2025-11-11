import React from 'react';
import { ViewProps } from 'react-native';
export interface RNNModalProps extends ViewProps {
    visible: boolean;
    transparent: boolean;
    blurOnUnmount: boolean;
    animationType: 'none' | 'fade' | 'slide';
    onShow?: () => any;
    onRequestClose: () => any;
}
export declare class Modal extends React.Component<RNNModalProps> {
    static defaultProps: {
        transparent: boolean;
        blurOnUnmount: boolean;
        animationType: string;
    };
    constructor(props: RNNModalProps);
    render(): React.JSX.Element | null;
    private proccessProps;
}
