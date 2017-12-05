const React = require('react');
const { Platform } = require('react-native');
const renderer = require('react-test-renderer');

const FloatingActionButton = require('./FloatingActionButton');
const CoordinatorLayout = require('./CoordinatorLayout');

function uut() {
  return (
    <CoordinatorLayout>
      <FloatingActionButton />
    </CoordinatorLayout >
  );
}

describe('Coordinator Layout', () => {
  beforeEach(() => {
    Platform.OS = 'android';
  });

  it('checks render correct type for android', () => {
    const tree = renderer.create(uut());
    expect(tree.toJSON().type).toEqual('RNNCoordinatorLayout');
  });

  it('checks render correct type for ios', () => {
    Platform.OS = 'ios';
    const tree = renderer.create(uut());
    expect(tree.toJSON().type).toEqual('View');
  });

  it('checks render contains Fab as a child', () => {
    const tree = renderer.create(uut());
    expect(tree.toJSON().children[0].type).toEqual('RNNFloatingActionButton');
  });
});
