const checkValidation = (current, error) => {
    if (current === '' && !error) {
        return undefined;
    } else {
        if (!(current = '') && error) {
            return false;
        } else {
            if (!(current = '') && !error) {
                return true;
            }
        }
    }
};

export default checkValidation;
