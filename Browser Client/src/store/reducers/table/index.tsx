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
    CANCEL_CHOOSING,
    UPDATE_INFO,
} from '../../actions/table';
import { TableState, TableAction } from './types';
import { stat } from 'fs';

const defaultTableState: TableState = {
    isLoading: {},
    error: {},
    success: {},
    slot: undefined,
    tableName: 'Test table',
    ledger: [
        {
            name: 'glenn',
            id: '1',
            balance: -14,
        },
        {
            name: 'glenn',
            id: '2',
            balance: 2,
        },
    ],
    players: [
        {
            username: 'john',
            slot: 1,
            balance: {
                main: 100,
            },
            lastAction: {},
            cards: ['H1', 'H1'],
            color: '#C49D3A',
        },
        {
            username: 'glenn',
            slot: 2,
            balance: {
                main: 100,
            },
            lastAction: {},
            cards: ['H1', 'H1'],
            isDealer: true,
            color: '#C49D3A',
        },
        {
            username: 'nick',
            color: '#C49D3A',
            slot: 6,
            balance: {
                main: 100,
            },
            lastAction: {},
            cards: ['H1', 'H1'],
        },
    ],
    timeBank: 7,
    player: undefined,
    chat: [
        {
            message: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit',
            user: { username: 'jacob', color: '#C49D3A' },
        },
        {
            message: 'Lorem ipsum dolor sit',
            user: { username: 'john', color: '#8B112F' },
        },
    ],
    balance: undefined,
    autoMuck: false,
    music: false,
    gameSounds: true,
    status: 'waiting',
    autoAction: 'default',
};

