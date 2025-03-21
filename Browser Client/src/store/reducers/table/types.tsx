export interface TableState {
    isLoading: Record<string, unknown>;
    error: Record<string, unknown>;
    success: Record<string, unknown>;
    players: Player[];
    slot?: number;
    cards?: string[];
    chat: { user: { username: string; color: string }; message: string }[];
    balance?: { totalPot?: number; currentPot?: number; sidePots?: number[] };
    autoMuck: boolean;
    music: boolean;
    gameSounds: boolean;
    player?: Player;
    status: 'waiting' | 'started' | 'betting' | 'finished-round' | 'finished';
    timeBank: number;
    autoAction: 'default' | 'checkfold' | 'callany' | 'muck' | 'show';
    ledger: { name: string; id: string; balance: number }[];
    tableName: string;
}

export interface TableAction {
    payload?: Record<string, any>;
    type: string;
}

export interface Player {
    username: string;
    lastAction: {
        type?: 'bet' | 'call' | 'raise' | 'check' | 'muck' | 'show';
        params?: Record<string, any>;
    };
    balance?: { pot?: number; main?: number };
    active?: boolean;
    slot: number;
    isDealer?: boolean;
    timeLeft?: number;
    status?: 'selected' | 'sitted-in' | 'sitted-out' | 'folded' | 'won' | 'lost' | 'waiting';
    cards: [string, string];
    color: string;
}
