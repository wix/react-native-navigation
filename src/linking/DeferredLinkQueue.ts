export class DeferredLinkQueue {
  private queue: string[] = [];
  private ready = true;
  private flushCallback: ((url: string) => void) | null = null;

  public setFlushCallback(callback: (url: string) => void): void {
    this.flushCallback = callback;
  }

  public setReady(ready: boolean): void {
    this.ready = ready;
    if (ready) {
      this.flush();
    }
  }

  public isReady(): boolean {
    return this.ready;
  }

  /**
   * Try to process a URL. Returns true if processed immediately,
   * false if queued for later.
   */
  public process(url: string): boolean {
    if (this.ready) {
      return true;
    }
    this.queue.push(url);
    return false;
  }

  public flush(): void {
    if (!this.flushCallback) {
      return;
    }
    const pending = [...this.queue];
    this.queue = [];
    for (const url of pending) {
      this.flushCallback(url);
    }
  }

  public clear(): void {
    this.queue = [];
  }

  public pending(): string[] {
    return [...this.queue];
  }
}
