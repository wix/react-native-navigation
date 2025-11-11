"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.events = void 0;
exports.events = {
    navigationButtonPressed: [(_event) => { }],
    componentWillAppear: [(_event) => { }],
    componentDidAppear: [(_event) => { }],
    componentDidDisappear: [(_event) => { }],
    modalDismissed: [(_event) => { }],
    bottomTabPressed: [(_event) => { }],
    commandCompleted: [(_event) => { }],
    screenPopped: [(_event) => { }],
    invokeComponentWillAppear: (event) => {
        exports.events.componentWillAppear &&
            exports.events.componentWillAppear.forEach((listener) => {
                listener(event);
            });
    },
    invokeComponentDidAppear: (event) => {
        exports.events.componentDidAppear &&
            exports.events.componentDidAppear.forEach((listener) => {
                listener(event);
            });
    },
    invokeComponentDidDisappear: (event) => {
        exports.events.componentDidDisappear &&
            exports.events.componentDidDisappear.forEach((listener) => {
                listener(event);
            });
    },
    invokeModalDismissed: (event) => {
        exports.events.modalDismissed &&
            exports.events.modalDismissed.forEach((listener) => {
                listener(event);
            });
    },
    invokeNavigationButtonPressed: (event) => {
        exports.events.navigationButtonPressed &&
            exports.events.navigationButtonPressed.forEach((listener) => {
                listener(event);
            });
    },
    invokeBottomTabPressed: (event) => {
        exports.events.bottomTabPressed &&
            exports.events.bottomTabPressed?.forEach((listener) => {
                listener(event);
            });
    },
    invokeCommandCompleted: (event) => {
        exports.events.commandCompleted &&
            exports.events.commandCompleted.forEach((listener) => {
                listener(event);
            });
    },
    invokeScreenPopped: (event) => {
        exports.events.screenPopped &&
            exports.events.screenPopped.forEach((listener) => {
                listener(event);
            });
    },
};
