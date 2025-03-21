import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import { Typography } from '../';

interface BaseButtonProps {
    size: 'small' | 'large';
    align: 'left' | 'right';
    as: React.ElementType;
    to?: string;
    type?: 'button' | 'submit';
    active: boolean;
    bottomGutter: boolean;
    color: 'primary' | 'secondary' | 'initial' | string;
    disabled?: boolean;
}

const BaseButton = styled('div')<BaseButtonProps>`
    display: flex;
    outline: none;
    text-decoration: none;
    border: none;
    background: transparent;
    padding: 0;
    display: flex;
    align-items: center;
    text-align: ${({ align }) => align};
    align-self: ${({ align }) => (align === 'right' ? 'flex-end' : null)};
    height: ${({ size }) => (size === 'large' ? '6.4rem' : '4.8rem')};
    cursor: ${({ disabled }) => (disabled ? 'unset' : 'pointer')};
    margin-bottom: ${({ bottomGutter }) => (bottomGutter ? '6.4rem' : null)};

    .line {
        background-color: ${({ theme, color }) => theme.palette[color!]};
        ${({ theme, active }) =>
            active ? `background-color: ${active ? theme.palette.secondary : theme.palette.primary};` : null};
        margin-bottom: 0.3rem;
        border-radius: 0.4rem;
        width: 1.6rem;
        height: 1.6rem;
    }

    span {
        margin: ${({ align }) => (align === 'left' ? '0 0 0 1.6rem' : '1.5rem 1.6rem 0 0')};
    }
`;

interface PrimaryButtonProps {
    title: string | null;
    size: 'small' | 'large';
    align?: 'left' | 'right';
    onClick?: () => void;
    component?: 'button' | 'link';
    type?: 'button' | 'submit';
    to?: string;
    active?: boolean;
    bottomGutter?: boolean;
    color?: 'primary' | 'secondary' | 'initial' | string;
    disabled?: boolean;
}

const Primary = ({
    title,
    onClick,
    align,
    size,
    component,
    type,
    to,
    active,
    bottomGutter,
    color,
    disabled,
}: PrimaryButtonProps) => {
    return (
        <BaseButton
            disabled={disabled}
            as={component === 'link' ? Link : 'button'}
            type={type}
            to={to}
            onClick={onClick}
            align={align!}
            active={active!}
            bottomGutter={bottomGutter!}
            color={color!}
            size={size!}
        >
            {(align === undefined || align === 'left') && <span className="line" />}
            <Typography
                animated
                variant={size === 'large' ? 'button1' : 'button2'}
                textTransform="uppercase"
                component="span"
                color={active ? 'secondary' : color}
            >
                {title}
            </Typography>
            <span className="title"></span>
            {align && align === 'right' && <span className="line" />}
        </BaseButton>
    );
};

Primary.defaultProps = {
    active: false,
    bottomGutter: false,
    color: 'primary',
    align: 'left',
    component: 'button',
    type: 'button',
};

export default Primary;
