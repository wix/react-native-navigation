import { ParsedURL } from './types';

export class URLParser {
  public stripPrefix(url: string, prefixes: string[]): string | null {
    const sorted = [...prefixes].sort((a, b) => b.length - a.length);
    for (const prefix of sorted) {
      if (url.startsWith(prefix)) {
        return url.slice(prefix.length);
      }
    }
    return null;
  }

  public parse(url: string, prefixes: string[]): ParsedURL | null {
    const stripped = this.stripPrefix(url, prefixes);
    if (stripped === null) {
      return null;
    }

    const [pathPart, queryPart] = stripped.split('?', 2);

    const path = pathPart
      .split('/')
      .filter((s) => s.length > 0)
      .join('/');

    const queryParams: Record<string, string> = {};
    if (queryPart) {
      for (const pair of queryPart.split('&')) {
        const [key, ...rest] = pair.split('=');
        if (key) {
          queryParams[decodeURIComponent(key)] = decodeURIComponent(rest.join('=') || '');
        }
      }
    }

    return { path, queryParams };
  }
}
