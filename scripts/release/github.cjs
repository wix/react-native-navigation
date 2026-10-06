const adapter = require('./rnn.cjs');
const {
  check,
  assertReleaseContext,
  isReleaseRunPath,
  assertNativeCI,
  compareVersions,
} = require('./policy.cjs');
const prefix = `/repos/${adapter.repository}`;

async function authorize(github, context) {
  assertReleaseContext(context);
  for (const actor of new Set([context.actor, context.triggeringActor])) {
    check(/^[a-zA-Z0-9-]+(?:\[bot\])?$/.test(actor), 'Invalid GitHub actor');
    const user = await github.request(
      'GET',
      `${prefix}/collaborators/${encodeURIComponent(actor)}/permission`
    );
    check(user.permission === 'admin', `${actor} must have repository admin permission`);
  }
}

async function nativeCI(github, sha) {
  check(/^[a-f0-9]{40}$/.test(sha), 'Native CI requires an exact source SHA');
  const statuses = await github.request('GET', `${prefix}/commits/${sha}/statuses?per_page=100`);
  return assertNativeCI(statuses, sha);
}

async function checkSourceRun(github, runId) {
  check(/^[1-9]\d*$/.test(runId), 'Resume requires a numeric source_run_id');
  const run = await github.request('GET', `${prefix}/actions/runs/${runId}`);
  check(
    run.event === 'workflow_dispatch' &&
      run.head_branch === adapter.branch &&
      isReleaseRunPath(run.path) &&
      run.repository.full_name === adapter.repository &&
      run.head_repository.full_name === adapter.repository &&
      run.status === 'completed',
    'Resume source must be a completed upstream master release workflow'
  );
  const artifacts = [];
  for (let page = 1; ; page++) {
    const batch = await github.request(
      'GET',
      `${prefix}/actions/runs/${runId}/artifacts?per_page=100&page=${page}`
    );
    artifacts.push(...batch.artifacts);
    if (batch.artifacts.length < 100) break;
  }
  const candidates = artifacts
    .filter((item) => new RegExp(`^release-${runId}-[1-9]\\d*$`).test(item.name) && !item.expired)
    .sort((a, b) => Number(b.id) - Number(a.id));
  check(candidates.length, 'No retained release artifact; automatic resume is unavailable');
  return { run, artifact: candidates[0] };
}

async function ensureBranch(github, name, sha) {
  const endpoint = `${prefix}/git/ref/heads/${name}`;
  const existing = await github.request('GET', endpoint, undefined, { allow404: true });
  if (existing) {
    check(
      existing.object.sha === sha,
      `Release branch ${name} already points to a different commit`
    );
    return;
  }
  await github.request('POST', `${prefix}/git/refs`, { ref: `refs/heads/${name}`, sha });
}

async function ensureTag(github, tag, sha) {
  const existing = await github.request('GET', `${prefix}/git/ref/tags/${tag}`, undefined, {
    allow404: true,
  });
  if (existing) {
    const target =
      existing.object.type === 'tag'
        ? await github.request('GET', `${prefix}/git/tags/${existing.object.sha}`)
        : existing;
    check(target.object.sha === sha, `Tag ${tag} already points to a different commit`);
    return;
  }
  const object = await github.request('POST', `${prefix}/git/tags`, {
    tag,
    message: tag,
    object: sha,
    type: 'commit',
  });
  await github.request('POST', `${prefix}/git/refs`, { ref: `refs/tags/${tag}`, sha: object.sha });
}

async function ensureRelease(github, manifest, notes) {
  const pkg = manifest.packages[0];
  const existing = await github.request(
    'GET',
    `${prefix}/releases/tags/${pkg.version}`,
    undefined,
    { allow404: true }
  );
  if (existing) {
    check(
      !existing.draft && existing.prerelease === (pkg.npmTag !== 'latest'),
      'Existing GitHub release has conflicting state'
    );
    return existing;
  }
  // Resuming an old release must never replace a newer GitHub "latest" release.
  const latest = await github.request('GET', `${prefix}/releases/latest`, undefined, {
    allow404: true,
  });
  let makeLatest = pkg.npmTag === 'latest';
  if (makeLatest && latest) {
    try {
      makeLatest = compareVersions(pkg.version, latest.tag_name) > 0;
    } catch {
      makeLatest = false;
    }
  }
  return github.request('POST', `${prefix}/releases`, {
    tag_name: pkg.version,
    target_commitish: manifest.releaseSha,
    name: pkg.version,
    body: notes,
    draft: false,
    prerelease: pkg.npmTag !== 'latest',
    make_latest: makeLatest ? 'true' : 'false',
  });
}

async function ensurePullRequest(github, manifest, template = '') {
  const pkg = manifest.packages[0];
  const branch = `ci/update-version-${pkg.version}`;
  const results = await github.request(
    'GET',
    `${prefix}/pulls?state=all&head=wix:${branch}&base=${adapter.branch}&per_page=100`
  );
  const existing = results.find((pr) => pr.head.ref === branch && pr.base.ref === adapter.branch);
  let pr = existing;
  if (existing) {
    check(
      existing.head.sha === manifest.releaseSha,
      'Version PR no longer matches the release commit'
    );
    check(
      existing.state === 'open' || existing.merged_at,
      'Version PR was closed without merging; resolve it manually'
    );
  } else {
    pr = await github.request('POST', `${prefix}/pulls`, {
      title: `Release: update version to ${pkg.version}`,
      head: branch,
      base: adapter.branch,
      body: `${template ? `${template.trim()}\n\n` : ''}Updates release metadata${
        manifest.documentationVersion ? ' and versioned documentation' : ''
      } for react-native-navigation ${pkg.version}.\n\nPublished from ${
        manifest.sourceSha
      }.\n\nRelease run: https://github.com/${adapter.repository}/actions/runs/${
        manifest.runId
      }\n\nThe workflow verified the package tarball and registry integrity. Review and merge this version update; no native source changes are included.`,
    });
  }
  const label = await github.request('GET', `${prefix}/labels/${adapter.label}`, undefined, {
    allow404: true,
  });
  if (!label)
    await github.request('POST', `${prefix}/labels`, {
      name: adapter.label,
      color: '0e8a16',
      description: 'Release metadata and publishing',
    });
  await github.request('POST', `${prefix}/issues/${pr.number}/labels`, { labels: [adapter.label] });
  return pr;
}

module.exports = {
  prefix,
  authorize,
  nativeCI,
  checkSourceRun,
  ensureBranch,
  ensureTag,
  ensureRelease,
  ensurePullRequest,
};
