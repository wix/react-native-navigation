interface ScreenProps {
  theme: 'light' | 'dark';
}

Navigation.addLayoutProcessor((layout: Layout, commandName: string) => {
  layout.stack?.children!.forEach((child) => {
    if ((child.component?.passProps as ScreenProps)?.theme === 'dark') {
      child.options = {
        topBar: {
          background: {
            color: 'black',
          },
        },
      };
    }
  });
  return layout;
});
