import React from 'react';
import styled from 'styled-components';
import { Rectangle, Line, Typography } from '../';

interface BaseSecondaryButtonProps {
    size: 'small' | 'large';
}

// bottom: ${({ variant }) => (variant === 'auth' ? '-9.6rem' : 'unset')};
// margin: ${({ variant }) => (variant === 'auth' ? '0 -2.5rem 0 -2.5rem' : '4.8rem 0 0 0')};
// position: ${({ variant }) => (variant === 'auth' ? 'absolute' : 'unset')};
// min-width: ${({ variant }) => (variant === 'auth' ? '35.6rem' : '23.8rem')};

const BaseSecondaryButton = styled('button')<BaseSecondaryButtonProps>`
    display: flex;
    align-items: center;
    width: inherit;
    height: ${({ size }) => (size === 'large' ? '4.8rem' : '2.4rem')};
    background: transparent;
    border: none;
    outline: none;
    cursor: pointer;
    justify-content: space-between;
    padding: 0;

    .line-button {
        margin: 0 0 0 0.8rem;
    }

    .line-button-last {
        margin: 0 0.8rem 0 0;
    }
    .lines-wrapper {
        display: flex;
        align-items: center;
        height: inherit;
    }
`;

interface SecondaryButtonProps {
    title: string;
    validated?: boolean;
    size: 'small' | 'large';
    className?: string;
    onClick: any;
    isActionCompleted?: boolean;
    actionCompletedColor?: string;
    animated?: boolean;
}

const Secondary = ({
    title,
    validated,
    size,
    className,
    isActionCompleted,
    actionCompletedColor,
    onClick,
    animated,
}: SecondaryButtonProps) => {
    const color = validated ? 'success' : 'primary';
    return (
        <BaseSecondaryButton size={size} className={className} type="submit" onClick={onClick}>
            <div className="lines-wrapper">
                <Rectangle
                    width={size === 'large' ? 'large' : 'extrasmall'}
                    height={size === 'large' ? 'large' : 'extrasmall'}
                    color={color}
                    border={size}
                />
                <Line
                    width={size === 'large' ? 'large' : 'small'}
                    className="line-button"
                    color={isActionCompleted ? (actionCompletedColor ? actionCompletedColor : 'yellow') : undefined}
                />
            </div>
            <div style={isActionCompleted ? { opacity: 0 } : undefined}>
                <Typography
                    component="span"
                    variant={size === 'large' ? 'button2' : 'h6'}
                    fontWeight={600}
                    textTransform="uppercase"
                    animated={animated}
                >
                    {title}
                </Typography>
            </div>
            <div className="lines-wrapper">
                <Line
                    width={size === 'large' ? 'large' : 'small'}
                    className="line-button line-button-last"
                    color={isActionCompleted ? (actionCompletedColor ? actionCompletedColor : 'yellow') : undefined}
                />
                <Rectangle
                    width={size === 'large' ? 'large' : 'extrasmall'}
                    height={size === 'large' ? 'large' : 'extrasmall'}
                    color={color}
                    border={size}
                />
            </div>
        </BaseSecondaryButton>
    );
};

export default Secondary;
