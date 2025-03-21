import { call, put, takeLatest } from 'redux-saga/effects';
import {
    SIGN_IN,
    SIGN_UP,
    RESET_PASSWORD,
    SIGN_OUT,
    SEND_FEEDBACK,
    GET_UPDATES,
    VOTE_POLL,
    GET_TABLES,
    CREATE_TABLE,
    signUpSuccess,
    signUpError,
    signInSuccess,
    signInError,
    resetPasswordSuccess,
    resetPasswordError,
    signOutSuccess,
    signOutError,
    sendFeedbackSuccess,
    sendFeedbackError,
    getUpdatesSuccess,
    getUpdatesError,
    votePollSuccess,
    votePollError,
    getTablesSuccess,
    getTablesError,
    createTableSuccess,
    createTableError,
} from '../../actions/application';
import { signUpRequest, signInRequest, resetPasswordRequest } from '../../../api/auth';
import { sendFeedbackRequest, getUpdatesRequest, votePollRequest } from '../../../api/general';
import { getTablesRequest, createTableRequest } from '../../../api/table';

const signIn = function* (action) {
    try {
        const data = yield call(signInRequest, action.payload);

        localStorage.setItem('token', data.token);
        localStorage.setItem('expire', data.expire);

        yield put(signInSuccess(data));
    } catch (error) {
        localStorage.removeItem('token');
        localStorage.removeItem('expire');

        yield put(signInError(error));
    }
};

const signUp = function* (action) {
    try {
        const data = yield call(signUpRequest, action.payload);
        yield put(signUpSuccess(data));
    } catch (error) {
        yield put(signUpError(error));
    }
};

const signOut = function* (action) {
    try {
        localStorage.removeItem('token');
        localStorage.removeItem('expire');

        yield put(signOutSuccess());
    } catch (error) {
        yield put(signOutError(error));
    }
};

const resetPassword = function* (action) {
    try {
        const data = yield call(resetPasswordRequest, action.payload);
        yield put(resetPasswordSuccess(data));
    } catch (error) {
        yield put(resetPasswordError(error));
    }
};

const sendFeedback = function* (action) {
    try {
        const data = yield call(sendFeedbackRequest, action.payload);
        yield put(sendFeedbackSuccess(data));
    } catch (error) {
        yield put(sendFeedbackError(error));
    }
};

const getUpdates = function* (action) {
    try {
        const data = yield call(getUpdatesRequest, action.payload);
        yield put(getUpdatesSuccess(data));
    } catch (error) {
        yield put(getUpdatesError(error));
    }
};

const votePoll = function* (action) {
    try {
        const data = yield call(votePollRequest, action.payload);
        yield put(votePollSuccess(data));
    } catch (error) {
        yield put(votePollError(error));
    }
};

const getTables = function* (action) {
    try {
        const data = yield call(getTablesRequest, action.payload);
        yield put(getTablesSuccess(data));
    } catch (error) {
        yield put(getTablesError(error));
    }
};

const createTable = function* (action) {
    try {
        const data = yield call(createTableRequest, action.payload);
        yield put(createTableSuccess(data));
    } catch (error) {
        yield put(createTableError(error));
    }
};

const application = function* () {
    yield takeLatest(SIGN_IN.REQUEST, signIn);
    yield takeLatest(SIGN_UP.REQUEST, signUp);
    yield takeLatest(SIGN_OUT.REQUEST, signOut);
    yield takeLatest(RESET_PASSWORD.REQUEST, resetPassword);
    yield takeLatest(SEND_FEEDBACK.REQUEST, sendFeedback);
    yield takeLatest(GET_UPDATES.REQUEST, getUpdates);
    yield takeLatest(VOTE_POLL.REQUEST, votePoll);
    yield takeLatest(GET_TABLES.REQUEST, getTables);
    yield takeLatest(CREATE_TABLE.REQUEST, createTable);
};

export default application;
