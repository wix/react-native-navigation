import { Navigation } from 'react-native-navigation';

function showUserProfileScreen(user: User) {
  Navigation.push(componentId, {
    component: {
      name: 'ProfileScreen',
      passProps: { user },
      options: {
        topBar: {
          title: {
            text: user.name,
          },
        },
      },
    },
  });
}
