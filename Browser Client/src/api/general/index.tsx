// import axios from "axios";

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export const sendFeedbackRequest = async (payload) => {
    // payload: {
    //     feedback: string;
    // }

    await wait(4000);

    return {
    };
};

export const getUpdatesRequest = async (payload) => {
    // payload: {
    //     updates: {
    //         id: number;
    //         title: string;
    //         content: string | Poll;
    //         type: string;
    //         optionalText: string;
    //         date: Date;
    //     }[];
    // }

    const updates = [
        {
            id: 1,
            title: 'Patch Notes v1.01',
            content: `Added a new music track
                Implemented bet size customization that - filler that mimicks a two line input - * - filler ends up going onto the next line and expanding the line to the left.
                Fixed bug that hid fold button in all-in situations`,
            type: 'text',
            optionalText: 'Optional text',
            date: Date.now(),
        },
        {
            id: 2,
            title: 'Poll: What should our next feature be?',
            content: {
                options: [
                    { title: 'Each of these bubbles are interactive', votes: 50 },
                    { title: 'Voting choice', votes: 20 },
                    { title: 'Voting choice', votes: 150 }
                ]
            },
            optionalText: 'Optional text below the poll. If this text is added, then it is 12px below the poll question.',
            type: 'poll',
            date: Date.now(),
        },
        {
            id: 3,
            title: 'Patch Notes v1.01',
            content: `Added a new music track
                Implemented bet size customization that - filler that mimicks a two line input - * - filler ends up going onto the next line and expanding the line to the left.
                Fixed bug that hid fold button in all-in situations`,
            type: 'text',
            optionalText: 'Optional text',
            date: Date.now(),
        },
        {
            id: 4,
            title: 'Poll: What should our next feature be?',
            content: {
                options: [
                    { title: 'Each of these bubbles are interactive', votes: 50 },
                    { title: 'Voting choice', votes: 20 },
                    { title: 'Voting choice', votes: 150 }
                ]
            },
            optionalText: 'Optional text below the poll. If this text is added, then it is 12px below the poll question.',
            type: 'poll',
            date: Date.now(),
        }
    ];

    return {
        updates
    };
};

export const votePollRequest = async (payload) => {
    // payload: {
    //     updateId: number;
    //     id: number;
    // }

    await wait(4000);

    return {
    };
};