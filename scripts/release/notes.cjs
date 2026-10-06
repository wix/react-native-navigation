const CATEGORIES = [
  { title: 'Features', labels: ['feature'] },
  { title: 'Enhancements', labels: ['type: accepted/enhancement', 'internal'] },
  { title: 'Fixed', labels: ['type: accepted/bug'] },
  { title: 'Other', labels: [] },
];

function escapeMarkdown(value) {
  return String(value ?? '')
    .replace(/[<>]/g, (char) => (char === '<' ? '&lt;' : '&gt;'))
    .replace(/[\\`*_{}\[\]()#+!|~]/g, '\\$&');
}

function labelsOf(pr) {
  return (Array.isArray(pr.labels) ? pr.labels : [])
    .map((label) => (typeof label === 'string' ? label : label?.name))
    .filter((label) => typeof label === 'string');
}

function comparePRs(a, b) {
  return (
    a.number - b.number ||
    String(a.title).localeCompare(String(b.title)) ||
    String(a.user?.login).localeCompare(String(b.user?.login))
  );
}

function categoryOf(labels) {
  for (const category of CATEGORIES.slice(0, -1)) {
    if (category.labels.some((label) => labels.includes(label))) return category.title;
  }
  return 'Other';
}

function platformOf(labels) {
  const ios = labels.includes('platform: iOS');
  const android = labels.includes('platform: Android');
  if (ios === android) return 'general';
  return ios ? 'iOS only' : 'Android only';
}

function renderPR(pr, repository) {
  const title = escapeMarkdown(pr.title);
  const author = pr.user?.login ? ` — @${escapeMarkdown(pr.user.login)}` : '';
  return `- [${title}](https://github.com/${repository}/pull/${pr.number})${author}`;
}

function renderNotes({
  version,
  previousTag,
  sourceSha,
  pullRequests,
  repository = 'wix/react-native-navigation',
}) {
  if (!/^[\w.-]+\/[\w.-]+$/.test(repository)) throw new TypeError('Invalid GitHub repository');
  if (typeof version !== 'string' || !version) throw new TypeError('A release version is required');
  if (!/^[a-f0-9]{40}$/i.test(sourceSha))
    throw new TypeError('A full source commit SHA is required');

  const byNumber = new Map();
  for (const pr of pullRequests || []) {
    if (!Number.isSafeInteger(pr.number) || pr.number < 1 || !pr.merged_at) continue;
    const labels = labelsOf(pr);
    if (labels.includes('skip-changelog')) continue;
    const candidate = { ...pr, _labels: labels };
    const existing = byNumber.get(pr.number);
    if (!existing || comparePRs(candidate, existing) < 0) byNumber.set(pr.number, candidate);
  }

  const prs = [...byNumber.values()].sort((a, b) => a.number - b.number);
  const lines = [
    `# ${escapeMarkdown(version)}`,
    '',
    `Source commit: [${sourceSha}](https://github.com/${repository}/commit/${sourceSha})`,
  ];
  if (previousTag) {
    lines.push(
      `Full changes: [${escapeMarkdown(
        previousTag
      )}...${sourceSha}](https://github.com/${repository}/compare/${encodeURIComponent(
        previousTag
      )}...${sourceSha})`
    );
  }

  for (const category of CATEGORIES) {
    const categoryPRs = prs.filter((pr) => categoryOf(pr._labels) === category.title);
    if (!categoryPRs.length) continue;
    lines.push('', `## ${category.title}`);
    const groups = new Map([
      ['general', []],
      ['iOS only', []],
      ['Android only', []],
    ]);
    for (const pr of categoryPRs) groups.get(platformOf(pr._labels)).push(pr);
    for (const [platform, groupPRs] of groups) {
      if (!groupPRs.length) continue;
      if (platform !== 'general') lines.push('', `### ${platform}`);
      for (const pr of groupPRs) lines.push(renderPR(pr, repository));
    }
  }
  return `${lines.join('\n')}\n`;
}

module.exports = { renderNotes };
