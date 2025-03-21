import styled from 'styled-components';
import { animated } from 'react-spring';

interface TimerLineProps {
    lastAction?: {
        type: 'bet' | 'call' | 'raise' | 'sit-in' | 'sit-out' | 'active' | 'finished-round' | 'win' | 'lose';
        params?: Record<string, unknown>;
    };
}

const TimerLine = styled(animated.div)<TimerLineProps>`
    width: 100%;
    background: ${({ theme, lastAction }) =>
        lastAction ? (lastAction.type === 'win' ? 'transparent' : theme.palette.primary) : theme.palette.primary};
    height: 0.6rem;
    margin: 0.2rem 0;
    transition: background 0.4s ease-in;
    border-radius: 0.1rem;
`;

export default TimerLine;
