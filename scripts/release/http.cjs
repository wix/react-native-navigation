const { setTimeout: delay } = require('node:timers/promises');
const { check } = require('./policy.cjs');

async function jsonRequest(url, options = {}) {
  const { allow404 = false, fetchImpl = fetch, sleep = delay, ...init } = options;
  for (let attempt = 0; attempt < 3; attempt++) {
    const response = await fetchImpl(url, { ...init, signal: AbortSignal.timeout(30_000) });
    if (response.status === 404 && allow404) return null;
    if ((response.status === 429 || response.status >= 500) && attempt < 2) {
      await sleep(1000 * 2 ** attempt);
      continue;
    }
    // Never echo request headers or arbitrary server response bodies (credentials).
    check(response.ok, `HTTP ${response.status} from ${new URL(url).host}${new URL(url).pathname}`);
    if (response.status === 204) return null;
    return response.json();
  }
  throw new Error('HTTP retry limit reached');
}

function githubClient(token, request = jsonRequest) {
  check(token, 'GH_TOKEN is required');
  return {
    request(method, endpoint, body, options = {}) {
      check(endpoint.startsWith('/repos/'), 'Only repository GitHub API endpoints are allowed');
      return request(`https://api.github.com${endpoint}`, {
        ...options,
        method,
        headers: {
          Authorization: `Bearer ${token}`,
          Accept: 'application/vnd.github+json',
          'X-GitHub-Api-Version': '2022-11-28',
          'Content-Type': 'application/json',
        },
        ...(body === undefined ? {} : { body: JSON.stringify(body) }),
      });
    },
  };
}

function registryVersion(name, selected, options = {}) {
  return jsonRequest(
    `https://registry.npmjs.org/${encodeURIComponent(name)}/${encodeURIComponent(selected)}`,
    options
  );
}

module.exports = { jsonRequest, githubClient, registryVersion };
