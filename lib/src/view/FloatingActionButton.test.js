const React = require('react');
const renderer = require('react-test-renderer');

const FloatingActionButton = require('./FloatingActionButton');

describe('Floating action button', () => {
  it('renders correct type', () => {
    const tree = renderer.create(<FloatingActionButton />);
    expect(tree.toJSON().type).toEqual('RNNFloatingActionButton');
  });

  it('contains props', () => {
    const tree = renderer.create(<FloatingActionButton icon="file:/icon.png" />);
    expect(tree.toJSON().props.icon).toEqual('file:/icon.png');
  });
});
