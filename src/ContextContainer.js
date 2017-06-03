/**
 * Created by luolishu on 12/13/16.
 */
import React, {Component, PropTypes,} from 'react';
import {
    StyleSheet,
    View
} from 'react-native';
import ActionSheet from '@expo/react-native-action-sheet';
import ContextMenu from './ContextMenu';
export default class ContextContainer extends Component {
    static childContextTypes = {
        actionSheet: PropTypes.func,
        contextContainer: PropTypes.func,
    };

    constructor(props) {
        super(props);
        this._actionSheetRef = null;
        this._contextMenu = null;
        this.getChildContext = this.getChildContext.bind(this)
    }

    getChildContext() {
        return {
            actionSheet: () => this._actionSheetRef,
            contextContainer: () => this,
        };
    }
    showContextMenu(){
        this._contextMenu.show();
    }
    toggleContextMenu(){
        this._contextMenu.toggle();
    }


    render() {
        let contextMenuDisplay = true;
        let contextMenuView;
        if (contextMenuDisplay) {
            contextMenuView = <ContextMenu ref={component => this._contextMenu = component}/>;
        }

        return (
            <View style={[styles.container, this.props.style]}>
                <ActionSheet style={styles.container}
                             ref={component => this._actionSheetRef = component}>
                    {this.props.children}

                </ActionSheet>
                {contextMenuView}
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
})