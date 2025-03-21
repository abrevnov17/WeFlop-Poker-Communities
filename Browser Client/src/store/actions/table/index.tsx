type Action = {
    REQUEST: string;
    SUCCESS: string;
    ERROR: string;
};

const CHOOSE_PANEL: Action = {
    REQUEST: 'CHOOSE_PANEL_REQUEST',
    SUCCESS: 'CHOOSE_PANEL_SUCCESS',
    ERROR: 'CHOOSE_PANEL_ERROR',
};

const JOIN_GAME: Action = {
    REQUEST: 'JOIN_GAME_REQUEST',
    SUCCESS: 'JOIN_GAME_SUCCESS',
    ERROR: 'JOIN_GAME_ERROR',
};

const BET: Action = {
    REQUEST: 'BET_REQUEST',
    SUCCESS: 'BET_SUCCESS',
    ERROR: 'BET_ERROR',
};

const CHECK: Action = {
    REQUEST: 'CHECK_REQUEST',
    SUCCESS: 'CHECK_SUCCESS',
    ERROR: 'CHECK_ERROR',
};

const FOLD: Action = {
    REQUEST: 'FOLD_REQUEST',
    SUCCESS: 'FOLD_SUCCESS',
    ERROR: 'FOLD_ERROR',
};

const RAISE: Action = {
    REQUEST: 'RAISE_REQUEST',
    SUCCESS: 'RAISE_SUCCESS',
    ERROR: 'RAISE_ERROR',
};

const CALL: Action = {
    REQUEST: 'CALL_REQUEST',
    SUCCESS: 'CALL_SUCCESS',
    ERROR: 'CALL_ERROR',
};

const SIT_IN: Action = {
    REQUEST: 'SIT_IN_REQUEST',
    SUCCESS: 'SIT_IN_SUCCESS',
    ERROR: 'SIT_IN_ERROR',
};

const SIT_OUT: Action = {
    REQUEST: 'SIT_OUT_REQUEST',
    SUCCESS: 'SIT_OUT_SUCCESS',
    ERROR: 'SIT_OUT_ERROR',
};

const CHANGE_SEATS: Action = {
    REQUEST: 'CHANGE_SEATS_REQUEST',
    SUCCESS: 'CHANGE_SEATS_SUCCESS',
    ERROR: 'CHANGE_SEATS_ERROR',
};

const WAIT_BB: Action = {
    REQUEST: 'WAIT_BB_REQUEST',
    SUCCESS: 'WAIT_BB_SUCCESS',
    ERROR: 'WAIT_BB_ERROR',
};

const POST_BB: Action = {
    REQUEST: 'POST_BB_REQUEST',
    SUCCESS: 'POST_BB_SUCCESS',
    ERROR: 'POST_BB_ERROR',
};

const SIT_OUT_NEXT_BB: Action = {
    REQUEST: 'SIT_OUT_NEXT_BB_REQUEST',
    SUCCESS: 'SIT_OUT_NEXT_BB_SUCCESS',
    ERROR: 'SIT_OUT_NEXT_BB_ERROR',
};

const SIT_OUT_NEXT_HAND: Action = {
    REQUEST: 'SIT_OUT_NEXT_HAND_REQUEST',
    SUCCESS: 'SIT_OUT_NEXT_HAND_SUCCESS',
    ERROR: 'SIT_OUT_NEXT_HAND_ERROR',
};

const BUY_IN: Action = {
    REQUEST: 'BUY_IN_REQUEST',
    SUCCESS: 'BUY_IN_SUCCESS',
    ERROR: 'BUY_IN_ERROR',
};

const TOP_UP: Action = {
    REQUEST: 'TOP_UP_REQUEST',
    SUCCESS: 'TOP_UP_SUCCESS',
    ERROR: 'TOP_UP_ERROR',
};

const MUCK: Action = {
    REQUEST: 'MUCK_REQUEST',
    SUCCESS: 'MUCK_SUCCESS',
    ERROR: 'MUCK_ERROR',
};

const SHOW: Action = {
    REQUEST: 'SHOW_REQUEST',
    SUCCESS: 'SHOW_SUCCESS',
    ERROR: 'SHOW_ERROR',
};

const GET_CHAT: Action = {
    REQUEST: 'GET_CHAT_REQUEST',
    SUCCESS: 'GET_CHAT_SUCCESS',
    ERROR: 'GET_CHAT_ERROR',
};

const GET_BALANCE: Action = {
    REQUEST: 'GET_BALANCE_REQUEST',
    SUCCESS: 'GET_BALANCE_SUCCESS',
    ERROR: 'GET_BALANCE_ERROR',
};

const SET_AUTO_MUCK: Action = {
    REQUEST: 'SET_AUTO_MUCK_REQUEST',
    SUCCESS: 'SET_AUTO_MUCK_SUCCESS',
    ERROR: 'SET_AUTO_MUCK_ERROR',
};

