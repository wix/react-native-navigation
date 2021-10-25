import React from 'react';
import { Button, View } from 'react-native-ui-lib';
import {
  Navigation,
  Options,
  NavigationComponent,
  NavigationComponentProps,
} from 'react-native-navigation';
import Root from '../components/Root';

interface Props {
  flag: boolean;
  componentId: string;
}

class StressedComponent extends React.Component<Props> {
  constructor(props: Props) {
    super(props);
  }

  componentDidUpdate() {
    console.log('mergeOptiosn called form componentDidUpdate');
    let buttons = [];
    if (this.props.flag) {
      buttons.push({
        text: 'Save',
        id: 'save',
        testID: 'nav-save',
        disabledColor: 'gray',
        enabled: this.props.flag,
      });
    } else {
      buttons = [];
    }
    Navigation.mergeOptions(this.props.componentId, {
      topBar: {
        animateLeftButtons: true,
        animateRightButtons: true,
        title: {
          text: `MergeOptions Stress Test ${this.props.flag ? ' Looooooooooooooooooong' : 'Short'}`,
        },
        rightButtons: buttons,
        leftButtons: buttons,
      },
    });
  }
  render() {
    return (
      <View style={{ flex: 1, flexDirection: 'column' }}>
        <Button label={'HExttt'} onPress={() => console.log('Hello')} />
      </View>
    );
  }
}

interface State {
  flag: boolean;
  componentId: string;
}

function getRandomInt(max: number) {
  return Math.floor(Math.random() * max);
}

export default class LayoutsScreen extends NavigationComponent<NavigationComponentProps, State> {
  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'MergeOptions Stress Test',
        },
      },
      layout: {
        orientation: ['portrait', 'landscape'],
      },
    };
  }
  private interval?: any;

  constructor(props: NavigationComponentProps) {
    super(props);
    Navigation.events().bindComponent(this);
    this.state = {
      componentId: this.props.componentId,
      flag: true,
    };
  }
  resetInterval = () => {
    if (this.interval != null) clearInterval(this.interval);
    this.interval = null;
  };
  componentWillUnmount() {
    this.resetInterval();
  }

  asyncState = async (obj: State) => {
    setTimeout(() => {
      this.setState(obj);
    }, getRandomInt(500));
  };
  stressState = () => {
    if (this.interval == null) {
      this.interval = setInterval(() => {
        this.asyncState({ flag: !this.state.flag, componentId: this.props.componentId });
      }, 50);
    } else {
      this.resetInterval();
    }
  };
  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label={`Toggle Stress MergeOptions ${this.state.flag}`}
          onPress={this.stressState}
        />
        <StressedComponent componentId={this.state.componentId} flag={this.state.flag} />
      </Root>
    );
  }
}
