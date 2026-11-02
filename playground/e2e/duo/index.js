// Folds and unfolds the iPhone Duo simulator from Detox tests. See hinge.m.
const { execFile, execFileSync } = require('child_process');
const { promisify } = require('util');
const fs = require('fs');
const path = require('path');

const source = path.join(__dirname, 'hinge.m');
const entitlements = path.join(__dirname, 'hinge.entitlements');
const binary = path.join(__dirname, 'build', 'hinge');

function build() {
  if (fs.existsSync(binary) && fs.statSync(binary).mtimeMs > fs.statSync(source).mtimeMs) {
    return binary;
  }
  fs.mkdirSync(path.dirname(binary), { recursive: true });
  const sdk = execFileSync('xcrun', ['--sdk', 'iphonesimulator', '--show-sdk-path']).toString().trim();
  // The simulator reads entitlements from the __entitlements section, not the code signature.
  execFileSync('xcrun', [
    'clang', '-fobjc-arc', '-isysroot', sdk, '-target', 'arm64-apple-ios15.1-simulator',
    '-framework', 'Foundation', source,
    `-Wl,-sectcreate,__TEXT,__entitlements,${entitlements}`, '-o', binary,
  ]);
  execFileSync('codesign', ['-fs', '-', binary]);
  return binary;
}

/**
 * Moves the hinge to `position` ('closed', 'book', 'open' or 0..1) without the animated sweep and
 * waits for the display swap to finish (about 5-10 seconds). 'closed' shows the app on the outer
 * display, 'open' on the inner one.
 */
async function setPose(position, deviceId = device.id) {
  await promisify(execFile)('xcrun', [
    'simctl', 'spawn', deviceId, build(), 'swap', `-position=${position}`, '-duration=0', '-wait',
  ]);
}

function isDuo(deviceId = device.id) {
  const { devices } = JSON.parse(execFileSync('xcrun', ['simctl', 'list', 'devices', '-j']).toString());
  return Object.values(devices)
    .flat()
    .some((d) => d.udid === deviceId && d.deviceTypeIdentifier.endsWith('iPhone-Duo'));
}

module.exports = { setPose, isDuo, build };
