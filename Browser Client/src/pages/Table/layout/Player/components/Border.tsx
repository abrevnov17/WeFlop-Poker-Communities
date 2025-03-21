import styled from 'styled-components';

interface BorderProps {
    color: 'primary' | 'secondary' | 'initial' | string;
    align: 'left' | 'right';
}


const Border = styled('div')<BorderProps>`
    background: ${({ theme, color }) => (theme.palette[color] ? theme.palette[color] : theme.palette.common[color])};
    height: 100%;
    width: 0.8rem;
    border-radius: ${({ align }) => (align === 'left' ? '0.4rem 0 0 0.4rem' : '0 0.4rem 0.4rem 0')};
`;

export default Border;