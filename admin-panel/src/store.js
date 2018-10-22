import {createStore, combineReducers, compose} from 'redux';
import firebase from 'firebase';
import 'firebase/firestore';
import {reactReduxFirebase, firebaseReducer} from 'react-redux-firebase';
import {reduxFirestore, firestoreReducer} from 'redux-firestore';
//import * as admin from 'firebase-admin';
//Reducers
//@todo

const firebaseConfig = {
    apiKey: "AIzaSyBh0db9icb1CGxCQa9FEKRiRtSwT0T_53A",
    authDomain: "uber-clone-project-484d8.firebaseapp.com",
    databaseURL: "https://uber-clone-project-484d8.firebaseio.com",
    projectId: "uber-clone-project-484d8",
    storageBucket: "uber-clone-project-484d8.appspot.com",
    messagingSenderId: "146078760213"
};

// react-redux-firebase config
const rrfConfig = {
    userProfile: 'users',
    useFirestoreForProfile: true // Firestore for Profile instead of Realtime DB
  };

  //init firebase instance
  firebase.initializeApp(firebaseConfig);

  //init firestore
firebase.firestore();

 firebase.firestore().settings({
    timestampsInSnapshots: true
 });

  //Add reactReduxFirebase enhancer when making store creator
  const createStoreWithFirebase = compose(
    reactReduxFirebase(firebase, rrfConfig), // firebase instance as first argument
    reduxFirestore(firebase) 
  )(createStore);

  const rootReducer = combineReducers({
      firebase: firebaseReducer,
      firestore: firestoreReducer
  });

  //Create Initial state
  const initialState = {};

  //Create store
  const store = createStoreWithFirebase(
      rootReducer, 
      initialState, 
      compose(
        reactReduxFirebase(firebase),
        window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
     )
 );

  export default store;
