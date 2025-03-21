import { all, fork } from 'redux-saga/effects';

import application from './application';
import table from './table';

export default function* root() {
    yield all([fork(application)]);
    yield all([fork(table)]);
}
