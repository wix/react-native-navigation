const URL = 'https://wix.github.io/react-native-navigation/v2/#/docs/';
const isLetter = (c) => c.toLowerCase() !== c.toUpperCase();
const isClass = (type) => isLetter(type[0]) && (type.includes(':') || type[0] === type[0].toUpperCase());
const stripArray = (type) => type.substring(type.indexOf('<') + 1, type.indexOf('>'));
const classUrl = (type) => URL + (type.includes('Array.<') ? stripArray(type) : type).replace(':', '/');
const className = (type) => type.includes('Array.<') ? className(stripArray(type)) + '[]' : type.substring(type.indexOf(':') + 1);
const url = (type) => `<a href="${classUrl(type)}">${className(type)}</a>`;
const simpleString = (type) => `<code>${type}</code>`;
exports.linkify = (type) => isClass(type) ? url(type) : simpleString(type);
