import { DeferredLinkQueue } from './DeferredLinkQueue';

describe('DeferredLinkQueue', () => {
  let uut: DeferredLinkQueue;

  beforeEach(() => {
    uut = new DeferredLinkQueue();
  });

  it('starts in ready state by default', () => {
    expect(uut.isReady()).toBe(true);
  });

  it('process returns true when ready', () => {
    expect(uut.process('myapp://home')).toBe(true);
  });

  it('process returns false and queues when not ready', () => {
    uut.setReady(false);
    expect(uut.process('myapp://home')).toBe(false);
    expect(uut.pending()).toEqual(['myapp://home']);
  });

  it('queues multiple links in order', () => {
    uut.setReady(false);
    uut.process('myapp://a');
    uut.process('myapp://b');
    uut.process('myapp://c');
    expect(uut.pending()).toEqual(['myapp://a', 'myapp://b', 'myapp://c']);
  });

  it('flushes queued links through callback when setReady(true)', () => {
    const flushed: string[] = [];
    uut.setFlushCallback((url) => flushed.push(url));
    uut.setReady(false);

    uut.process('myapp://a');
    uut.process('myapp://b');
    expect(flushed).toEqual([]);

    uut.setReady(true);
    expect(flushed).toEqual(['myapp://a', 'myapp://b']);
    expect(uut.pending()).toEqual([]);
  });

  it('retains items when no callback is set', () => {
    uut.setReady(false);
    uut.process('myapp://a');
    uut.setReady(true);
    expect(uut.pending()).toEqual(['myapp://a']);
  });

  it('clear empties the queue', () => {
    uut.setReady(false);
    uut.process('myapp://a');
    uut.process('myapp://b');
    uut.clear();
    expect(uut.pending()).toEqual([]);
  });

  it('pending returns a copy, not internal reference', () => {
    uut.setReady(false);
    uut.process('myapp://a');
    const pending = uut.pending();
    pending.push('myapp://injected');
    expect(uut.pending()).toEqual(['myapp://a']);
  });
});
