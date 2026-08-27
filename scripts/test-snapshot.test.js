const { execFileSync } = require('child_process');
const { run } = require('./test-snapshot');

jest.mock('child_process', () => ({ execFileSync: jest.fn() }));

it('uses supported build commands and never publishes recorded snapshots', () => {
  const previous = {
    RECORD: process.env.RECORD,
    IOS_TEST_DEVICE: process.env.IOS_TEST_DEVICE,
    IOS_TEST_OS: process.env.IOS_TEST_OS,
  };
  const log = jest.spyOn(console, 'log').mockImplementation(() => {});
  try {
    process.env.RECORD = 'true';
    process.env.IOS_TEST_DEVICE = 'Review Simulator';
    process.env.IOS_TEST_OS = '26.5';
    run();
    const calls = execFileSync.mock.calls;
    expect(calls.map(([command]) => command)).toEqual(['yarn', 'yarn', 'xcodebuild', 'xcodebuild']);
    expect(calls[0][1]).toEqual(['prepare']);
    expect(calls[2][1]).toContain('SnapshotRecordTests');
    expect(calls[3][1]).toContain('platform=iOS Simulator,name=Review Simulator,OS=26.5');
  } finally {
    for (const [key, value] of Object.entries(previous)) {
      if (value === undefined) delete process.env[key];
      else process.env[key] = value;
    }
    log.mockRestore();
  }
});
