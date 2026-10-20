const { test } = require('node:test');
const assert = require('node:assert/strict');
const { jsonRequest } = require('./http.cjs');

test('only an explicitly allowed 404 means a package is absent', async () => {
  const fetchImpl = async () => ({ status: 404, ok: false });
  assert.equal(
    await jsonRequest('https://registry.npmjs.org/p/1', { fetchImpl, allow404: true }),
    null
  );
  await assert.rejects(
    jsonRequest('https://registry.npmjs.org/p/latest', { fetchImpl }),
    /HTTP 404/
  );
  await assert.rejects(
    jsonRequest('https://registry.npmjs.org/p/1', {
      fetchImpl: async () => ({ status: 401, ok: false }),
      allow404: true,
    }),
    /HTTP 401/
  );
});

test('registry outages retry and fail rather than selecting a new version', async () => {
  let attempts = 0;
  await assert.rejects(
    jsonRequest('https://registry.npmjs.org/p/1', {
      fetchImpl: async () => {
        attempts++;
        return { status: 503, ok: false };
      },
      sleep: async () => {},
      allow404: true,
    }),
    /HTTP 503/
  );
  assert.equal(attempts, 3);
});

test('network errors cannot be mistaken for a missing version', async () => {
  await assert.rejects(
    jsonRequest('https://registry.npmjs.org/p/1', {
      fetchImpl: async () => {
        throw new Error('offline');
      },
      allow404: true,
    }),
    /offline/
  );
});
