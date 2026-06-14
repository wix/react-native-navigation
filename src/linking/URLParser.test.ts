import { URLParser } from './URLParser';

describe('URLParser', () => {
  let uut: URLParser;

  beforeEach(() => {
    uut = new URLParser();
  });

  describe('stripPrefix', () => {
    it('returns the remainder after the matching prefix', () => {
      expect(uut.stripPrefix('myapp://home', ['myapp://'])).toBe('home');
    });

    it('returns null when no prefix matches', () => {
      expect(uut.stripPrefix('other://home', ['myapp://'])).toBeNull();
    });

    it('chooses the longest matching prefix', () => {
      const result = uut.stripPrefix('https://myapp.com/v2/home', [
        'https://myapp.com',
        'https://myapp.com/v2',
      ]);
      expect(result).toBe('/home');
    });
  });

  describe('parse', () => {
    const prefixes = ['myapp://', 'https://myapp.com'];

    it('returns null when no prefix matches', () => {
      expect(uut.parse('other://home', prefixes)).toBeNull();
    });

    it('parses a simple path', () => {
      expect(uut.parse('myapp://home', prefixes)).toEqual({
        path: 'home',
        queryParams: {},
      });
    });

    it('trims leading and trailing slashes', () => {
      expect(uut.parse('myapp:///home/', prefixes)).toEqual({
        path: 'home',
        queryParams: {},
      });
    });

    it('parses query parameters', () => {
      expect(uut.parse('myapp://search?q=hello&page=2', prefixes)).toEqual({
        path: 'search',
        queryParams: { q: 'hello', page: '2' },
      });
    });

    it('decodes URL-encoded path segments', () => {
      expect(uut.parse('myapp://hello%20world', prefixes)).toEqual({
        path: 'hello world',
        queryParams: {},
      });
    });

    it('decodes URL-encoded query parameter keys and values', () => {
      expect(uut.parse('myapp://search?q=hello%20world&a%20b=c', prefixes)).toEqual({
        path: 'search',
        queryParams: { q: 'hello world', 'a b': 'c' },
      });
    });

    it('strips fragments before parsing the query', () => {
      expect(uut.parse('myapp://home?x=1#section', prefixes)).toEqual({
        path: 'home',
        queryParams: { x: '1' },
      });
    });

    it('strips fragments when there is no query string', () => {
      expect(uut.parse('myapp://home#section', prefixes)).toEqual({
        path: 'home',
        queryParams: {},
      });
    });

    it('treats query keys without values as empty strings', () => {
      expect(uut.parse('myapp://home?flag', prefixes)).toEqual({
        path: 'home',
        queryParams: { flag: '' },
      });
    });

    it('handles malformed encoding without throwing', () => {
      expect(uut.parse('myapp://%E0%A4%A', prefixes)).toEqual({
        path: '%E0%A4%A',
        queryParams: {},
      });
    });

    it('keeps "=" inside values intact', () => {
      expect(uut.parse('myapp://search?token=a=b=c', prefixes)).toEqual({
        path: 'search',
        queryParams: { token: 'a=b=c' },
      });
    });
  });
});