const SET_MUSIC: Action = {
    REQUEST: 'SET_MUSIC_REQUEST',
    SUCCESS: 'SET_MUSIC_SUCCESS',
    ERROR: 'SET_MUSIC_ERROR',
};

const SET_SOUNDS: Action = {
    REQUEST: 'SET_SOUNDS_REQUEST',
    SUCCESS: 'SET_SOUNDS_SUCCESS',
    ERROR: 'SET_SOUNDS_ERROR',
};

const GET_LEDGER: Action = {
    REQUEST: 'GET_LEDGER_REQUEST',
    SUCCESS: 'GET_LEDGER_SUCCESS',
    ERROR: 'GET_LEDGER_ERROR',
};

const SEND_MESSAGE_CHAT: Action = {
    REQUEST: 'SEND_MESSAGE_CHAT_REQUEST',
    SUCCESS: 'SEND_MESSAGE_CHAT_SUCCESS',
    ERROR: 'SEND_MESSAGE_CHAT_ERROR',
};

const UPDATE_INFO: Action = {
    REQUEST: 'UPDATE_INFO_REQUEST',
    SUCCESS: 'UPDATE_INFO_SUCCESS',
    ERROR: 'UPDATE_INFO_ERROR',
};

const SET_AUTO_ACTION: Action = {
    REQUEST: 'SET_AUTO_ACTION_REQUEST',
    SUCCESS: 'SET_AUTO_ACTION_SUCCESS',
    ERROR: 'SET_AUTO_ACTION_ERROR',
};

const CANCEL_CHOOSING: Action = {
    REQUEST: 'CANCEL_CHOOSING_REQUEST',
    SUCCESS: 'CANCEL_CHOOSING_SUCCESS',
    ERROR: 'CANCEL_CHOOSING_ERROR',
};

// choose panel

const choosePanel = (payload) => {
    return {
        type: CHOOSE_PANEL.REQUEST,
        payload,
    };
};

const choosePanelSuccess = (payload) => {
    return {
        type: CHOOSE_PANEL.SUCCESS,
        payload,
    };
};

const choosePanelError = (payload) => {
    return {
        type: CHOOSE_PANEL.ERROR,
        payload,
    };
};

// join game

const joinGame = (payload, socket) => {
    return {
        type: JOIN_GAME.REQUEST,
        payload,
        socket,
    };
};

const joinGameSuccess = (payload) => {
    return {
        type: JOIN_GAME.SUCCESS,
        payload,
    };
};

const joinGameError = (payload) => {
    return {
        type: JOIN_GAME.ERROR,
        payload,
    };
};

// bet

const bet = (payload, socket) => {
    return {
        type: BET.REQUEST,
        payload,
        socket,
    };
};

const betSuccess = (payload) => {
    return {
        type: BET.SUCCESS,
        payload,
    };
};

const betError = (payload) => {
    return {
        type: BET.ERROR,
        payload,
    };
};

// check

const check = (socket) => {
    return {
        type: CHECK.REQUEST,
        socket,
    };
};

const checkSuccess = () => {
    return {
        type: CHECK.SUCCESS,
    };
};

const checkError = (payload) => {
    return {
        type: CHECK.ERROR,
        payload,
    };
};

// fold

const fold = (socket) => {
    return {
        type: FOLD.REQUEST,
        socket,
    };
};

const foldSuccess = () => {
    return {
        type: FOLD.SUCCESS,
    };
};

const foldError = (payload) => {
    return {
        type: FOLD.ERROR,
        payload,
    };
};

// raise

const raise = (payload, socket) => {
    return {
        type: RAISE.REQUEST,
        payload,
        socket,
    };
};

const raiseSuccess = (payload) => {
    return {
        type: RAISE.SUCCESS,
        payload,
    };
};

const raiseError = (payload) => {
    return {
        type: RAISE.ERROR,
        payload,
    };
};

// call

const call = (payload, socket) => {
    return {
        type: CALL.REQUEST,
        payload,
        socket,
    };
};

const callSuccess = (payload) => {
    return {
        type: CALL.SUCCESS,
        payload,
    };
};

const callError = (payload) => {
    return {
        type: CALL.ERROR,
        payload,
    };
};

// sit in

const sitIn = (payload, socket) => {
    return {
        type: SIT_IN.REQUEST,
        payload,
        socket,
    };
};

const sitInSuccess = (payload) => {
    return {
        type: SIT_IN.SUCCESS,
        payload,
    };
};

const sitInError = (payload) => {
    return {
        type: SIT_IN.ERROR,
        payload,
    };
};

// sit out

const sitOut = (payload, socket) => {
    return {
        type: SIT_OUT.REQUEST,
        payload,
        socket,
    };
};

