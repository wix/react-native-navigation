import React from 'react';
import { NavigationProps } from './NavigationComponentProps';
import { Options } from './Options';
export interface NavigationFunctionComponent<Props = {}> extends React.FunctionComponent<Props & NavigationProps> {
    options?: ((props: Props & NavigationProps) => Options) | Options;
}
