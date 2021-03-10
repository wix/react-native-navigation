import React from 'react';
import {
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ScreenPoppedEvent,
  ComponentWillAppearEvent,
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
} from './ComponentEvents';
import { NavigationComponentProps } from './NavigationComponentProps';
import { Options } from './Options';

export class NavigationComponent<Props = {}, State = {}, Snapshot = any> extends React.Component<
  Props & NavigationComponentProps,
  State,
  Snapshot
> {
  /**
   * Options used to apply a style configuration when the screen appears.
   *
   * This field can either contain the concrete options to be applied, or a generator function
   * which accepts props and returns an Options object.
   */
  static options: ((props?: any) => Options) | Options;

  componentWillAppear(_event: ComponentWillAppearEvent) {}
  componentDidAppear(_event: ComponentDidAppearEvent) {}
  componentDidDisappear(_event: ComponentDidDisappearEvent) {}
  navigationButtonPressed(_event: NavigationButtonPressedEvent) {}
  searchBarUpdated(_event: SearchBarUpdatedEvent) {}
  searchBarCancelPressed(_event: SearchBarCancelPressedEvent) {}
  previewCompleted(_event: PreviewCompletedEvent) {}
  screenPopped(_event: ScreenPoppedEvent) {}
}
