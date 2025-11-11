import React from 'react';
import type { NavigationProps } from './NavigationComponentProps';
import type { Options } from './Options';

export interface NavigationFunctionComponent<Props = {}>
  extends React.FunctionComponent<Props & NavigationProps> {
  options?: ((props: Props & NavigationProps) => Options) | Options;
}
