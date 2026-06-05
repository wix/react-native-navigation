declare module '@nativescript/react-native' {
  import * as React from 'react';
  import { ViewProps } from 'react-native';

  export type UIKitViewContext<Props extends object> = {
    props: Readonly<Props>;
  };

  export type UIKitLayoutOptions = {
    sizing?: 'fill' | 'intrinsic' | 'sizeThatFits' | 'autoLayout';
    defaultSize?: {width?: number; height?: number};
    minSize?: {width?: number; height?: number};
    maxSize?: {width?: number; height?: number};
  };

  export type UIViewControllerDefinition<Props extends object, Controller = unknown> = {
    debugName?: string;
    layout?: UIKitLayoutOptions;
    createController: (
      ctx: UIKitViewContext<Props & ViewProps> & Readonly<Props & ViewProps>
    ) => Controller;
    childrenView?: (controller: Controller) => unknown;
    update?: (
      controller: Controller,
      props: Readonly<Props & ViewProps>,
      previousProps?: Readonly<Props & ViewProps>,
      ctx?: UIKitViewContext<Props & ViewProps>
    ) => void;
    mounted?: (
      controller: Controller,
      props: Readonly<Props & ViewProps>,
      ctx?: UIKitViewContext<Props & ViewProps>
    ) => void;
    dispose?: (
      controller: Controller,
      props: Readonly<Props & ViewProps>,
      ctx?: UIKitViewContext<Props & ViewProps>
    ) => void;
  };

  export function defineUIViewController<Props extends object, Controller = unknown>(
    definition: UIViewControllerDefinition<Props, Controller>
  ): React.ComponentType<
    React.PropsWithChildren<
      Props & ViewProps & {attachController?: boolean; attachNativeView?: boolean}
    >
  >;

  export function getClass<T = unknown>(name: string): T | null;

  const NativeScript: {
    defineUIViewController: typeof defineUIViewController;
    getClass: typeof getClass;
  };

  export default NativeScript;
}
