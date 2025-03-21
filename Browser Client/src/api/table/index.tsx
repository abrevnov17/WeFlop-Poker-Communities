// import axios from "axios";

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export const getTablesRequest = async (payload) => {
    /*try {
		const result = await axios.post("/signup", payload);
		return result.data;
	} catch (e) {
		throw Error(e);
	}*/

    const tables = {
        active: [
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 1,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            },
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 2,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            },
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 3,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            },
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 4,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            }
        ],
        archive: [
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 5,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            },
            {
                name: 'Test',
                createdBy: 'username1',
                dateCreated: 1596569105,
                gameNumber: 6,
                type: 'member',
                online: 3,
                max: 9,
                smallBlind: 0.5,
                bigBlind: 1,
                variant: 'TEST',
                turnDuration: 30,
                minBuyIn: 100,
                maxBuyIn: 300,
                ledger: {
                    john: 1.3,
                    mark: -50,
                    peter: 1488,
                    matt: -5,
                },
            }
        ]
    }

    return {
        tables: {
            [payload.type]: tables[payload.type]
        }
    };
};

export const createTableRequest = async (payload) => {
    /*try {
        const result = await axios.post("/signup", payload);
        return result.data;
    } catch (e) {
        throw Error(e);
    }*/

    await wait(4000);

    return {
    };
};