import React, { forwardRef } from 'react';
import styled from 'styled-components';
import { animated } from 'react-spring';

import { Typography } from '../../../../components';

interface BaseBalanceProps {
    size: 'small' | 'big';
    style?: any;
}

const BaseBalance = styled(animated.div)<BaseBalanceProps>`
    height: ${({ size }) => (size === 'big' ? '3rem' : '2.4rem')};
    min-width: ${({ size }) => (size === 'big' ? '9rem' : '6.6rem')};
    border-radius: ${({ size }) => (size === 'big' ? '1rem' : '0.9rem')};
    border: solid 0.1rem rgba(255, 255, 255, 0.25);
    display: flex;
    align-items: center;
    padding: 0.5rem;
    .icon-outer {
        width: ${({ size }) => (size === 'big' ? '1.8rem' : '1.2rem')};
        height: ${({ size }) => (size === 'big' ? '1.8rem' : '1.2rem')};
        border-radius: 1.2rem;
        border: ${({ theme, size }) => `solid ${size === 'big' ? '0.3rem' : '0.2rem'} ${theme.palette.yellow}`};
        padding: ${({ size }) => (size === 'big' ? '0.3rem' : '0.2rem')};
    }
    .icon-inner {
        width: ${({ size }) => (size === 'big' ? '0.6rem' : '0.4rem')};
        height: ${({ size }) => (size === 'big' ? '0.6rem' : '0.4rem')};
        border-radius: 0.9rem;
        background: ${({ theme }) => theme.palette.yellow};
    }
    .typography-balance {
        margin: ${({ size }) => (size === 'big' ? '0 0 0 0.8rem' : '0 0 0.1rem 0.5rem')};
    }
`;

interface BalanceProps {
    size: 'small' | 'big';
    value?: number;
    style?: any;
    className?: string;
}

const Balance = forwardRef(({ size, value, className, style }: BalanceProps, ref: any) => {
    return (
        <BaseBalance size={size} className={className} ref={ref} style={style}>
            <div className="icon-outer">
                <div className="icon-inner" />
            </div>
            <Typography component="span" variant={size === 'big' ? 'body1' : 'h6'} className="typography-balance">
                {value ? value.toFixed(2) : "0.00"}
            </Typography>
        </BaseBalance>
    );
});

export default Balance;
