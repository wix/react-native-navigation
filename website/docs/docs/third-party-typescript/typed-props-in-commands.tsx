class Props: {
  name: string
}

Navigation.push<Props>(componentId, {
  component: {
    name: 'MyComponent',
    passProps: {
      name: 'Bob',
      color: 'red', // Compilation error! color isn't declared in Props
    }
  }
});
