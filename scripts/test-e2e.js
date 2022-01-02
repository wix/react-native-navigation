const includes = require('lodash/includes');
const exec = require('shell-utils').exec;

const android = includes(process.argv, '--android');
const release = includes(process.argv, '--release');
const skipBuild = includes(process.argv, '--skipBuild');
const headless = includes(process.argv, '--headless');
const multi = includes(process.argv, '--multi');
const verbose = includes(process.argv, '--verbose');
const CI = includes(process.argv, '--ci');

run();

function run() {
  const prefix = android ? (CI ? `android.genycloud` : `android.emu`) : `ios.sim`;
  const suffix = release ? `release` : `debug`;
  const configuration = `${prefix}.${suffix}`;
  const headless$ = android ? (headless ? `--headless` : ``) : ``;
  const workers = multi ? 3 : 1;
  const loglevel = verbose ? '--loglevel verbose' : '';

  if (!android) {
    exec.execSync('npm run build');
    exec.execSync('npm run pod-install');
  }
  if (!skipBuild) {
    exec.execSync(`detox build --configuration ${configuration}`);
  }
  exec.execSync(
    `detox test --configuration ${configuration} ${headless$} -w ${workers} ${loglevel}`
    // "Buttons.test.js" --loglevel trace`
  );
}
