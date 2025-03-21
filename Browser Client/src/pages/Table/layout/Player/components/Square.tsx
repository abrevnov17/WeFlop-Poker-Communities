import styled from 'styled-components';

const Square = styled('div')`
    background: ${({ theme }) => theme.palette.secondary};
    height: 0.8rem;
    width: 0.8rem;
    margin: 0 1.2rem;
    border-radius: 0.2rem;
`;

export default Square;
