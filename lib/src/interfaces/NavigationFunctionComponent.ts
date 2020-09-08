import React from 'react';
import { NavigationComponentProps } from './NavigationComponentProps';
import { Options } from './Options';

export interface NavigationFunctionComponent<Props = {}>
  extends React.FunctionComponent<Props & NavigationComponentProps> {
  options?: ((props: Props & NavigationComponentProps) => Options) | Options;
}
