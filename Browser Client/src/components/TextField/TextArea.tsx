import React, { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';

interface BaseTextAreaProps {
    width?: string;
    size: 'large' | 'medium' | 'small';
    rightLine: boolean;
    validated?: boolean;
    animated?: boolean;
}

const TextAreaBase = styled('div')<BaseTextAreaProps>`
    .wrapper {
        animation: ${({ theme, animated }) => (animated ? theme.animations.text : 'none')};
        opacity: ${({ animated }) => (animated ? 0 : 1)};
        position: relative;
        width: 100%;
        height: 100%;
        max-width: ${({ width }) => (width ? width : null)};
    }

    .textarea {
        font-size: ${({ theme }) => theme.typography.input1!.fontSize};
        font-family: ${({ theme }) => theme.typography.fontFamily};
        color: ${({ theme, validated }) => (validated ? theme.palette.yellow : theme.palette.primary)};
        letter-spacing: ${({ theme }) => theme.typography.input1?.letterSpacing};
        position: relative;
        width: ${({ width }) => (width ? width : '29.1rem')};
        height: 100%;
        background: transparent;
        border-width: 0;
        border-radius: 0.4rem;
        padding: ${({ size }) => (size === 'large' ? '0 1rem' : size === 'medium' ? '0 0.8rem' : '0 0.4rem')};
        outline: none;
        margin: 0 0.8rem 0 0.8rem;
        resize: none;
        transition: color 0.4s ease-in;
    }

    .wrapper::after,
    .wrapper::before {
        background-color: ${({ theme, validated }) => (validated ? theme.palette.yellow : theme.palette.primary)};
        transition: background-color 0.4s ease-in;
        content: '';
        display: block;
        position: absolute;
        top: 0;
        right: 0;
        width: ${({ size }) => (size === 'large' ? '1rem' : size === 'medium' ? '0.8rem' : '0.4rem')};
        height: 100%;
        border-radius: ${({ size }) => (size === 'large' || size === 'medium' ? '0.2rem' : '0.1rem')};
        margin-left: 0.3rem;
    }

    .wrapper::before {
        left: 0;
    }

    .wrapper::after {
        display: ${({ rightLine }) => (rightLine ? 'block' : 'none')};
        margin: 0 0.3rem 0 0;
    }

    .input::placeholder {
        color: ${({ theme }) => theme.palette.primary};
        text-transform: uppercase;
    }
`;

interface TextAreaProps {
    width?: string;
    size: 'large' | 'medium' | 'small';
    className?: string;
    placeholder?: string;
    register?: (ref: any) => void;
    name?: string;
    disabled?: boolean;
    defaultValue?: string | number;
    onFocus?: any;
    autoresize?: boolean;
    rightLine?: boolean;
    onKeyDown?: () => void;
    validated?: boolean;
    style?: Record<string, undefined>;
    animated?: boolean;
}

const TextArea = ({
    placeholder,
    name,
    register,
    disabled,
    defaultValue,
    onFocus,
    className,
    width,
    autoresize,
    size,
    rightLine,
    onKeyDown,
    validated,
    style,
    animated,
}: TextAreaProps) => {
    const [height, setHeight] = useState(autoresize ? 'auto' : '17.6rem');
    const [text, setText] = useState('');
    const textareaRef = useRef<HTMLTextAreaElement | null>(null);

    useEffect(() => {
        if (textareaRef!.current!.scrollHeight <= 200) {
            setHeight(`${textareaRef!.current!.scrollHeight}px`);
        } else {
            setHeight('200px');
        }
    }, [text]);

    const onChangeHandler = (event) => {
        setHeight('auto');
        setText(event.target.value);
    };

    return (
        <TextAreaBase
            className={className}
            width={width}
            size={size}
            rightLine={rightLine!}
            validated={validated}
            animated={animated}
        >
            <div className="wrapper">
                <textarea
                    onKeyDown={onKeyDown}
                    onFocus={onFocus}
                    defaultValue={defaultValue}
                    className="textarea"
                    name={name}
                    ref={(ref) => {
                        register!(ref);
                        textareaRef.current = ref;
                    }}
                    placeholder={placeholder}
                    disabled={disabled}
                    onChange={(event) => autoresize && onChangeHandler(event)}
                    style={{ height, ...style }}
                />
            </div>
        </TextAreaBase>
    );
};

TextArea.defaultProps = {
    register: () => undefined,
    autoresize: false,
    rightLine: true,
};

export default TextArea;
