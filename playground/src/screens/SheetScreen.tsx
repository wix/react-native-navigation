import { StyleSheet, TouchableOpacity, View, Text, ScrollView } from 'react-native';
import { NavigationLayoutElements } from 'react-native-navigation';
import { useState } from 'react';
import React from 'react';

export const TestScreen = () => {
  const [list, setList] = useState([{ title: 1 }]);

  return (
    <View style={styles.container}>
      <View style={styles.header} nativeID={NavigationLayoutElements.Header}>
        <TouchableOpacity style={styles.button} onPress={() => {}}>
          <Text>FullScreen sheet</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={() => {}}>
          <Text>Open Modal</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => setList([{ title: list.length + 1 }, ...list])}
        >
          <Text>Add Item</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => setList([...list.slice(0, 0), ...list.slice(0 + 1)])}>
          <Text>Remove Item</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={() => {}}>
          <Text>Go back</Text>
        </TouchableOpacity>
      </View>

      <ScrollView nativeID={NavigationLayoutElements.Content} nestedScrollEnabled>
        {list.map((i, key) => (
          <View style={styles.item} key={key}>
            <Text style={styles.itemText}>{i.title}</Text>
          </View>
        ))}
      </ScrollView>

      <View nativeID={NavigationLayoutElements.Footer} style={styles.footer}>
        <Text style={styles.footerText}>FOOTER</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  button: {},
  header: {
    height: 64,
    backgroundColor: 'red',
    flexDirection: 'row',
  },
  item: {
    backgroundColor: 'blue',
    width: 200,
    height: 200,
    borderBottomWidth: 2,
    borderBottomColor: 'red',
  },
  itemText: {
    color: '#FFF',
    fontSize: 18,
    textAlign: 'center',
  },
  footer: {
    height: 64,
    backgroundColor: 'green',
    borderBottomWidth: 1,
    borderBottomColor: 'red',
  },
  footerText: {
    color: '#FFF',
    fontSize: 18,
    textAlign: 'center',
  },
});
