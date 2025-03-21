import styled from 'styled-components';
import React from 'react';

interface BaseRectangleProps {
    width: 'extrasmall' | 'small' | 'medium' | 'large' | 'extralarge' | 'initial' | string;
    height: 'extrasmall' | 'small' | 'medium' | 'large' | 'extralarge' | 'initial' | string;
    color: 'primary' | 'secondary' | 'success' | string;
    border?: 'small' | 'large';
}

const BaseRectangle = styled.span<BaseRectangleProps>`
    border-radius: ${({ border }) => (border === 'large' ? '0.4rem' : '0.2rem')};
    background-color: ${({ theme, color }) =>
        theme.palette[color!] ? theme.palette[color!] : theme.palette.common[color!]};
    display: block;
    width: ${({ width }) =>
        width === 'extrasmall'
            ? '0.8rem'
            : width === 'small'
            ? '1rem'
            : width === 'medium'
            ? '1.2rem'
            : width === 'large'
            ? '1.6rem'
            : width === 'extralarge'
            ? '4rem'
            : width === 'initial'
            ? '100%'
            : width};
    height: ${({ height }) =>
        height === 'extrasmall'
            ? '0.8rem'
            : height === 'small'
            ? '1rem'
            : height === 'medium'
            ? '1.2rem'
            : height === 'large'
            ? '1.6rem'
            : height === 'extralarge'
            ? '4rem'
            : height === 'initial'
            ? '100%'
            : height};
    transition: background-color 0.4s ease-in-out;
`;

interface RectangleProps {
    width?: 'extrasmall' | 'small' | 'medium' | 'large' | 'extralarge' | 'initial' | string;
    height?: 'extrasmall' | 'small' | 'medium' | 'large' | 'extralarge' | 'initial' | string;
    color?: 'primary' | 'secondary' | 'success' | string;
    border?: 'small' | 'large';
    className?: string;
    onClick?: () => void;
}

const Rectangle = ({ color, width, height, border, className, onClick }: RectangleProps) => {
    return (
        <div className={`rectangle-wrapper ${className ? className : ''}`} onClick={onClick}>
            <BaseRectangle width={width!} height={height!} color={color!} border={border} />
        </div>
    );
};

Rectangle.defaultProps = {
    color: 'secondary',
    border: 'large',
    width: 'initial',
    height: 'initial',
};

export default Rectangle;
