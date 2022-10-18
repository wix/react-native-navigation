import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  order: OrderDetails;
}

class OrderScreen extends NavigationComponent<Props> {
  static options(props: Props): Options {
    return {
      topBar: {
        title: {
          text: props.order.orderId,
        },
      },
    };
  }
}
