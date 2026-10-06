const { test } = require('node:test');
const assert = require('node:assert/strict');
const { renderNotes } = require('./notes.cjs');

const sha = 'a'.repeat(40);
function pr(number, title, labels = [], extra = {}) {
  return {
    number,
    title,
    html_url: 'https://example.com/not-canonical',
    user: { login: 'contributor' },
    labels: labels.map((name) => ({ name })),
    merged_at: '2026-01-02T03:04:05Z',
    ...extra,
  };
}

test('renders categories, platform subgroups, canonical links, source and compare commits', () => {
  const markdown = renderNotes({
    version: '8.9.0',
    previousTag: 'v8.8.9',
    sourceSha: sha,
    pullRequests: [
      pr(12, 'Fix bug', ['type: accepted/bug']),
      pr(4, 'New feature', ['feature']),
      pr(7, 'iOS improvement', ['internal', 'platform: iOS']),
      pr(9, 'Android improvement', ['type: accepted/enhancement', 'platform: Android']),
      pr(10, 'Cross platform', ['feature', 'platform: iOS', 'platform: Android']),
      pr(15, 'Uncategorized'),
    ],
  });
  assert.match(markdown, /# 8\.9\.0/);
  assert.match(markdown, new RegExp(`Source commit: \\[${sha}\\]`));
  assert.match(markdown, /compare\/v8\.8\.9\.\.\.[a-f0-9]{40}/);
  assert.ok(
    markdown.indexOf('[New feature](https://github.com/wix/react-native-navigation/pull/4)') <
      markdown.indexOf('/pull/10')
  );
  assert.match(markdown, /## Features[\s\S]*### iOS only[\s\S]*### Android only/);
  assert.match(markdown, /## Enhancements[\s\S]*### iOS only[\s\S]*### Android only/);
  assert.match(markdown, /## Fixed[\s\S]*Fix bug/);
  assert.match(markdown, /## Other[\s\S]*Uncategorized/);
  assert.doesNotMatch(markdown, /example\.com/);
});

test('omits skipped and unmerged PRs and deduplicates by number deterministically', () => {
  const markdown = renderNotes({
    version: '8.9.0',
    previousTag: '',
    sourceSha: sha,
    pullRequests: [
      pr(8, 'z duplicate'),
      pr(3, 'skip this', ['skip-changelog']),
      pr(6, 'not merged', [], { merged_at: null }),
      pr(8, 'a duplicate'),
    ],
  });
  assert.equal((markdown.match(/\/pull\/8/g) || []).length, 1);
  assert.match(markdown, /\[a duplicate\]/);
  assert.doesNotMatch(markdown, /skip this|not merged|z duplicate|Full changes/);
});

test('escapes title, author, and version content and uses the requested canonical repository', () => {
  const markdown = renderNotes({
    version: '8.9.0<script>',
    previousTag: 'release/8.8.9',
    sourceSha: sha,
    repository: 'example/project',
    pullRequests: [
      pr(2, 'bad ](https://evil.test) <img src=x> **title**', ['feature'], {
        user: { login: 'name](javascript:alert(1))' },
      }),
    ],
  });
  assert.match(markdown, /# 8\.9\.0&lt;script&gt;/);
  assert.match(markdown, /https:\/\/github\.com\/example\/project\/pull\/2/);
  assert.match(markdown, /bad \\\]\\\(https:\/\/evil\.test\\\)/);
  assert.match(markdown, /&lt;img src=x&gt;/);
  assert.match(markdown, /\\\*\\\*title\\\*\\\*/);
  assert.match(markdown, /@name\\\]\\\(javascript:alert\\\(1\\\)\\\)/);
});
