const React = require('react');
const { Platform } = require('react-native');
const renderer = require('react-test-renderer');

const NestedScrollView = require('./NestedScrollView');

function uut() {
  return (
    <NestedScrollView />
  );
}

describe('Nested ScrollView', () => {
  beforeEach(() => {
    Platform.OS = 'android';
  });

  it('checks render correct type for android', () => {
    const tree = renderer.create(uut());
    expect(tree.toJSON().type).toEqual('RnnNestedScrollView');
  });

  it('checks render correct type for ios', () => {
    Platform.OS = 'ios';
    const tree = renderer.create(uut());
    expect(tree.toJSON().type).toEqual('RCTScrollView');
  });

  it('checks scrol view has container for children', () => {
    const tree = renderer.create(uut());
    expect(tree.toJSON().children[0].type).toEqual('View');
  });
});
