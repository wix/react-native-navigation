import React, {Component} from 'react';
import {
    StyleSheet,
    View,
    Image,
    Text,
    Modal,
    Dimensions,
    TouchableOpacity,
    TouchableHighlight,
    RecyclerViewBackedScrollView,
    SwipeableListView,
    StatusBar,
    PixelRatio
} from 'react-native';
import Popover from './popover';

export default class ContextMenu extends Component {
    constructor(props) {
        super(props)
        this.state = {
            showMenu: false
        }
        this.show = this.show.bind(this);
        this.hide = this.hide.bind(this);
    }

    show() {
        this.setState({
            showMenu: true
        })
    }

    toggle() {
        this.setState({
            showMenu: !this.state.showMenu
        })
    }

    hide() {
        this.setState({
            showMenu: false
        })
    }


    render() {
        return (
            <Popover
                isVisible={!!this.state.showMenu}
                backgroundStyle={{backgroundColor: 'transparent'}}
                arrowLeft={175}
                arrowSize={{width: 16, height: 9}}
                fromRect={{x: 160, y: 0, width: 447, height: 0}}
                onClose={this.hide}>
                <View style={styles.popoverContent}>
                    <TouchableOpacity style={styles.menuItem} onPress={()=> {
                        this.hide();
                    }}>
                        <View style={styles.menuIconContainer}>
                            {/*<Image source={AppIcons.discover.menu_reviews_icon} style={styles.menuIcon}/>*/}
                        </View>
                        <Text ellipsizeMode="tail" numberOfLines={1} style={styles.menuItemText}>Reviews</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.menuItem} onPress={()=> {
                        this.hide();
                    }}>
                        <View style={styles.menuIconContainer}>
                            {/*<Image source={AppIcons.discover.menu_favorites_icon} style={styles.menuIcon}/>*/}
                        </View>

                        <Text ellipsizeMode="tail" numberOfLines={1} style={styles.menuItemText}>Favorites</Text>

                    </TouchableOpacity>
                    <TouchableOpacity style={styles.menuItem} onPress={()=> {
                        this.hide(); 
                    }}>
                        <View style={styles.menuIconContainer}>
                            {/*<Image source={AppIcons.discover.menu_add_icon} style={styles.menuIcon}/>*/}
                        </View>
                        <Text ellipsizeMode="tail" numberOfLines={1} style={styles.menuItemText}>Add Bussiness</Text>

                    </TouchableOpacity>
                    <TouchableOpacity style={styles.menuItem} onPress={()=> {
                        this.hide();
                    }}>
                        <View style={styles.menuIconContainer}>
                            {/*<Image source={AppIcons.discover.menu_help_icon} style={styles.menuIcon}/>*/}
                        </View>
                        <Text ellipsizeMode="tail" numberOfLines={1} style={styles.menuItemText}>Help         </Text>

                    </TouchableOpacity>
                </View>
            </Popover>
        )
    }


}
var styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'white',

        position:'absolute',

    },
    modalMenu: {
        flex: 1,

    },
    menuIconContainer: {
        width: 50,
        flexDirection: 'row',
        alignItems: 'center',
    },
    menuIcon: {
        marginLeft:20,
    },
    menuItem: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderBottomColor: "#2B8CDF",
        height: 50,
        alignItems: 'center',
        justifyContent:'flex-end'

    },
    menuItemText: {
        fontSize: 16,
        color: 'white',
        paddingLeft: 10,
        width:150,
        textAlign:'left',

    },
    popoverContent: {
        width: 200,
        height: 200,
        backgroundColor: "#3EA1F5",
        justifyContent: 'center',
        alignItems: 'center',
    },
    popoverText: {
        color: '#ccc',
    },

});
