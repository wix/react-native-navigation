const exec = require('shell-utils').exec;
const includes = require('lodash/includes');
const split = require('lodash/split');
const filter = require('lodash/filter');

const fix = includes(process.argv, '--fix') ? '--fix' : '';

/**
 * Temporarily disabling lint checking until the lint/prettier settings are settled.
 *
 * @todo Uncomment the following and run auto lint fix on these.
 * @author Jin Shin (22/06/2020)
 */
// const dirs = ['lib/src', 'integration', 'scripts', 'playground/src'].join(' ');
const dirs = [];

run();

function run() {
  exec.execSync(`eslint ${dirs} ${fix} --ext .js,.jsx,.ts,.tsx --format "codeframe"`);
  assertAllTsFilesInSrc();
  exec.execSync(`jest --coverage --config ./jest.config.js`);
}

function assertAllTsFilesInSrc() {
  const allFiles = exec.execSyncRead('find ./lib/src -type f');
  const lines = split(allFiles, '\n');
  const offenders = filter(lines, (f) => !f.endsWith('.ts') && !f.endsWith('.tsx'));
  if (offenders.length) {
    throw new Error(`\n\nOnly ts/tsx files are allowed:\n${offenders.join('\n')}\n\n\n`);
  }
}
