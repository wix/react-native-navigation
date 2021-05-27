import { connect as remxConnect } from 'remx';

export function connect<T>(component: T): T {
  // @ts-ignore
  return remxConnect()(component);
}
