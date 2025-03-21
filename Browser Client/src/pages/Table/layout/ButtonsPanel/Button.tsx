import React from 'react';
import styled from 'styled-components';
import { Typography } from '../../../../components';

interface BaseButtonProps {
    backgroundColor: 'background' | 'primary' | 'secondary' | 'initial' | string;
    border?: 'left' | 'right';
    size: 'small' | 'medium' | 'large';
    justifyContent: 'center' | 'flex-end' | 'flex-start';
    alignItems: 'center' | 'flex-end' | 'flex-start';
    onClick?: () => void;
}

const BaseButton = styled.div<BaseButtonProps>`
    cursor: ${({ onClick }) => (onClick ? 'pointer' : 'default')};
    width: ${({ size }) => (size === 'small' ? '4.9rem' : size === 'medium' ? '10.6rem' : '11.4rem')};
    height: ${({ size }) => (size === 'small' ? '2.6rem' : size === 'medium' ? '4.4rem' : '5.5rem')};
    display: flex;
    border-radius: ${({ border, size }) => {
        switch (size) {
            case 'small':
                return border !== undefined
                    ? border === 'left'
                        ? '1.3rem 0.4rem 0.4rem 1.3rem'
                        : '0.4rem 1.3rem 1.3rem 0.4rem'
                    : '0.4rem';
            case 'medium':
                return border !== undefined
                    ? border === 'left'
                        ? '2.5rem 0.6rem 0.6rem 2.5rem'
                        : '0.6rem 2.5rem 2.5rem 0.6rem'
                    : '0.6rem';
            case 'large':
                return border !== undefined
                    ? border === 'left'
                        ? '2.8rem 1rem 1rem 2.8rem'
                        : '1rem 2.8rem 2.8rem 1rem'
                    : '1rem';
        }
    }};
    padding: ${({ size }) => (size === 'small' ? '0.2rem' : size === 'medium' ? '0.3rem' : '0.4rem')};
    background: ${({ theme }) => theme.palette.secondary};

    .border-wrapper {
        display: flex;
        flex: 1;
        background: ${({ theme, backgroundColor }) =>
            theme.palette[backgroundColor] ? theme.palette[backgroundColor] : theme.palette.common[backgroundColor]};
        border-radius: ${({ border, size }) => {
            switch (size) {
                case 'small':
                    return border !== undefined
                        ? border === 'left'
                            ? '1.3rem 0.4rem 0.4rem 1.3rem'
                            : '0.4rem 1.3rem 1.3rem 0.4rem'
                        : '0.4rem';
                case 'medium':
                    return border !== undefined
                        ? border === 'left'
                            ? '2.5rem 0.6rem 0.6rem 2.5rem'
                            : '0.6rem 2.5rem 2.5rem 0.6rem'
                        : '0.6rem';
                case 'large':
                    return border !== undefined
                        ? border === 'left'
                            ? '2.8rem 1rem 1rem 2.8rem'
                            : '1rem 2.8rem 2.8rem 1rem'
                        : '1rem';
            }
        }};
        border: 0.1rem solid ${({ theme }) => theme.palette.background};
        padding: ${({ size }) => (size === 'small' ? '0 0.2rem 0 0.2rem' : size === 'medium' ? '0.3rem' : '0.4rem')};
        justify-content: ${({ justifyContent }) => justifyContent};
        align-items: ${({ alignItems }) => alignItems};
    }
    .button-title {
        display: flex;
        align-items: center;
    }
`;

interface ButtonProps {
    title: string | number[];
    backgroundColor?: 'background' | 'primary' | 'secondary' | 'initial' | string;
    border?: 'left' | 'right';
    size?: 'small' | 'medium' | 'large';
    justifyContent?: 'center' | 'flex-end' | 'flex-start';
    alignItems?: 'center' | 'flex-end' | 'flex-start';
    bold?: boolean | undefined;
    onClick?: () => void;
    optionalText?: string | undefined;
}

const Button = ({
    size,
    border,
    justifyContent,
    alignItems,
    title,
    backgroundColor,
    bold,
    onClick,
    optionalText,
}: ButtonProps) => {
    const variant = () => {
        switch (size) {
            case 'large':
                return bold ? 'h3' : 'h3';
            case 'medium':
                return 'h4';
            case 'small':
                return 'h6';
        }
    };

    return (
        <BaseButton
            size={size!}
            border={border}
            backgroundColor={backgroundColor!}
            justifyContent={justifyContent!}
            alignItems={alignItems!}
            onClick={onClick}
        >
            <div className="border-wrapper">
                <Typography variant={variant()} component="span" textTransform="uppercase" className="button-title">
                    {title}
                    {optionalText ? (
                        <Typography variant="h6" component="span">
                            {optionalText}
                        </Typography>
                    ) : null}
                </Typography>
            </div>
        </BaseButton>
    );
};

Button.defaultProps = {
    backgroundColor: 'secondary',
    justifyContent: 'center',
    alignItems: 'center',
    size: 'small',
};

export default Button;
