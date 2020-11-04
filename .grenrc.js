module.exports = {
  template: {
    commit: ({message, url, author, name}) =>
      `- [${message}](${url}) - ${author ? `@${author}` : name}`,
    issue: ({name, labels, text, url, user_login, user_url}) => `${processLabels(labels)}${name} [${text}](${url}) by [${user_login}](${user_url})`,
    label: '[**{{label}}**]',
    noLabel: 'closed',
    group: '\n## {{heading}}\n',
    changelogTitle: '# Changelog\n\n',
    release: '## {{release}} ({{date}})\n{{body}}',
    releaseSeparator: '\n---\n\n',
  },
  groupBy: {
    'Enhancements:': ['type: accepted/enhancement', 'internal'],
    'Bug fixes:': ['type: accepted/bug'],
    Features: ['feature'],
  },
  groupPostProcessor: (groupContent) => {
    const lines = groupContent.split('\n');
    const iosIssues = [];
    const androidIssues = [];
    const otherIssues = [];

    for (let i = 0; i < lines.length; i++) {
      const line = lines[i];
      if (line.includes('## ')) continue;
      else if (line.includes('[iOS] ')) iosIssues.push(line.replace('[iOS] ', ''));
      else if (line.includes('[Android] ')) androidIssues.push(line.replace('[Android] ', ''));
      else otherIssues.push(line);
    }

    const groupHeader = groupContent.substr(0, groupContent.indexOf(':\n'));
    return `${groupHeader}${generateSection(undefined, otherIssues)}${generateSection('iOS', iosIssues)}${generateSection('Android', androidIssues)}`;
  },
  ignoreIssuesWith: ['skip-changelog'],
  ignoreTagsWith: ['snapshot', 'v1', 'v2', '0\..\..', '1\..\..', '2\..\..', '3\..\..', '4\..\..', '5\..\..', '6\..\..'],
  dataSource: 'prs',
  changelogFilename: 'CHANGELOG.gren.md',
  override: true,
  generate: true,
  tags: 'all'
};

function generateSection(name, issues) {
  if (!issues.length) return '';
  let section = `${name ? `### ${name}\n` : ''}`;

  issues.forEach(issue => {
    section += `${issue}\n`;
  });

  return `${section}\n`;
}

function processLabels(labels) {
  if (labels.includes('**platform: iOS**')) {
    return '[iOS] '
  } else if (labels.includes('**platform: Android**')) {
    return '[Android] '
  }

  return '';
}