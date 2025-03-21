const suits = ['H', 'D', 'C', 'S'];

const isCard = (variant) => {
    if (!variant) return false;
    if (!suits.includes(variant[0])) return false;
    const value = parseInt(variant.substring(1));
    if (value < 0 || value > 13) return false;
    return true;
};

export default isCard;
