import React from 'react';
import Input from './Input';
import TextArea from './TextArea';

interface InputProps {
    size?: 'large' | 'medium' | 'small';
    className?: string;
    placeholder?: string;
    type?: string;
    validated?: boolean;
    errorMessage?: string;
    register?: any;
    name?: string;
    disabled?: boolean;
    defaultValue?: string | number;
    onFocus?: any;
    variant?: 'input' | 'textarea';
    width?: string;
    tooltipAlign?: 'left' | 'right';
    autoresize?: boolean;
    rightLine?: boolean;
    onKeyDown?: any;
    onChange?: any;
    style?: Record<string, any>;
    animated?: boolean;
}

const TextField = ({
    animated,
    size,
    placeholder,
    validated,
    type,
    name,
    errorMessage,
    register,
    disabled,
    defaultValue,
    onFocus,
    variant,
    width,
    tooltipAlign,
    className,
    autoresize,
    rightLine,
    onKeyDown,
    onChange,
    style,
}: InputProps) => {
    switch (variant) {
        case 'input':
            return (
                <Input
                    className={className}
                    size={size!}
                    width={width}
                    placeholder={placeholder}
                    validated={validated!}
                    type={type}
                    name={name}
                    errorMessage={errorMessage}
                    register={register!}
                    disabled={disabled}
                    defaultValue={defaultValue}
                    onFocus={onFocus}
                    tooltipAlign={tooltipAlign!}
                    onChange={onChange}
                    style={style}
                    animated={animated}
                />
            );
        case 'textarea':
            return (
                <TextArea
                    validated={validated}
                    onKeyDown={onKeyDown}
                    className={className}
                    size={size!}
                    width={width}
                    placeholder={placeholder}
                    name={name}
                    register={register!}
                    disabled={disabled}
                    defaultValue={defaultValue}
                    onFocus={onFocus}
                    autoresize={autoresize}
                    rightLine={rightLine}
                    style={style}
                    animated={animated}
                />
            );
        default:
            return null;
    }
};

TextField.defaultProps = {
    validated: false,
    variant: 'input',
    tooltipAlign: 'right',
    size: 'large',
};

export default TextField;
