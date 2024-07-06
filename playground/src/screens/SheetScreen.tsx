import { NavigationLayoutElements, NavigationProps } from 'react-native-navigation';
import { useLayoutEffect, useRef, useState } from 'react';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import React from 'react';
import {
  StyleSheet,
  TouchableOpacity,
  View,
  Text,
  ScrollView,
  findNodeHandle,
  Platform,
} from 'react-native';

function useSetupSheetContent<
  THeaderRef extends React.Component<any, any>,
  TContentRef extends React.Component<any, any>,
  TFooterRef extends React.Component<any, any>
>(
  componentId: string,
  headerRef?: React.RefObject<THeaderRef>,
  contentRef?: React.RefObject<TContentRef>,
  footerRef?: React.RefObject<TFooterRef>
) {
  useLayoutEffect(() => {
    const headerNode = headerRef ? findNodeHandle(headerRef.current) : null;
    const contentNode = contentRef ? findNodeHandle(contentRef.current) : null;
    const footerNode = footerRef ? findNodeHandle(footerRef.current) : null;

    Navigation.setupSheetContentNodes(componentId, headerNode, contentNode, footerNode);
  }, [componentId, contentRef, footerRef, headerRef]);
}

const SheetScreen = (props: NavigationProps) => {
  const [list, setList] = useState([{ title: 1 }]);
  const contentRef = useRef<ScrollView>(null);
  const headerRef = useRef<View>(null);
  const footerRef = useRef<View>(null);

  useSetupSheetContent(props.componentId, headerRef, contentRef, footerRef);

  return (
    <View style={styles.container}>
      <View ref={headerRef} style={styles.header} nativeID={NavigationLayoutElements.Header}>
        {Platform.OS === 'android' && (
          <TouchableOpacity
            style={styles.button}
            onPress={() => {
              Navigation.showSheet(Screens.Sheet, { layout: { sheetFullScreen: true } });
            }}
          >
            <Text style={styles.buttonText}>FullScreen</Text>
          </TouchableOpacity>
        )}
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            Navigation.showModal(Screens.Modal);
          }}
        >
          <Text style={styles.buttonText}>Open Modal</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => setList([{ title: list.length + 1 }, ...list])}
        >
          <Text style={styles.buttonText}>Add Item</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => setList([...list.slice(0, 0), ...list.slice(0 + 1)])}>
          <Text style={styles.buttonText}>Remove Item</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            Navigation.dismissSheet(props.componentId);
          }}
        >
          <Text style={styles.buttonText}>dismissModal</Text>
        </TouchableOpacity>
      </View>

      <ScrollView ref={contentRef} nativeID={NavigationLayoutElements.Content} nestedScrollEnabled>
        {list.map((i, key) => (
          <View style={styles.item} key={key}>
            <Text style={styles.itemText}>{i.title}</Text>
          </View>
        ))}
      </ScrollView>

      <View ref={footerRef} nativeID={NavigationLayoutElements.Footer} style={styles.footer}>
        <Text style={styles.footerText}>FOOTER</Text>
      </View>
    </View>
  );
};

export default SheetScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  button: {
    paddingHorizontal: 6,
  },
  buttonText: {
    color: '#FFF',
    backgroundColor: 'blue',
  },
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
