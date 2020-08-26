import { View } from 'react-native';
import { NavigationComponentProps, NavigationFunctionComponent } from 'react-native-navigation';

interface Props extends NavigationComponentProps {
  order: OrderDetails;
}

const OrderScreen: NavigationFunctionComponent<Props> = (props: Props) => {
  return <View />;
};

OrderScreen.options = (props: Props) => {
  return {
    topBar: {
      title: {
        text: props.order.orderId,
      },
    },
  };
};