const sitOutSuccess = (payload) => {
    return {
        type: SIT_OUT.SUCCESS,
        payload,
    };
};

const sitOutError = (payload) => {
    return {
        type: SIT_OUT.ERROR,
        payload,
    };
};

// change seats

const changeSeats = (payload, socket) => {
    return {
        type: BET.REQUEST,
        payload,
        socket,
    };
};

const changeSeatsSuccess = (payload) => {
    return {
        type: CHANGE_SEATS.SUCCESS,
        payload,
    };
};

const changeSeatsError = (payload) => {
    return {
        type: CHANGE_SEATS.ERROR,
        payload,
    };
};

// wait bb

const waitBB = (payload, socket) => {
    return {
        type: WAIT_BB.REQUEST,
        payload,
        socket,
    };
};

const waitBBSuccess = (payload) => {
    return {
        type: WAIT_BB.SUCCESS,
        payload,
    };
};

const waitBBError = (payload) => {
    return {
        type: WAIT_BB.ERROR,
        payload,
    };
};

// post bb

const postBB = (payload, socket) => {
    return {
        type: POST_BB.REQUEST,
        payload,
        socket,
    };
};

const postBBSuccess = (payload) => {
    return {
        type: POST_BB.SUCCESS,
        payload,
    };
};

const postBBError = (payload) => {
    return {
        type: POST_BB.ERROR,
        payload,
    };
};

// sit out next bb

const sitOutNextBB = (payload, socket) => {
    return {
        type: SIT_OUT_NEXT_BB.REQUEST,
        payload,
        socket,
    };
};

const sitOutNextBBSuccess = (payload) => {
    return {
        type: SIT_OUT_NEXT_BB.SUCCESS,
        payload,
    };
};

const sitOutNextBBError = (payload) => {
    return {
        type: SIT_OUT_NEXT_BB.ERROR,
        payload,
    };
};

// sit out next hand

const sitOutNextHand = (payload, socket) => {
    return {
        type: SIT_OUT_NEXT_HAND.REQUEST,
        payload,
        socket,
    };
};

const sitOutNextHandSuccess = (payload) => {
    return {
        type: SIT_OUT_NEXT_HAND.SUCCESS,
        payload,
    };
};

const sitOutNextHandError = (payload) => {
    return {
        type: SIT_OUT_NEXT_HAND.ERROR,
        payload,
    };
};

// buy in

const buyIn = (payload, socket) => {
    return {
        type: BUY_IN.REQUEST,
        payload,
        socket,
    };
};

const buyInSuccess = (payload) => {
    return {
        type: BUY_IN.SUCCESS,
        payload,
    };
};

const buyInError = (payload) => {
    return {
        type: BUY_IN.ERROR,
        payload,
    };
};

// top up

const topUp = (payload, socket) => {
    return {
        type: TOP_UP.REQUEST,
        payload,
        socket,
    };
};

const topUpSuccess = (payload) => {
    return {
        type: TOP_UP.SUCCESS,
        payload,
    };
};

const topUpError = (payload) => {
    return {
        type: TOP_UP.ERROR,
        payload,
    };
};

// muck

const muck = (socket) => {
    return {
        type: MUCK.REQUEST,
        socket,
    };
};

const muckSuccess = () => {
    return {
        type: MUCK.SUCCESS,
    };
};

const muckError = (payload) => {
    return {
        type: MUCK.ERROR,
        payload,
    };
};

// show

const show = (socket) => {
    return {
        type: BET.REQUEST,
        socket,
    };
};

const showSuccess = () => {
    return {
        type: SHOW.SUCCESS,
    };
};

const showError = (payload) => {
    return {
        type: SHOW.ERROR,
        payload,
    };
};

// get chat

const getChat = (payload, socket) => {
    return {
        type: GET_CHAT.REQUEST,
        payload,
        socket,
    };
};

const getChatSuccess = (payload) => {
    return {
        type: GET_CHAT.SUCCESS,
        payload,
    };
};

const getChatError = (payload) => {
    return {
        type: GET_CHAT.ERROR,
        payload,
    };
};

// get balance

const getBalance = (payload, socket) => {
    return {
        type: GET_BALANCE.REQUEST,
        payload,
        socket,
    };
};

const getBalanceSuccess = (payload) => {
    return {
        type: GET_BALANCE.SUCCESS,
        payload,
    };
};

const getBalanceError = (payload) => {
    return {
        type: GET_BALANCE.ERROR,
        payload,
    };
};

// set auto muck

const setAutoMuck = (socket) => {
    return {
        type: SET_AUTO_MUCK.REQUEST,
        socket,
    };
};

const setAutoMuckSuccess = () => {
    return {
        type: SET_AUTO_MUCK.SUCCESS,
    };
};

