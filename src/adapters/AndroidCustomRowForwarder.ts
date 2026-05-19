import { NativeModules, Platform } from 'react-native';

import { Options } from '../interfaces/Options';

/**
 * Walks a Layout / Options tree looking for `bottomTabs.customRow`
 * configuration and forwards it to the Android-only native module
 * `RNNBottomTabsCustomRowModule`.
 *
 * On iOS this is a no-op — the existing native options parser already picks
 * up `bottomTabs.customRow` and applies it to the iOS custom row.
 *
 * The original layout/options object is *never* modified — both pipelines
 * (iOS native parser, Android native parser which currently ignores the
 * field) keep seeing the same data.
 */
export class AndroidCustomRowForwarder {
  forwardFromLayout(layout: any) {
    console.log('[CustomRowFwd] forwardFromLayout enter', this.shouldForward(), !!layout);
    if (!this.shouldForward()) return;
    const config = this.findCustomRowInLayout(layout);
    console.log('[CustomRowFwd] forwardFromLayout config=', JSON.stringify(config));
    if (config) this.send(config);
  }

  forwardFromLayouts(layouts: any[]) {
    console.log('[CustomRowFwd] forwardFromLayouts enter', this.shouldForward(), layouts?.length);
    if (!this.shouldForward()) return;
    for (const layout of layouts) {
      const config = this.findCustomRowInLayout(layout);
      if (config) {
        this.send(config);
        return;
      }
    }
  }

  forwardFromOptions(options: Options | undefined) {
    if (!this.shouldForward() || !options) return;
    const config = this.extractFromOptions(options);
    if (config) this.send(config);
  }

  private shouldForward(): boolean {
    return Platform.OS === 'android';
  }

  private findCustomRowInLayout(layout: any): object | null {
    if (!layout || typeof layout !== 'object') return null;
    // Walk both the raw layout shape (`layout.options`) and the
    // post-`layoutTreeParser.parse` shape (`layout.data.options`).
    const direct =
      this.extractFromOptions(layout.options) ??
      this.extractFromOptions(layout.data?.options);
    if (direct) return direct;
    const children = layout.children;
    if (Array.isArray(children)) {
      for (const child of children) {
        const found = this.findCustomRowInLayout(child);
        if (found) return found;
      }
    }
    return null;
  }

  private extractFromOptions(options: Options | undefined): object | null {
    if (!options || typeof options !== 'object') return null;
    const bottomTabs: any = (options as any).bottomTabs;
    if (!bottomTabs || typeof bottomTabs !== 'object') return null;
    const customRow = bottomTabs.customRow;
    if (!customRow || typeof customRow !== 'object') return null;
    return customRow;
  }

  private send(config: object): void {
    const nativeModule = (NativeModules as any).RNNBottomTabsCustomRowModule;
    if (!nativeModule || typeof nativeModule.configure !== 'function') {
      console.log('[CustomRowFwd] module unavailable, dropping', Object.keys(NativeModules));
      return;
    }
    try {
      console.log('[CustomRowFwd] sending', config);
      nativeModule.configure(config);
    } catch (e) {
      console.log('[CustomRowFwd] send error', String(e));
    }
  }
}
