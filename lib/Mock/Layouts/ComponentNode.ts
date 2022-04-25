import _ from 'lodash';
import { OptionsTopBarButton } from '../../src';
import { events } from '../Stores/EventsStore';
import ParentNode from './ParentNode';

export default class ComponentNode extends ParentNode {
  componentDidMountOnce = false;
  componentDidAppearPending = false;

  constructor(layout: any, parentNode?: ParentNode) {
    super(layout, 'Component', parentNode);
  }

  public componentDidMount() {
    this.componentDidMountOnce = true;
    this.componentDidAppearPending && this.componentDidAppear();
  }

  public componentDidAppear() {
    if (this.componentDidMountOnce) {
      setTimeout(() => {
        events.invokeComponentWillAppear({
          componentName: this.data.name,
          componentId: this.nodeId,
          componentType: 'Component',
        });
        events.invokeComponentDidAppear({
          componentName: this.data.name,
          componentId: this.nodeId,
          componentType: 'Component',
        });

        this.buttonsDidAppear(
          _.concat(
            this.data.options.topBar?.rightButtons || [],
            this.data.options.topBar?.leftButtons || []
          )
        );
        this.titleChanged(undefined, this.data.options.topBar?.title);
      }, 0);
    } else {
      this.componentDidAppearPending = true;
    }
  }

  public componentDidDisappear() {
    setTimeout(() => {
      events.invokeComponentDidDisappear({
        componentName: this.data.name,
        componentId: this.nodeId,
        componentType: 'Component',
      });

      this.buttonsDidDisappear(
        _.concat(
          this.data.options.topBar?.rightButtons || [],
          this.data.options.topBar?.leftButtons || []
        )
      );
      this.titleChanged(this.data.options.topBar?.title);
    }, 0);
  }

  titleChanged(oldTitle: any, newTitle?: any) {
    if (oldTitle && oldTitle.component) {
      events.invokeComponentDidDisappear({
        componentName: oldTitle.component.name,
        componentId: oldTitle.component.componentId,
        componentType: 'TopBarTitle',
      });
    }

    if (newTitle && newTitle.component) {
      events.invokeComponentWillAppear({
        componentName: newTitle.component.name,
        componentId: newTitle.component.componentId,
        componentType: 'TopBarTitle',
      });
      events.invokeComponentDidAppear({
        componentName: newTitle.component.name,
        componentId: newTitle.component.componentId,
        componentType: 'TopBarTitle',
      });
    }
  }

  buttonsChanged(oldButtons: OptionsTopBarButton[], newButtons: OptionsTopBarButton[]) {
    this.buttonsDidDisappear(oldButtons);
    this.buttonsDidAppear(newButtons);
  }

  buttonsDidAppear(buttons: OptionsTopBarButton[] = []) {
    buttons.forEach((button: OptionsTopBarButton) => {
      if (button.component) {
        events.invokeComponentWillAppear({
          componentName: button.component.name,
          // @ts-ignore
          componentId: button.component.componentId,
          componentType: 'TopBarButton',
        });

        events.invokeComponentDidAppear({
          componentName: button.component.name,
          // @ts-ignore
          componentId: button.component.componentId,
          componentType: 'TopBarButton',
        });
      }
    });
  }

  buttonsDidDisappear(buttons: OptionsTopBarButton[] = []) {
    buttons.forEach((button: OptionsTopBarButton) => {
      if (button.component) {
        events.invokeComponentDidDisappear({
          componentName: button.component.name,
          // @ts-ignore
          componentId: button.component.componentId,
          componentType: 'TopBarButton',
        });
      }
    });
  }

  getVisibleLayout() {
    return this;
  }
}