const setAutoMuckError = (payload) => {
    return {
        type: SET_AUTO_MUCK.ERROR,
        payload,
    };
};

// set music

const setMusic = () => {
    return {
        type: SET_MUSIC.REQUEST,
    };
};

const setMusicSuccess = () => {
    return {
        type: SET_MUSIC.SUCCESS,
    };
};

const setMusicError = (payload) => {
    return {
        type: SET_MUSIC.ERROR,
        payload,
    };
};

// set sounds

const setSounds = () => {
    return {
        type: SET_SOUNDS.REQUEST,
    };
};

const setSoundsSuccess = () => {
    return {
        type: SET_SOUNDS.SUCCESS,
    };
};

const setSoundsError = (payload) => {
    return {
        type: SET_SOUNDS.ERROR,
        payload,
    };
};

// get ledger

const getLedger = (payload, socket) => {
    return {
        type: GET_LEDGER.REQUEST,
        payload,
        socket,
    };
};

const getLedgerSuccess = (payload) => {
    return {
        type: GET_LEDGER.SUCCESS,
        payload,
    };
};

const getLedgerError = (payload) => {
    return {
        type: GET_LEDGER.ERROR,
        payload,
    };
};

// send message chat

const sendMessageChat = (payload, socket) => {
    return {
        type: SEND_MESSAGE_CHAT.REQUEST,
        payload,
        socket,
    };
};

const sendMessageChatSuccess = (payload) => {
    return {
        type: SEND_MESSAGE_CHAT.SUCCESS,
        payload,
    };
};

const sendMessageChatError = (payload) => {
    return {
        type: SEND_MESSAGE_CHAT.ERROR,
        payload,
    };
};

// send message chat

const updateInfo = (payload) => {
    return {
        type: UPDATE_INFO.REQUEST,
        payload,
    };
};

const updateInfoSuccess = (payload) => {
    return {
        type: UPDATE_INFO.SUCCESS,
        payload,
    };
};

const updateInfoError = (payload) => {
    return {
        type: UPDATE_INFO.ERROR,
        payload,
    };
};

// set auto action

const setAutoAction = (payload) => {
    return {
        type: SET_AUTO_ACTION.REQUEST,
        payload,
    };
};

const setAutoActionSuccess = (payload) => {
    return {
        type: SET_AUTO_ACTION.SUCCESS,
        payload,
    };
};

const setAutoActionError = (payload) => {
    return {
        type: SET_AUTO_ACTION.ERROR,
        payload,
    };
};

// remove player

const cancelChoosing = () => {
    return {
        type: CANCEL_CHOOSING.REQUEST,
    };
};

const cancelChoosingSuccess = () => {
    return {
        type: CANCEL_CHOOSING.SUCCESS,
    };
};

const cancelChoosingError = (payload) => {
    return {
        type: CANCEL_CHOOSING.ERROR,
        payload,
    };
};

export {
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
    UPDATE_INFO,
    SET_AUTO_ACTION,
    CANCEL_CHOOSING,
    joinGame,
    joinGameSuccess,
    joinGameError,
    bet,
    betSuccess,
    betError,
    check,
    checkSuccess,
    checkError,
    fold,
    foldError,
    foldSuccess,
    raise,
    raiseError,
    raiseSuccess,
    call,
    callError,
    callSuccess,
    sitIn,
    sitInError,
    sitInSuccess,
    sitOut,
    sitOutSuccess,
    sitOutError,
    changeSeats,
    changeSeatsSuccess,
    changeSeatsError,
    waitBB,
    waitBBSuccess,
    waitBBError,
    postBB,
    postBBError,
    postBBSuccess,
    sitOutNextBB,
    sitOutNextBBError,
    sitOutNextBBSuccess,
    sitOutNextHand,
    sitOutNextHandSuccess,
    sitOutNextHandError,
    buyIn,
    buyInSuccess,
    buyInError,
    topUp,
    topUpError,
    topUpSuccess,
    muck,
    muckError,
    muckSuccess,
    show,
    showError,
    showSuccess,
    getChat,
    getChatSuccess,
    getChatError,
    getBalance,
    getBalanceSuccess,
    getBalanceError,
    setAutoMuck,
    setAutoMuckSuccess,
    setAutoMuckError,
    setMusic,
    setMusicSuccess,
    setMusicError,
    setSounds,
    setSoundsSuccess,
    setSoundsError,
    getLedger,
    getLedgerSuccess,
    getLedgerError,
    sendMessageChat,
    sendMessageChatSuccess,
    sendMessageChatError,
    updateInfo,
    updateInfoSuccess,
    updateInfoError,
    choosePanel,
    choosePanelSuccess,
    choosePanelError,
    setAutoAction,
    setAutoActionError,
    setAutoActionSuccess,
    cancelChoosing,
    cancelChoosingError,
    cancelChoosingSuccess,
};
