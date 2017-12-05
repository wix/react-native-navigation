const exec = require('shell-utils').exec;

exec.execSync(`detox build && detox test`);
