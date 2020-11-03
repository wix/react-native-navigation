module.exports = {
  template: {
    commit: ({message, url, author, name}) =>
      `- [${message}](${url}) - ${author ? `@${author}` : name}`,
    issue: '- {{name}} [{{text}}]({{url}}) by [{{user_login}}](https://github.com/{{user_login}})",',
    label: '[**{{label}}**]',
    noLabel: 'closed',
    group: '\n#### {{heading}}\n',
    changelogTitle: '# Changelog\n\n',
    release: '## {{release}} ({{date}})\n{{body}}',
    releaseSeparator: '\n---\n\n',
  },
  groupBy: {
    'Enhancements:': ['type: accepted/enhancement', 'internal'],
    'Bug fixes:': ['type: accepted/bug'],
    Features: ['feature'],
  },
  ignoreIssuesWith: ['skip-changelog'],
  ignoreTagsWith: ['snapshot', 'v1', 'v2', '0\..\..', '1\..\..', '2\..\..', '3\..\..', '4\..\..', '5\..\..', '6\..\..'],
  dataSource: 'prs',
  changelogFilename: 'CHANGELOG.gren.md',
  override: true,
  generate: true,
  tags: 'all'
};
