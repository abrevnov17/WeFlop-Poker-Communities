import React from 'react';
import { Typography } from '../';
import styled from 'styled-components';
import { animated } from 'react-spring';

interface ButtonPlayBase {
    title?: string;
    active?: boolean;
    align: 'left' | 'right';
    onClick?: any;
    activeColor?: string;
}

const ButtonPlayBase = styled.div<ButtonPlayBase>`
    display: flex;
    cursor: pointer;
    .line {
        display: block;
        width: 1.6rem;
        height: ${({ active }) => (active ? '4.8rem' : '1.6rem')};
        border-radius: 0.4rem;
        background-color: ${({ theme, active, activeColor }) =>
            active ? (activeColor ? theme.palette[activeColor] : theme.palette.yellow) : theme.palette.initial};
        margin-bottom: 0.3rem;
        margin: ${({ align }) => (align === 'left' ? '1rem 0.8rem 0 0' : '1rem 0 0 0.8rem')};
        transition: height 0.2s ease-in;
    }

    span {
        color: ${({ theme, active, activeColor }) =>
            active ? (activeColor ? theme.palette[activeColor] : theme.palette.yellow) : theme.palette.initial};
    }
`;

interface ButtonPlayProps {
    title?: string;
    active?: boolean;
    align: 'left' | 'right';
    onClick?: any;
    activeColor?: string;
    textStyle?: Record<string, unknown>;
    animated?: boolean;
}

const ButtonPlay = ({ title, active, align, onClick, activeColor, textStyle, animated }: ButtonPlayProps) => {
    return (
        <ButtonPlayBase active={active} align={align} onClick={onClick} activeColor={activeColor}>
            {align === 'left' ? <span className="line" /> : null}
            <div style={textStyle}>
                <Typography variant="button3" textTransform="uppercase" component="span" animated={animated}>
                    {title}
                </Typography>
            </div>
            {align === 'right' ? <span className="line" /> : null}
        </ButtonPlayBase>
    );
};

export default ButtonPlay;
