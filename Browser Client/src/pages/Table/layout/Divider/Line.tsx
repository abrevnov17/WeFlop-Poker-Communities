import styled from 'styled-components';

interface LineProps {
    type: 'horizontal' | 'vertical';
    lineBottom?: boolean;
    lineTop?: boolean;
}

const Line = styled.div<LineProps>`
    width: ${({ type }) => (type === 'vertical' ? '1.6rem' : '2.4rem')};
    height: ${({ type }) => (type === 'vertical' ? '2.4rem' : '1.6rem')};
    border-radius: 0.2rem;
    background-color: ${({ theme }) => theme.palette.secondary};
    margin: ${({ lineTop, lineBottom }) =>
        lineTop && lineBottom ? 0 : lineBottom ? '0 0 2.4rem 0' : lineTop ? '2.4rem 0 0 0' : 0};
`;

export default Line;