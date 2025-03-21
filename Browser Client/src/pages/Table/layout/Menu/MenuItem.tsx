import React from 'react';
import { Divider } from '../';
import styled from 'styled-components';

interface MenuItemProps {
    children: JSX.Element[] | JSX.Element;
    top?: boolean;
    bottom?: boolean;
    left?: boolean;
    right?: boolean;
    className?: string;
}

interface MenuItemBaseProps {
    top?: boolean;
    bottom?: boolean;
    left?: boolean;
    right?: boolean;
}

const MenuItemBase = styled.div<MenuItemBaseProps>`
    flex-direction: column;
    align-items: ${({ left }) => (left ? 'flex-start' : 'flex-end')};
    display: flex;
    & > * + * {
        margin-top: 0.6rem;
    }
`;

const MenuItem = ({ children, top, bottom, left, right }: MenuItemProps) => {
    return (
        <>
            {bottom && <Divider lineRight={right} lineLeft={left} />}
            <MenuItemBase top={top} bottom={bottom} left={left} right={right}>
                {children}
            </MenuItemBase>
            {top && <Divider lineRight={right} lineLeft={left} />}
        </>
    );
};

export default MenuItem;
