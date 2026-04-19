import { URLParser } from './URLParser';

describe('URLParser', () => {
  let uut: URLParser;

  beforeEach(() => {
    uut = new URLParser();
  });

  describe('stripPrefix', () => {
    it('strips matching custom scheme prefix', () => {
      expect(uut.stripPrefix('myapp://user/123', ['myapp://'])).toBe('user/123');
    });

    it('strips matching https prefix', () => {
      expect(uut.stripPrefix('https://myapp.com/settings', ['https://myapp.com'])).toBe(
        '/settings'
      );
    });

    it('returns null when no prefix matches', () => {
      expect(uut.stripPrefix('other://foo', ['myapp://'])).toBeNull();
    });

    it('picks the longest matching prefix', () => {
      const prefixes = ['https://myapp.com', 'https://myapp.com/app'];
      expect(uut.stripPrefix('https://myapp.com/app/profile', prefixes)).toBe('/profile');
    });

    it('handles empty url', () => {
      expect(uut.stripPrefix('', ['myapp://'])).toBeNull();
    });
  });

  describe('parse', () => {
    const prefixes = ['myapp://', 'https://myapp.com'];

    it('parses simple path', () => {
      const result = uut.parse('myapp://home', prefixes);
      expect(result).toEqual({ path: 'home', queryParams: {} });
    });

    it('parses nested path', () => {
      const result = uut.parse('myapp://user/42/posts', prefixes);
      expect(result).toEqual({ path: 'user/42/posts', queryParams: {} });
    });

    it('parses query parameters', () => {
      const result = uut.parse('myapp://search?q=hello&page=2', prefixes);
      expect(result).toEqual({
        path: 'search',
        queryParams: { q: 'hello', page: '2' },
      });
    });

    it('handles encoded query values', () => {
      const result = uut.parse('myapp://search?q=hello%20world', prefixes);
      expect(result).toEqual({
        path: 'search',
        queryParams: { q: 'hello world' },
      });
    });

    it('strips trailing slashes from path', () => {
      const result = uut.parse('myapp://home/', prefixes);
      expect(result).toEqual({ path: 'home', queryParams: {} });
    });

    it('normalizes multiple slashes', () => {
      const result = uut.parse('myapp://user//42', prefixes);
      expect(result).toEqual({ path: 'user/42', queryParams: {} });
    });

    it('returns null for unmatched prefix', () => {
      expect(uut.parse('other://home', prefixes)).toBeNull();
    });

    it('handles https prefix with path', () => {
      const result = uut.parse('https://myapp.com/user/5', prefixes);
      expect(result).toEqual({ path: 'user/5', queryParams: {} });
    });

    it('handles query param with equals in value', () => {
      const result = uut.parse('myapp://callback?token=abc=def', prefixes);
      expect(result).toEqual({
        path: 'callback',
        queryParams: { token: 'abc=def' },
      });
    });

    it('handles empty path after prefix', () => {
      const result = uut.parse('myapp://', prefixes);
      expect(result).toEqual({ path: '', queryParams: {} });
    });
  });
});
