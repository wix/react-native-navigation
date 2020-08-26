import { NavigationFunctionalComponent, Options } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  order: OrderDetails;
}

const OrderScreen: NavigationFunctionComponent<Props> = (props: Props) => {};

OrderScreen.options = (props: Props) => {
  return {
    topBar: {
      title: {
        text: props.order.orderId,
      },
    },
  };
};
