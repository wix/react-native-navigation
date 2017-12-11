const Button = require('./Button');

const BUTTON = {
  id: 'myBtn',
  testID: 'BTN',
  title: 'My Button',
  color: 'red'
};

describe('Button', () => {
  it('parses button', () => {
    const uut = new Button(BUTTON);
    expect(uut.id).toEqual('myBtn');
    expect(uut.testID).toEqual('BTN');
    expect(uut.title).toEqual('My Button');
    expect(uut.color).toEqual('red');
  });
});
