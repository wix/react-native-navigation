import * as React from 'react';
import * as PropTypes from 'prop-types';
export interface SharedElementProps {
    elementId: string;
    resizeMode: string;
}
export declare class SharedElement extends React.Component<SharedElementProps> {
    static propTypes: {
        elementId: PropTypes.Validator<string>;
        resizeMode: PropTypes.Requireable<string>;
    };
    static defaultProps: {
        resizeMode: string;
    };
    render(): JSX.Element;
}
