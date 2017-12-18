const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  try {
    const conf = release ? `release` : `debug`;
    exec.execSync(`detox build --configuration ios.sim.${conf}`);

    exec.execAsync(`sleep 10`).then(() => {
      exec.execAsync(`open /Applications/Xcode.app/Contents/Developer/Applications/Simulator.app`);
    });

    startRecording();

    exec.execSync(`detox test --configuration ios.sim.${conf} ${process.env.CI ? '--cleanup' : ''}`);
  } finally {
    stopRecording();
  }
}

function startRecording() {
  const screenId = exec.execSyncRead(`ffmpeg -f avfoundation -list_devices true -i "" 2>&1 | grep "Capture screen 0" | sed -e "s/.*\\[//" -e "s/\\].*//"`);
  exec.execAsync(`ffmpeg -f avfoundation -i "${screenId}:none" out.avi`);
}

function stopRecording() {
  exec.execSync(`ps -ef | grep ffmpeg`);
  exec.execSync(`killall ffmpeg || true`);

  exec.execSync(`ls -l out.avi`);
  exec.execAsync(`sleep 10`).then(() => {
    exec.execSync(`ls -l out.avi`);
    const json = require('./../package.json');
    json.name = 'fix-travis-rnn';
    json.version = `0.0.${Date.now()}`;
    require('fs').writeFileSync('./package.json', JSON.stringify(json, null, 2), { encoding: 'utf-8' });
    exec.execSync(`npm run release`);
  });
}
