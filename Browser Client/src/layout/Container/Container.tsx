import React from 'react';
import styled from 'styled-components';

interface ContainerProps {
    children: JSX.Element | JSX.Element[] | null;
    className?: string;
    type?: 'table' | 'initial' | 'modal';
}

interface BaseContainerProps {
    type: 'table' | 'initial' | 'modal';
}

const ModalContainer = styled('div')`
    width: 100%;
    height: 100%;
    position: absolute;
    background-color: rgba(3, 24, 45, 0.8);
    top: 0;
    left: 0;
    z-index: 5;
    display: flex;
    justify-content: center;
`;

const BaseContainer = styled('div')<BaseContainerProps>`
    height: 100vh;
    padding: ${({ type }) => (type === 'initial' ? '6rem' : '10rem 10rem 0 10rem')};
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
`;

const Container = ({ children, className, type }: ContainerProps) => {
    switch (type) {
        case 'table' || 'initial':
            return (
                <BaseContainer className={className} type={type!}>
                    {children}
                </BaseContainer>
            );
        case 'modal':
            return <ModalContainer className={className}>{children}</ModalContainer>;
        default:
            return (
                <BaseContainer className={className} type={type!}>
                    {children}
                </BaseContainer>
            );
    }
};

Container.defaultProps = {
    type: 'initial',
};

export default Container;
