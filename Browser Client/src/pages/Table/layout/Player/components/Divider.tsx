import styled from 'styled-components';

interface DividerProps {
    size: 'small' | 'big';
    color: 'primary' | 'secondary' | 'initial' | string;
}

const Divider = styled('div')<DividerProps>`
    background: ${({ theme, color }) => (theme.palette[color] ? theme.palette[color] : theme.palette.common[color])};
    height: ${({ size }) => (size === 'small' ? '0.1rem' : '0.2rem')};
    margin: ${({ size }) => (size === 'small' ? '0' : '0 0.2rem')};
    border-radius: 0.1rem;
`;

export default Divider;
