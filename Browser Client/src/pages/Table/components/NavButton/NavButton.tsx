import React from 'react';
import styled from 'styled-components';
import { Typography, Rectangle } from '../../../../components';

interface NavButtonProps {
    align: 'left' | 'right';
    title: string;
    className?: string;
    onClick?: () => void;
    active?: boolean | undefined;
    noMargin?: boolean;
}

interface NavButtonBaseProps {
    align: 'left' | 'right';
    onClick?: () => void;
    noMargin?: boolean;
    checkBox?: boolean;
}

const NavButtonBase = styled.div<NavButtonBaseProps>`
    cursor: pointer;
    margin: ${({ align, noMargin }) => (noMargin ? 0 : align === 'left' ? '0 0 0 3.8rem' : '0 3.8rem 0 0')};
    display: flex;
    align-items: center;
    & > * + * {
        margin-left: ${({ noMargin }) => (noMargin ? '0.6rem' : '1rem')};
    }
    &:hover .rect-nav > span {
        background-color: ${({ theme }) => theme.palette.yellow};
    }
`;

const NavButton = ({ align, title, className, onClick, active, noMargin }: NavButtonProps) => {
    return (
        <NavButtonBase className={className} align={align} onClick={onClick} noMargin={noMargin}>
            {align === 'left' && (
                <Rectangle
                    className="rect-nav"
                    border="small"
                    color={active ? 'yellow' : 'initial'}
                    width="large"
                    height="2.4rem"
                />
            )}
            <Typography
                variant="h4"
                color={active ? 'yellow' : 'initial'}
                component="span"
                fontWeight={700}
                textTransform="uppercase"
            >
                {title}
            </Typography>
            {align === 'right' && (
                <Rectangle
                    className="rect-nav"
                    border="small"
                    color={active ? 'yellow' : 'initial'}
                    width="large"
                    height="2.4rem"
                />
            )}
        </NavButtonBase>
    );
};

export default NavButton;
