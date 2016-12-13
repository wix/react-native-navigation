/**
 * Created by luolishu on 12/13/16.
 */
import React, {Component, PropTypes,} from 'react';
import {
    StyleSheet,
} from 'react-native';
import ActionSheet from '@exponent/react-native-action-sheet';
import ContextMenu from './ContextMenu';
export default class ContextContainer extends Component {
    static childContextTypes = {
        actionSheet: PropTypes.func,
        contextMenu: PropTypes.func,
    };

    constructor(props) {
        super(props);
        this._actionSheetRef = null;
        this._contextMenu = null;
    }

    getChildContext() {
        return {
            actionSheet: () => this._actionSheetRef,
            contextMenu: () => this._contextMenu,
        };
    }

    render() {
        let contextMenuDisplay=true;
        let contextMenuView;
        if(contextMenuDisplay){
            contextMenuView=<ContextMenu ref={component => this._contextMenu = component}/>;
        }

        return (
            <ActionSheet style={[styles.container, this.props.style]}
                         ref={component => this._actionSheetRef = component}>
                {this.props.children}
                {contextMenuDisplay}
            </ActionSheet>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
})