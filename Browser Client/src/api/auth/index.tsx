// import axios from "axios";
import moment from 'moment';

const wait = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

export const signUpRequest = async (payload) => {
    /*try {
		const result = await axios.post("/signup", payload);
		return result.data;
	} catch (e) {
		throw Error(e);
	}*/

    // await wait(4000);

    return {
        username: payload.name,
        email: payload.email
    };
};

export const signInRequest = async (payload) => {
    /*try {
        const result = await axios.post("/signup", payload);
        return result.data;
    } catch (e) {
        throw Error(e);
    }*/

    // await wait(4000);

    return {
        token: "testtoken",
        expire: moment().add(2, 'days').toDate(),
        username: 'test',
        email: payload.email
    };
};

export const resetPasswordRequest = async (payload) => {
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