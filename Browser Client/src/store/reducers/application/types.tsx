export type User = {
    username: string;
    email?: string;
};

export type PollOption = {
    title: string;
    votes: number;
}

export type Poll = {
    selected?: number;
    options: PollOption[];
};

export type Update = {
    id: number;
    title: string;
    content: string | Poll;
    type: 'poll' | 'text';
    optionalText?: string;
    date: Date;
};

export type Table = {
    name: string;
    createdBy: string;
    dateCreated: number;
    gameNumber: number;
    type: string;
    online: number;
    max: number;
    smallBlind: number;
    bigBlind: number;
    variant: string;
    turnDuration: number;
    minBuyIn: number;
    maxBuyIn: number;
    ledger: Record<string, number>;
};

export interface ApplicationState {
    user?: User;
    updates?: Update[];
    tables?: {
        archive?: Table[];
        active?: Table[];
    };
    isAuthenticated: boolean;
    isLoading: Record<string, unknown>;
    error: Record<string, unknown>;
    success: Record<string, unknown>;
}

export interface ApplicationAction {
    payload?: Record<string, any>;
    type: string;
}
