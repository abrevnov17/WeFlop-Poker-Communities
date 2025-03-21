import styled from 'styled-components';

interface LineProps {
    size: 'small' | 'big';
    color: 'primary' | 'secondary' | 'initial' | string;
    type: 'buy-in' | 'empty' | 'player';
}

const Line = styled('div')<LineProps>`
    background: ${({ theme, color, type }) =>
        type === 'empty' ? 'unset' : theme.palette[color] ? theme.palette[color] : theme.palette.common[color]};
    height: 100%;
    width: ${({ size }) => (size === 'small' ? '0.6rem' : '0.8rem')};
    border-radius: 0.1rem;
    border: ${({ type, theme }) => (type === 'empty' ? `0.1rem solid ${theme.palette.initial}` : 'unset')};
    transition: background 0.4s ease-in;
`;

export default Line;
