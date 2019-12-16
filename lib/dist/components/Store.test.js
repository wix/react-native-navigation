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
        expect(instance.setProps).toHaveBeenCalledWith(props);
    });
    it('not throw exeption when set props by id component not found', () => {
        expect(() => uut.updateProps('component1', { foo: 'bar' })).not.toThrow();
    });
});
