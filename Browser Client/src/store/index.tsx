import { applyMiddleware, createStore, combineReducers, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import reducers from './reducers';
import { ApplicationState } from './reducers/application/types';
import { TableState } from './reducers/table/types';
import sagas from './sagas';

const persistConfig = {
    key: 'daily-harvest-web-persist-store',
    storage,
    whitelist: [],
};

const combinedReducer = combineReducers(reducers);
const sagaMiddleware = createSagaMiddleware();
const composeEnhancers = (window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__'] as typeof compose) || compose;
const enhancer = composeEnhancers(applyMiddleware(sagaMiddleware));
const persistedReducer = persistReducer(persistConfig, combinedReducer);

export const store = createStore(persistedReducer, enhancer);
export const persistor = persistStore(store);

sagaMiddleware.run(sagas);

export type RootState = {
	application: ApplicationState,
	table: TableState
};