const table = (state: TableState = defaultTableState, action: TableAction) => {
    switch (action.type) {
        case UPDATE_INFO.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [UPDATE_INFO.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [UPDATE_INFO.ERROR]: undefined,
                },
            };
        case UPDATE_INFO.SUCCESS:
            return {
                ...state,
                ...action.payload,
                isLoading: {
                    ...state.isLoading,
                    [UPDATE_INFO.REQUEST]: undefined,
                },
            };
        case UPDATE_INFO.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [UPDATE_INFO.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [UPDATE_INFO.ERROR]: action.payload,
                },
            };

        case JOIN_GAME.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [JOIN_GAME.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [JOIN_GAME.ERROR]: undefined,
                },
            };
        case JOIN_GAME.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [JOIN_GAME.REQUEST]: undefined,
                },
                slot: action.payload!.slot,
                status: 'started',
                player: undefined,
                balance: {
                    totalPot: 0,
                    currentPot: 0,
                },
                players: [...state.players, { ...state.player, ...action.payload }],
            };
        case JOIN_GAME.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [JOIN_GAME.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [JOIN_GAME.ERROR]: action.payload,
                },
            };

        case CHOOSE_PANEL.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHOOSE_PANEL.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [CHOOSE_PANEL.ERROR]: undefined,
                },
            };
        case CHOOSE_PANEL.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHOOSE_PANEL.REQUEST]: undefined,
                },
                player: {
                    username: action.payload!.username,
                    slot: action.payload!.slot,
                    lastAction: {},
                },
            };
        case CHOOSE_PANEL.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHOOSE_PANEL.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [CHOOSE_PANEL.ERROR]: action.payload,
                },
            };

        case BET.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BET.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [BET.ERROR]: undefined,
                },
            };
        case BET.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BET.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              balance: { ...player.balance, pot: action.payload!.value },
                              lastAction: { type: 'bet', params: { value: action.payload!.value } },
                          }
                        : player;
                }),
            };
        case BET.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BET.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [BET.ERROR]: action.payload,
                },
            };
        case CHECK.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHECK.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [CHECK.ERROR]: undefined,
                },
            };
        case CHECK.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHECK.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              lastAction: { type: 'check' },
                          }
                        : player;
                }),
            };
        case CHECK.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHECK.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [CHECK.ERROR]: action.payload,
                },
            };
        case FOLD.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [FOLD.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [FOLD.ERROR]: undefined,
                },
            };
        case FOLD.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [FOLD.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              status: 'folded',
                          }
                        : player;
                }),
            };
        case FOLD.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [FOLD.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [FOLD.ERROR]: action.payload,
                },
            };
        case RAISE.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [RAISE.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [RAISE.ERROR]: undefined,
                },
            };
        case RAISE.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [RAISE.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              balance: { ...player.balance, pot: player.balance!.pot! + action.payload!.value },
                              lastAction: { type: 'raise', params: { value: action.payload!.value } },
                          }
                        : player;
                }),
            };
        case RAISE.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [RAISE.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [RAISE.ERROR]: action.payload,
                },
            };
        case CALL.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CALL.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [CALL.ERROR]: undefined,
                },
            };
        case CALL.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CALL.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              balance: { ...player.balance, pot: player.balance!.pot! + action.payload!.value },
                              lastAction: { type: 'call', params: { value: action.payload!.value } },
                          }
                        : player;
                }),
            };
        case CALL.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CALL.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [CALL.ERROR]: action.payload,
                },
            };
        case SIT_IN.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_IN.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SIT_IN.ERROR]: undefined,
                },
            };
        case SIT_IN.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_IN.REQUEST]: undefined,
                },
            };
        case SIT_IN.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_IN.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SIT_IN.ERROR]: action.payload,
                },
            };
        case SIT_OUT.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SIT_OUT.ERROR]: undefined,
                },
            };
        case SIT_OUT.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT.REQUEST]: undefined,
                },
            };
        case SIT_OUT.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SIT_OUT.ERROR]: action.payload,
                },
            };
        case CHANGE_SEATS.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHANGE_SEATS.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [CHANGE_SEATS.ERROR]: undefined,
                },
            };
        case CHANGE_SEATS.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHANGE_SEATS.REQUEST]: undefined,
                },
            };
        case CHANGE_SEATS.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CHANGE_SEATS.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [CHANGE_SEATS.ERROR]: action.payload,
                },
            };
        case WAIT_BB.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [WAIT_BB.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [WAIT_BB.ERROR]: undefined,
                },
            };
        case WAIT_BB.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [WAIT_BB.REQUEST]: undefined,
                },
            };
        case WAIT_BB.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [WAIT_BB.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [WAIT_BB.ERROR]: action.payload,
                },
            };
        case POST_BB.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [POST_BB.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [POST_BB.ERROR]: undefined,
                },
            };
        case POST_BB.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [POST_BB.REQUEST]: undefined,
                },
            };
        case POST_BB.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [POST_BB.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [POST_BB.ERROR]: action.payload,
                },
            };
        case SIT_OUT_NEXT_BB.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_BB.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SIT_OUT_NEXT_BB.ERROR]: undefined,
                },
            };
        case SIT_OUT_NEXT_BB.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_BB.REQUEST]: undefined,
                },
            };
        case SIT_OUT_NEXT_BB.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_BB.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SIT_OUT_NEXT_BB.ERROR]: action.payload,
                },
            };
        case SIT_OUT_NEXT_HAND.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_HAND.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SIT_OUT_NEXT_HAND.ERROR]: undefined,
                },
            };
        case SIT_OUT_NEXT_HAND.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_HAND.REQUEST]: undefined,
                },
            };
        case SIT_OUT_NEXT_HAND.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SIT_OUT_NEXT_HAND.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SIT_OUT_NEXT_HAND.ERROR]: action.payload,
                },
            };
        case BUY_IN.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BUY_IN.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [BUY_IN.ERROR]: undefined,
                },
            };
        case BUY_IN.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BUY_IN.REQUEST]: undefined,
                },
            };
        case BUY_IN.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [BUY_IN.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [BUY_IN.ERROR]: action.payload,
                },
            };
        case TOP_UP.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [TOP_UP.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [TOP_UP.ERROR]: undefined,
                },
            };
        case TOP_UP.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [TOP_UP.REQUEST]: undefined,
                },
            };
        case TOP_UP.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [TOP_UP.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [TOP_UP.ERROR]: action.payload,
                },
            };
        case MUCK.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [MUCK.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [MUCK.ERROR]: undefined,
                },
            };
        case MUCK.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [MUCK.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              lastAction: { type: 'muck' },
                          }
                        : player;
                }),
            };
        case MUCK.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [MUCK.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [MUCK.ERROR]: action.payload,
                },
            };
        case SHOW.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SHOW.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SHOW.ERROR]: undefined,
                },
            };
        case SHOW.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SHOW.REQUEST]: undefined,
                },
                players: state.players.map((player) => {
                    return player.slot === state.slot
                        ? {
                              ...player,
                              lastAction: { type: 'show' },
                          }
                        : player;
                }),
            };
        case SHOW.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SHOW.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SHOW.ERROR]: action.payload,
                },
            };
        case GET_CHAT.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_CHAT.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [GET_CHAT.ERROR]: undefined,
                },
            };
        case GET_CHAT.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_CHAT.REQUEST]: undefined,
                },
                chat: action.payload,
            };
        case GET_CHAT.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_CHAT.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [GET_CHAT.ERROR]: action.payload,
                },
            };
        case GET_BALANCE.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_BALANCE.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [GET_BALANCE.ERROR]: undefined,
                },
            };
        case GET_BALANCE.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_BALANCE.REQUEST]: undefined,
                },
                balance: action.payload,
            };
        case GET_BALANCE.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_BALANCE.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [GET_BALANCE.ERROR]: action.payload,
                },
            };
        case SET_AUTO_MUCK.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_MUCK.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SET_AUTO_MUCK.ERROR]: undefined,
                },
            };
        case SET_AUTO_MUCK.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_MUCK.REQUEST]: undefined,
                },
                autoMuck: !state.autoMuck,
            };
        case SET_AUTO_MUCK.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_MUCK.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SET_AUTO_MUCK.ERROR]: action.payload,
                },
            };
        case SET_MUSIC.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_MUSIC.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SET_MUSIC.ERROR]: undefined,
                },
            };
        case SET_MUSIC.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_MUSIC.REQUEST]: undefined,
                },
                music: !state.music,
            };
        case SET_MUSIC.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_MUSIC.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SET_MUSIC.ERROR]: action.payload,
                },
            };
        case SET_SOUNDS.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_SOUNDS.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SET_SOUNDS.ERROR]: undefined,
                },
            };
        case SET_SOUNDS.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_SOUNDS.REQUEST]: undefined,
                },
                gameSounds: !state.gameSounds,
            };
        case SET_SOUNDS.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_SOUNDS.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SET_SOUNDS.ERROR]: action.payload,
                },
            };
        case GET_LEDGER.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_LEDGER.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [GET_LEDGER.ERROR]: undefined,
                },
            };
        case GET_LEDGER.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_LEDGER.REQUEST]: undefined,
                },
                ledger: action.payload,
            };
        case GET_LEDGER.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [GET_LEDGER.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [GET_LEDGER.ERROR]: action.payload,
                },
            };
        case SEND_MESSAGE_CHAT.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SEND_MESSAGE_CHAT.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SEND_MESSAGE_CHAT.ERROR]: undefined,
                },
            };
        case SEND_MESSAGE_CHAT.SUCCESS:
            const player = state.players.find((player) => player.slot === state.slot);
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SEND_MESSAGE_CHAT.REQUEST]: undefined,
                },
                chat: [
                    ...state.chat,
                    { message: action.payload!.message, user: { username: player!.username, color: player!.color } },
                ],
            };
        case SEND_MESSAGE_CHAT.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SEND_MESSAGE_CHAT.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SEND_MESSAGE_CHAT.ERROR]: action.payload,
                },
            };

        case SET_AUTO_ACTION.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_ACTION.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [SET_AUTO_ACTION.ERROR]: undefined,
                },
            };
        case SET_AUTO_ACTION.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_ACTION.REQUEST]: undefined,
                },
                autoAction: action.payload!.name,
            };
        case SET_AUTO_ACTION.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [SET_AUTO_ACTION.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [SET_AUTO_ACTION.ERROR]: action.payload,
                },
            };

        case CANCEL_CHOOSING.REQUEST:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CANCEL_CHOOSING.REQUEST]: true,
                },
                error: {
                    ...state.error,
                    [CANCEL_CHOOSING.ERROR]: undefined,
                },
            };
        case CANCEL_CHOOSING.SUCCESS:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CANCEL_CHOOSING.REQUEST]: undefined,
                },
                player: undefined,
            };
        case CANCEL_CHOOSING.ERROR:
            return {
                ...state,
                isLoading: {
                    ...state.isLoading,
                    [CANCEL_CHOOSING.REQUEST]: false,
                },
                error: {
                    ...state.error,
                    [CANCEL_CHOOSING.ERROR]: action.payload,
                },
            };
        default:
            return state;
    }
};

export default table;
