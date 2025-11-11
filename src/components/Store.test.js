"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const React = tslib_1.__importStar(require("react"));
const Store_1 = require("./Store");
describe('Store', () => {
    let uut;
    beforeEach(() => {
        uut = new Store_1.Store();
    });
    it('initial state', () => {
        expect(uut.getPropsForId('component1')).toEqual({});
    });
    it('holds props by id', () => {
        uut.updateProps('component1', { a: 1, b: 2 });
        expect(uut.getPropsForId('component1')).toEqual({ a: 1, b: 2 });
    });
    it('defensive for invalid Id and props', () => {
        uut.updateProps('component1', undefined);
        expect(uut.getPropsForId('component1')).toEqual({});
    });
    it('update props with callback', (done) => {
        const instance = {
            setProps: jest.fn((_props, callback) => {
                callback();
            }),
        };
        uut.setComponentInstance('component1', instance);
        function callback() {
            try {
                expect(true).toBe(true);
                done();
            }
            catch (error) {
                done(error);
            }
        }
        uut.updateProps('component1', { someProp: 'someValue' }, callback);
    });
    it('holds original components classes by componentName', () => {
        const MyWrappedComponent = () => class MyComponent extends React.Component {
        };
        uut.setComponentClassForName('example.mycomponent', MyWrappedComponent);
        expect(uut.getComponentClassForName('example.mycomponent')).toEqual(MyWrappedComponent);
    });
    it('clear props by component id when clear component', () => {
        uut.updateProps('refUniqueId', { foo: 'bar' });
        uut.clearComponent('refUniqueId');
        expect(uut.getPropsForId('refUniqueId')).toEqual({});
    });
    it('clear instance by component id when clear component', () => {
        uut.setComponentInstance('refUniqueId', {});
        uut.clearComponent('refUniqueId');
        expect(uut.getComponentInstance('refUniqueId')).toEqual(undefined);
    });
    it('holds component instance by id', () => {
        uut.setComponentInstance('component1', {});
        expect(uut.getComponentInstance('component1')).toEqual({});
    });
    it('calls component setProps when set props by id', () => {
        const instance = { setProps: jest.fn() };
        const props = { foo: 'bar' };
        uut.setComponentInstance('component1', instance);
        uut.updateProps('component1', props);
        expect(instance.setProps).toHaveBeenCalledWith(props, undefined);
    });
    it('not throw exception when set props by id component not found', () => {
        expect(() => uut.updateProps('component1', { foo: 'bar' })).not.toThrow();
    });
    it('tries to register components lazily when given a lazy registrator', () => {
        const MyLazyComponent = () => class MyComponent extends React.Component {
        };
        const MyEagerComponent = () => class MyComponent extends React.Component {
        };
        uut.setComponentClassForName('eager', MyEagerComponent);
        const lazyRegistrator = jest.fn((name) => {
            if (name === 'lazy') {
                uut.setComponentClassForName(name, MyLazyComponent);
            }
        });
        uut.setLazyComponentRegistrator(lazyRegistrator);
        expect(uut.getComponentClassForName('eager')).toEqual(MyEagerComponent);
        expect(uut.getComponentClassForName('lazy')).toEqual(MyLazyComponent);
        expect(uut.getComponentClassForName('lazy')).toEqual(MyLazyComponent);
        expect(lazyRegistrator).toHaveBeenCalledTimes(1);
    });
    it('keeps the old props when updateProps is called', () => {
        uut.updateProps('component1', { foo: 'foo', bar: 'bar' });
        uut.updateProps('component1', { foo: 'foo2' });
        expect(uut.getPropsForId('component1')).toEqual({ foo: 'foo2', bar: 'bar' });
    });
    it('clearing component props should not clear pending props', () => {
        uut.updateProps('component1', { foo: 'foo2' });
        uut.setPendingProps('component1', { foo: 'foo', bar: 'bar' });
        uut.clearComponent('component1');
        expect(uut.getPropsForId('component1')).toEqual({ foo: 'foo', bar: 'bar' });
    });
    it('should not clear pending props if not consumed', () => {
        uut.setPendingProps('component1', { foo: 'foo', bar: 'bar' });
        uut.getPropsForId('component1');
        uut.clearComponent('component1');
        expect(uut.getPropsForId('component1')).toEqual({ foo: 'foo', bar: 'bar' });
    });
    it('should clear pending props if consumed', () => {
        uut.setPendingProps('component1', { foo: 'foo', bar: 'bar' });
        uut.getPropsForId('component1');
        uut.consumePendingProps('component1');
        uut.clearComponent('component1');
        expect(uut.getPropsForId('component1')).toEqual({});
    });
});
