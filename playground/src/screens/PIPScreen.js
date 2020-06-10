const React = require('react');
const Button = require('../components/Button')
const Root = require('../components/Root');
const Navigation = require('./../services/Navigation');

class PIPScreen extends React.Component {

    render() {
        return (
            <Root style={styles.root}>
                <Button label='Switched To PIP' onPress={this.showPIP}/>
            </Root>
        );
    }

    showPIP = () =>  Navigation.switchToPIP(this.props.componentId);
}

module.exports = PIPScreen;

const styles = {
    root: {
        justifyContent: 'center',
        alignItems: 'center'
    },
    text: {
        fontSize: 14,
        textAlign: 'center',
        marginBottom: 8
    }
};
