import React from 'react';
import { NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ScreenPoppedEvent, ComponentWillAppearEvent, ComponentDidAppearEvent, ComponentDidDisappearEvent } from './ComponentEvents';
import { NavigationProps } from './NavigationComponentProps';
import { Options } from './Options';
export declare class NavigationComponent<Props = {}, State = {}, Snapshot = any> extends React.Component<Props & NavigationProps, State, Snapshot> {
    /**
     * Options used to apply a style configuration when the screen appears.
     *
     * This field can either contain the concrete options to be applied, or a generator function
     * which accepts props and returns an Options object.
     */
    static options: ((props?: any) => Options) | Options;
    componentWillAppear(_event: ComponentWillAppearEvent): void;
    componentDidAppear(_event: ComponentDidAppearEvent): void;
    componentDidDisappear(_event: ComponentDidDisappearEvent): void;
    navigationButtonPressed(_event: NavigationButtonPressedEvent): void;
    searchBarUpdated(_event: SearchBarUpdatedEvent): void;
    searchBarCancelPressed(_event: SearchBarCancelPressedEvent): void;
    previewCompleted(_event: PreviewCompletedEvent): void;
    screenPopped(_event: ScreenPoppedEvent): void;
}
