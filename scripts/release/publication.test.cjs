const { test } = require('node:test');
const assert = require('node:assert/strict');
const { publishPackages } = require('./publication.cjs');

const pkg = { name: 'example', version: '2.0.0', npmTag: 'latest', integrity: 'sha512-exact' };
const published = (p) => ({ name: p.name, version: p.version, dist: { integrity: p.integrity } });

test('a lost response after npm success resumes without a second publish', async () => {
  let registry = null;
  let calls = 0;
  const lookup = async (_name, selected) =>
    selected === 'latest' ? { version: '1.0.0' } : registry;
  await assert.rejects(
    publishPackages([pkg], {
      lookup,
      publish: async (p) => {
        calls++;
        registry = published(p);
        throw new Error('connection lost');
      },
    }),
    /connection lost/
  );
  await publishPackages([pkg], {
    lookup,
    publish: async () => {
      calls++;
    },
  });
  assert.equal(calls, 1);
});

test('a partial multi-package publication resumes only the remaining package', async () => {
  const second = { ...pkg, name: 'second' };
  const registry = new Map();
  const calls = [];
  const lookup = async (name, selected) =>
    selected === 'latest' ? { version: '1.0.0' } : registry.get(name) || null;
  await assert.rejects(
    publishPackages([pkg, second], {
      lookup,
      publish: async (p) => {
        calls.push(p.name);
        if (p.name === 'second') throw new Error('registry unavailable');
        registry.set(p.name, published(p));
      },
    }),
    /registry unavailable/
  );
  await publishPackages([pkg, second], {
    lookup,
    publish: async (p) => {
      calls.push(p.name);
      registry.set(p.name, published(p));
    },
  });
  assert.deepEqual(calls, ['example', 'second', 'second']);
});

test('conflicting bytes and channel regression never reach npm publish', async () => {
  let calls = 0;
  await assert.rejects(
    publishPackages([pkg], {
      lookup: async () => published({ ...pkg, integrity: 'sha512-other' }),
      publish: async () => {
        calls++;
      },
    }),
    /Conflicting/
  );
  await assert.rejects(
    publishPackages([pkg], {
      lookup: async (_name, selected) => (selected === 'latest' ? { version: '3.0.0' } : null),
      publish: async () => {
        calls++;
      },
    }),
    /backward/
  );
  assert.equal(calls, 0);
});

test('an unconfirmed publish fails, preserving the selected version for recovery', async () => {
  await assert.rejects(
    publishPackages([pkg], {
      lookup: async () => null,
      publish: async () => {},
    }),
    /not confirmed/
  );
});
