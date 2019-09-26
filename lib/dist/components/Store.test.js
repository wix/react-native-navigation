"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const React = require("react");
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
        uut.setPropsForId('component1', { a: 1, b: 2 });
        expect(uut.getPropsForId('component1')).toEqual({ a: 1, b: 2 });
    });
    it('defensive for invalid Id and props', () => {
        uut.setPropsForId('component1', undefined);
        expect(uut.getPropsForId('component1')).toEqual({});
    });
    it('holds original components classes by componentName', () => {
        const MyWrappedComponent = () => class MyComponent extends React.Component {
        };
        uut.setComponentClassForName('example.mycomponent', MyWrappedComponent);
        expect(uut.getComponentClassForName('example.mycomponent')).toEqual(MyWrappedComponent);
    });
    it('clean by component id', () => {
        uut.setPropsForId('refUniqueId', { foo: 'bar' });
        uut.cleanId('refUniqueId');
        expect(uut.getPropsForId('refUniqueId')).toEqual({});
    });
});
