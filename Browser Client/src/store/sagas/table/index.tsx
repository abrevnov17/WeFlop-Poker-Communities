import { call, put, takeLatest } from 'redux-saga/effects';
import {
    JOIN_GAME,
    CHOOSE_PANEL,
    BET,
    CHECK,
    FOLD,
    RAISE,
    CALL,
    SIT_IN,
    SIT_OUT,
    CHANGE_SEATS,
    WAIT_BB,
    POST_BB,
    SIT_OUT_NEXT_BB,
    SIT_OUT_NEXT_HAND,
    BUY_IN,
    TOP_UP,
    MUCK,
    SHOW,
    GET_CHAT,
    GET_BALANCE,
    SET_AUTO_MUCK,
    SET_MUSIC,
    SET_SOUNDS,
    GET_LEDGER,
    SEND_MESSAGE_CHAT,
    SET_AUTO_ACTION,
    UPDATE_INFO,
    CANCEL_CHOOSING,
    choosePanelSuccess,
    choosePanelError,
    joinGameSuccess,
    joinGameError,
    betSuccess,
    betError,
    checkSuccess,
    checkError,
    foldError,
    foldSuccess,
    raiseError,
    raiseSuccess,
    callError,
    callSuccess,
    sitInError,
    sitInSuccess,
    sitOutSuccess,
    sitOutError,
    changeSeatsSuccess,
    changeSeatsError,
    waitBBSuccess,
    waitBBError,
    postBBError,
    postBBSuccess,
    sitOutNextBBError,
    sitOutNextBBSuccess,
    sitOutNextHandSuccess,
    sitOutNextHandError,
    buyInSuccess,
    buyInError,
    topUpError,
    topUpSuccess,
    muckError,
    muckSuccess,
    showError,
    showSuccess,
    getChatSuccess,
    getChatError,
    getBalanceSuccess,
    getBalanceError,
    setAutoMuckSuccess,
    setAutoMuckError,
    setMusicSuccess,
    setMusicError,
    setSoundsSuccess,
    setSoundsError,
    getLedgerSuccess,
    getLedgerError,
    sendMessageChatSuccess,
    sendMessageChatError,
    setAutoActionSuccess,
    setAutoActionError,
    cancelChoosingSuccess,
    cancelChoosingError,
    updateInfoSuccess,
    updateInfoError,
} from '../../actions/table';

const joinGame = function* (action) {
    try {
        yield put(joinGameSuccess(action.payload));
    } catch (error) {
        yield put(joinGameError(error));
    }
};

const choosePanel = function* (action) {
    try {
        yield put(choosePanelSuccess(action.payload));
    } catch (error) {
        yield put(choosePanelError(error));
    }
};

const bet = function* (action) {
    const data = {};
    try {
        yield put(betSuccess(action.payload));
    } catch (error) {
        yield put(betError(error));
    }
};

const check = function* () {
    try {
        yield put(checkSuccess());
    } catch (error) {
        yield put(checkError(error));
    }
};

const fold = function* () {
    try {
        yield put(foldSuccess());
    } catch (error) {
        yield put(foldError(error));
    }
};

const raise = function* (action) {
    const data = {};
    try {
        yield put(raiseSuccess(action.payload));
    } catch (error) {
        yield put(raiseError(error));
    }
};

const callAction = function* (action) {
    try {
        yield put(callSuccess(action.payload));
    } catch (error) {
        yield put(callError(error));
    }
};

const sitIn = function* (action) {
    const data = {};
    try {
        yield put(sitInSuccess(data));
    } catch (error) {
        yield put(sitInError(error));
    }
};

const sitOut = function* (action) {
    const data = {};
    try {
        yield put(sitOutSuccess(data));
    } catch (error) {
        yield put(sitOutError(error));
    }
};

const changeSeats = function* (action) {
    const data = {};
    try {
        yield put(changeSeatsSuccess(data));
    } catch (error) {
        yield put(changeSeatsError(error));
    }
};

const waitBB = function* (action) {
    const data = {};
    try {
        yield put(waitBBSuccess(data));
    } catch (error) {
        yield put(waitBBError(error));
    }
};

const postBB = function* (action) {
    const data = {};
    try {
        yield put(postBBSuccess(data));
    } catch (error) {
        yield put(postBBError(error));
    }
};

const sitOutNextBB = function* (action) {
    const data = {};
    try {
        yield put(sitOutNextBBSuccess(data));
    } catch (error) {
        yield put(sitOutNextBBError(error));
    }
};

const sitOutNextHand = function* (action) {
    const data = {};
    try {
        yield put(sitOutNextHandSuccess(data));
    } catch (error) {
        yield put(sitOutNextHandError(error));
    }
};

