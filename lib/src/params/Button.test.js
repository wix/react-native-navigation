const Button = require('./Button');

const BUTTON = {
  id: 'myBtn',
  testID: 'BTN',
  title: 'My Button',
  buttonColor: 'red',
  buttonFontSize: 16,
  buttonFontWeight: 300,
  showAsAction: 'never',
  disableIconTint: true,
  disabled: true
};

describe('Button', () => {
  it('parses button', () => {
    const uut = new Button(BUTTON);
    expect(uut.id).toEqual('myBtn');
    expect(uut.testID).toEqual('BTN');
    expect(uut.title).toEqual('My Button');
    expect(uut.buttonColor).toEqual('red');
    expect(uut.showAsAction).toEqual('never');
    expect(uut.buttonFontWeight).toEqual(300);
    expect(uut.buttonFontSize).toEqual(16);
    expect(uut.disableIconTint).toEqual(true);
    expect(uut.disabled).toEqual(true);
  });
});
