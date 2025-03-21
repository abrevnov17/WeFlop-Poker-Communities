type Action = {
    REQUEST: string;
    SUCCESS: string;
    ERROR: string;
};

const SIGN_UP: Action = {
    REQUEST: 'SIGN_UP_REQUEST',
    SUCCESS: 'SIGN_UP_SUCCESS',
    ERROR: 'SIGN_UP_ERROR',
};

const SIGN_IN: Action = {
    REQUEST: 'SIGN_IN_REQUEST',
    SUCCESS: 'SIGN_IN_SUCCESS',
    ERROR: 'SIGN_IN_ERROR',
};

const RESET_PASSWORD: Action = {
    REQUEST: 'RESET_PASSWORD_REQUEST',
    SUCCESS: 'RESET_PASSWORD_SUCCESS',
    ERROR: 'RESET_PASSWORD_ERROR',
};

const SIGN_OUT: Action = {
    REQUEST: 'SIGN_OUT_REQUEST',
    SUCCESS: 'SIGN_OUT_SUCCESS',
    ERROR: 'SIGN_OUT_ERROR',
};

const SEND_FEEDBACK: Action = {
    REQUEST: 'SEND_FEEDBACK_REQUEST',
    SUCCESS: 'SEND_FEEDBACK_SUCCESS',
    ERROR: 'SEND_FEEDBACK_ERROR',
};

const GET_UPDATES: Action = {
    REQUEST: 'GET_UPDATES_REQUEST',
    SUCCESS: 'GET_UPDATES_SUCCESS',
    ERROR: 'GET_UPDATES_ERROR',
};

const VOTE_POLL: Action = {
    REQUEST: 'VOTE_POLL_REQUEST',
    SUCCESS: 'VOTE_POLL_SUCCESS',
    ERROR: 'VOTE_POLL_ERROR',
};

const GET_TABLES: Action = {
    REQUEST: 'GET_TABLES_REQUEST',
    SUCCESS: 'GET_TABLES_SUCCESS',
    ERROR: 'GET_TABLES_ERROR',
};

const CREATE_TABLE: Action = {
    REQUEST: 'CREATE_TABLE_REQUEST',
    SUCCESS: 'CREATE_TABLE_SUCCESS',
    ERROR: 'CREATE_TABLE_ERROR',
};

/*

Sign up

*/

const signUp = (payload) => {
    return {
        type: SIGN_UP.REQUEST,
        payload,
    };
};

const signUpSuccess = (payload) => {
    return {
        type: SIGN_UP.SUCCESS,
        payload,
    };
};

const signUpError = (payload) => {
    return {
        type: SIGN_UP.ERROR,
        payload,
    };
};

/*

Sign in

*/

const signIn = (payload) => {
    return {
        type: SIGN_IN.REQUEST,
        payload,
    };
};

const signInSuccess = (payload) => {
    return {
        type: SIGN_IN.SUCCESS,
        payload,
    };
};

const signInError = (payload) => {
    return {
        type: SIGN_IN.ERROR,
        payload,
    };
};

/*

Reset password

*/

const resetPassword = (payload) => {
    return {
        type: RESET_PASSWORD.REQUEST,
        payload,
    };
};

const resetPasswordSuccess = (payload) => {
    return {
        type: RESET_PASSWORD.SUCCESS,
        payload,
    };
};

const resetPasswordError = (payload) => {
    return {
        type: RESET_PASSWORD.ERROR,
        payload,
    };
};

/*

Logout

*/

const signOut = () => {
    return {
        type: SIGN_OUT.REQUEST,
    };
};

const signOutSuccess = () => {
    return {
        type: SIGN_OUT.SUCCESS,
    };
};

const signOutError = (payload) => {
    return {
        type: SIGN_OUT.ERROR,
        payload,
    };
};

/*

Send feedback

*/

const sendFeedback = (payload) => {
    return {
        type: SEND_FEEDBACK.REQUEST,
        payload,
    };
};

const sendFeedbackSuccess = (payload) => {
    return {
        type: SEND_FEEDBACK.SUCCESS,
        payload,
    };
};

const sendFeedbackError = (payload) => {
    return {
        type: SEND_FEEDBACK.ERROR,
        payload,
    };
};

/*

Get updates

*/

const getUpdates = () => {
    return {
        type: GET_UPDATES.REQUEST,
    };
};

const getUpdatesSuccess = (payload) => {
    return {
        type: GET_UPDATES.SUCCESS,
        payload,
    };
};

const getUpdatesError = (payload) => {
    return {
        type: GET_UPDATES.ERROR,
        payload,
    };
};

/*

Vote

*/

const votePoll = (payload) => {
    return {
        type: VOTE_POLL.REQUEST,
        payload,
    };
};

const votePollSuccess = (payload) => {
    return {
        type: VOTE_POLL.SUCCESS,
        payload,
    };
};

const votePollError = (payload) => {
    return {
        type: VOTE_POLL.ERROR,
        payload,
    };
};

/*

Get tables

*/

const getTables = (payload) => {
    return {
        type: GET_TABLES.REQUEST,
        payload,
    };
};

const getTablesSuccess = (payload) => {
    return {
        type: GET_TABLES.SUCCESS,
        payload,
    };
};

const getTablesError = (payload) => {
    return {
        type: GET_TABLES.ERROR,
        payload,
    };
};

/*

Create table

*/

const createTable = (payload) => {
    return {
        type: CREATE_TABLE.REQUEST,
        payload,
    };
};

const createTableSuccess = (payload) => {
    return {
        type: CREATE_TABLE.SUCCESS,
        payload,
    };
};

const createTableError = (payload) => {
    return {
        type: CREATE_TABLE.ERROR,
        payload,
    };
};

export {
    SIGN_UP,
    SIGN_IN,
    RESET_PASSWORD,
    SIGN_OUT,
    SEND_FEEDBACK,
    GET_UPDATES,
    VOTE_POLL,
    GET_TABLES,
    CREATE_TABLE,
    signUp,
    signUpSuccess,
    signUpError,
    signIn,
    signInSuccess,
    signInError,
    resetPassword,
    resetPasswordSuccess,
    resetPasswordError,
    signOut,
    signOutSuccess,
    signOutError,
    sendFeedback,
    sendFeedbackSuccess,
    sendFeedbackError,
    getUpdates,
    getUpdatesSuccess,
    getUpdatesError,
    votePoll,
    votePollSuccess,
    votePollError,
    getTables,
    getTablesSuccess,
    getTablesError,
    createTable,
    createTableSuccess,
    createTableError,
};