const buyIn = function* (action) {
    const data = {};
    try {
        yield put(buyInSuccess(data));
    } catch (error) {
        yield put(buyInError(error));
    }
};

const topUp = function* (action) {
    const data = {};
    try {
        yield put(topUpSuccess(data));
    } catch (error) {
        yield put(topUpError(error));
    }
};

const muck = function* (action) {
    try {
        yield put(muckSuccess());
    } catch (error) {
        yield put(muckError(error));
    }
};

const show = function* (action) {
    try {
        yield put(showSuccess());
    } catch (error) {
        yield put(showError(error));
    }
};

const getChat = function* (action) {
    const data = {};
    try {
        yield put(getChatSuccess(data));
    } catch (error) {
        yield put(getChatError(error));
    }
};

const getBalance = function* (action) {
    const data = {};
    try {
        yield put(getBalanceSuccess(data));
    } catch (error) {
        yield put(getBalanceError(error));
    }
};

const setMusic = function* (action) {
    try {
        yield put(setMusicSuccess());
    } catch (error) {
        yield put(setMusicError(error));
    }
};

const setAutoMuck = function* (action) {
    try {
        yield put(setAutoMuckSuccess());
    } catch (error) {
        yield put(setAutoMuckError(error));
    }
};

const setSounds = function* (action) {
    try {
        yield put(setSoundsSuccess());
    } catch (error) {
        yield put(setSoundsError(error));
    }
};

const getLedger = function* (action) {
    const data = {};
    try {
        yield put(getLedgerSuccess(data));
    } catch (error) {
        yield put(getLedgerError(error));
    }
};

const sendMessageChat = function* (action) {
    try {
        yield put(sendMessageChatSuccess(action.payload));
    } catch (error) {
        yield put(sendMessageChatError(error));
    }
};

const setAutoAction = function* (action) {
    try {
        yield put(setAutoActionSuccess(action.payload));
    } catch (error) {
        yield put(setAutoActionError(error));
    }
};

const updateInfo = function* (action) {
    try {
        yield put(updateInfoSuccess(action.payload));
    } catch (error) {
        yield put(updateInfoError(error));
    }
};

const cancelChoosing = function* (action) {
    try {
        yield put(cancelChoosingSuccess());
    } catch (error) {
        yield put(cancelChoosingError(error));
    }
};

const table = function* () {
    yield takeLatest(JOIN_GAME.REQUEST, joinGame);
    yield takeLatest(CHOOSE_PANEL.REQUEST, choosePanel);
    yield takeLatest(BET.REQUEST, bet);
    yield takeLatest(CHECK.REQUEST, check);
    yield takeLatest(FOLD.REQUEST, fold);
    yield takeLatest(RAISE.REQUEST, raise);
    yield takeLatest(CALL.REQUEST, callAction);
    yield takeLatest(SIT_IN.REQUEST, sitIn);
    yield takeLatest(SIT_OUT.REQUEST, sitOut);
    yield takeLatest(CHANGE_SEATS.REQUEST, changeSeats);
    yield takeLatest(WAIT_BB.REQUEST, waitBB);
    yield takeLatest(POST_BB.REQUEST, postBB);
    yield takeLatest(SIT_OUT_NEXT_BB.REQUEST, sitOutNextBB);
    yield takeLatest(SIT_OUT_NEXT_HAND.REQUEST, sitOutNextHand);
    yield takeLatest(BUY_IN.REQUEST, buyIn);
    yield takeLatest(TOP_UP.REQUEST, topUp);
    yield takeLatest(MUCK.REQUEST, muck);
    yield takeLatest(SHOW.REQUEST, show);
    yield takeLatest(GET_CHAT.REQUEST, getChat);
    yield takeLatest(GET_BALANCE.REQUEST, getBalance);
    yield takeLatest(SET_AUTO_MUCK.REQUEST, setAutoMuck);
    yield takeLatest(SET_MUSIC.REQUEST, setMusic);
    yield takeLatest(SET_SOUNDS.REQUEST, setSounds);
    yield takeLatest(GET_LEDGER.REQUEST, getLedger);
    yield takeLatest(SEND_MESSAGE_CHAT.REQUEST, sendMessageChat);
    yield takeLatest(SET_AUTO_ACTION.REQUEST, setAutoAction);
    yield takeLatest(UPDATE_INFO.REQUEST, updateInfo);
    yield takeLatest(CANCEL_CHOOSING.REQUEST, cancelChoosing);
    yield takeLatest(UPDATE_INFO.REQUEST, updateInfo);
};

export default table;
