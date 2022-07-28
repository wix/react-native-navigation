const exec = require('shell-utils').exec;

function run() {
  exec.execSync(`jest autolink --watch`);
}

run();
