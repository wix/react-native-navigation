import React from 'react';
export interface NavigationComponentProps {
  componentId: string;
}

export class NavigationComponent<Props = {}, State = {}, Snapshot = any>
  extends React.Component<Props & NavigationComponentProps, State, Snapshot> {}
