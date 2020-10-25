const exec = require('shell-utils').exec;

const files = process.argv.slice(2).join(' ');
const result = exec.execSyncRead(`./node_modules/.bin/git-clang-format --diff -- ${files}`).trim();

if (
  result !== 'no modified files to format' &&
  result !== 'clang-format did not modify any files'
) {
  throw result;
}
