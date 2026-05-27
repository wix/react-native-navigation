import { ParsedURL } from './types';

/**
 * Parses a URL string against a list of configured prefixes.
 *
 * Responsibilities:
 *   - Strip the longest matching prefix (or report no match).
 *   - Drop any `#fragment` portion.
 *   - Decode each path segment so literal patterns compare against decoded
 *     text (e.g. `hello%20world` matches a `hello world` pattern segment).
 *   - Parse the query string into a plain key/value map.
 */
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

    const withoutFragment = stripped.split('#', 1)[0];
    const [pathPart, queryPart] = withoutFragment.split('?', 2);

    const path = pathPart
      .split('/')
      .filter((s) => s.length > 0)
      .map((segment) => safeDecode(segment))
      .join('/');

    const queryParams: Record<string, string> = {};
    if (queryPart) {
      for (const pair of queryPart.split('&')) {
        if (!pair) continue;
        const eqIndex = pair.indexOf('=');
        const rawKey = eqIndex === -1 ? pair : pair.slice(0, eqIndex);
        const rawValue = eqIndex === -1 ? '' : pair.slice(eqIndex + 1);
        const key = safeDecode(rawKey);
        if (!key) continue;
        queryParams[key] = safeDecode(rawValue);
      }
    }

    return { path, queryParams };
  }
}

function safeDecode(value: string): string {
  try {
    return decodeURIComponent(value);
  } catch {
    return value;
  }
}
