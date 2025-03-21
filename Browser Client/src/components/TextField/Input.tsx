import React from 'react';
import styled from 'styled-components';
import ToolTip from './ToolTip';

interface BaseInputProps {
    validated: boolean | undefined;
    width?: string;
    size: 'large' | 'medium' | 'small';
    animated?: boolean;
}

const BaseInput = styled('div')<BaseInputProps>`
    .wrapper {
        animation: ${({ theme, animated }) => (animated ? theme.animations.text : 'none')};
        opacity: ${({ theme, animated }) => (animated ? 0 : 1)};
        position: relative;
        width: 100%;
        max-width: ${({ width }) => (width ? width : null)};
    }

    .input {
        text-align: center;
        font-size: ${({ theme, size }) =>
            size === 'large' ? theme.typography.input1!.fontSize : theme.typography.input3!.fontSize};
        font-family: ${({ theme }) => theme.typography.fontFamily};
        color: ${({ theme, validated, size }) =>
            validated === true && size === 'large' ? theme.palette.success : theme.palette.primary};
        letter-spacing: ${({ theme, size }) =>
            size === 'large' ? theme.typography.input1!.letterSpacing : theme.typography.input3!.letterSpacing};
        position: relative;
        width: ${({ width }) => (width ? width : '30.5rem')};
        background: transparent;
        border-width: 0;
        padding: ${({ size }) => (size === 'large' ? '0 1rem' : size === 'medium' ? '0 0.8rem' : '0 0.4rem')};
        outline: none;
        transition: all 0.4s ease-in-out;
        height: ${({ size }) => (size === 'large' ? '4.8rem' : size === 'medium' ? '3.2rem' : '1.6re,')};
    }

    .input::placeholder {
        text-transform: uppercase;
        color: ${({ theme }) => theme.palette.primary};
    }

    .wrapper::after,
    .wrapper::before {
        background-color: ${({ theme, validated, size }) =>
            validated === true
                ? size === 'large'
                    ? theme.palette.success
                    : theme.palette.secondary
                : validated === false
                ? theme.palette.yellow
                : validated === undefined
                ? theme.palette.primary
                : theme.palette.primary};
        content: '';
        display: block;
        position: absolute;
        top: 0;
        right: 0;
        width: ${({ size }) => (size === 'large' ? '1rem' : size === 'medium' ? '0.8rem' : '0.4rem')};
        height: 100%;
        border-radius: ${({ size }) => (size === 'large' || size === 'medium' ? '0.2rem' : '0.1rem')};
        transition: all 0.4s ease-in-out;
    }

    .wrapper::after {
        margin-right: 0.3rem;
    }

    .wrapper::before {
        left: 0;
        margin-left: 0.3rem;
    }
`;

interface InputProps {
    animated?: boolean;
    className?: string;
    size: 'large' | 'medium' | 'small';
    placeholder?: string;
    type: string;
    validated: boolean;
    errorMessage?: string;
    register?: React.Ref<HTMLInputElement>;
    name?: string;
    disabled?: boolean;
    defaultValue?: string | number;
    onFocus?: any;
    width?: string;
    onChange?: any;
    tooltipAlign: 'left' | 'right';
    style?: Record<string, unknown>;
}

const Primary = ({
    placeholder,
    validated,
    type,
    name,
    errorMessage,
    register,
    disabled,
    defaultValue,
    onFocus,
    width,
    tooltipAlign,
    size,
    className,
    onChange,
    style,
    animated,
}: InputProps) => {
    const show = !validated && errorMessage !== undefined;
    const isValidated = errorMessage ? false : validated ? true : undefined;
    return (
        <BaseInput validated={isValidated} width={width} size={size} className={className} animated={animated}>
            <div className="wrapper">
                <ToolTip message={errorMessage!} name={name} show={show} align={tooltipAlign} />
                <input
                    onFocus={onFocus}
                    defaultValue={defaultValue}
                    className="input"
                    onChange={onChange}
                    name={name}
                    ref={register}
                    type={type}
                    placeholder={placeholder}
                    disabled={disabled}
                    style={style}
                />
            </div>
        </BaseInput>
    );
};

Primary.defaultProps = {
    type: 'text',
};

export default Primary;
