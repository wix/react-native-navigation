const React = require('react');
require('react-native');
const { render } = require('@testing-library/react-native');
const { Navigation } = require('../../lib/src/index');

describe('remx support', () => {
  let MyConnectedComponent;
  let store;

  beforeEach(() => {
    MyConnectedComponent = require('./MyComponent');
    store = require('./MyStore');
  });

  it('renders normally', () => {
    const { getByText } = render(<MyConnectedComponent />);
    const { getByText: getByText2 } = render(<MyConnectedComponent />);
    expect(getByText('no name')).toBeTruthy();
    expect(getByText2('no name')).toBeTruthy();
  });

  it('rerenders as a result of an underlying state change (by selector)', () => {
    const renderCountIncrement = jest.fn();
    const { getByText, rerender } = render(
      <MyConnectedComponent renderCountIncrement={renderCountIncrement} />
    );

    expect(getByText('no name')).toBeTruthy();
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setName('Bob');
    expect(store.getters.getName()).toEqual('Bob');

    // Re-render to get updated content
    rerender(<MyConnectedComponent renderCountIncrement={renderCountIncrement} />);
    expect(getByText('Bob')).toBeTruthy();

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('rerenders as a result of an underlying state change with a new key', () => {
    const renderCountIncrement = jest.fn();
    const { queryByText, rerender } = render(
      <MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />
    );

    // Initially should show nothing (null children means no text)
    expect(queryByText('30')).toBeNull();
    expect(renderCountIncrement).toHaveBeenCalledTimes(1);

    store.setters.setAge(30);
    expect(store.getters.getAge()).toEqual(30);

    // Re-render to get updated content
    rerender(<MyConnectedComponent printAge={true} renderCountIncrement={renderCountIncrement} />);
    expect(queryByText('30')).toBeTruthy();

    expect(renderCountIncrement).toHaveBeenCalledTimes(2);
  });

  it('support for static members in connected components', () => {
    expect(MyConnectedComponent.options).toEqual({ title: 'MyComponent' });

    const registeredComponentClass = Navigation.registerComponent(
      'MyComponentName',
      () => MyConnectedComponent
    )();
    expect(registeredComponentClass.options).toEqual({ title: 'MyComponent' });
  });
});
