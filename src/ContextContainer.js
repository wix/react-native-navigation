/**
 * Created by luolishu on 12/13/16.
 */
import React, {Component, PropTypes,} from 'react';
import {
    StyleSheet,
} from 'react-native';
import ActionSheet from '@exponent/react-native-action-sheet';
export default class ContextContainer extends Component {
    static childContextTypes = {
        actionSheet: PropTypes.func,
    };

    constructor(props) {
        super(props);
        this._actionSheetRef = null;
    }

    getChildContext() {
        return {
            actionSheet: () => this._actionSheetRef,
        };
    }

    render() {
        return (
            <ActionSheet style={[styles.container, this.props.style]}
                         ref={component => this._actionSheetRef = component}>
                {this.props.children}
            </ActionSheet>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
})