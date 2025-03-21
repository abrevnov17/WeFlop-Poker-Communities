import React from 'react';
import styled from 'styled-components';

interface ContentProps {
    children: JSX.Element[] | JSX.Element;
    className?: string;
}

const Content = ({ children, className }: ContentProps) => {
    return <div className={className}>{children}</div>;
};

export default styled(Content)`
    flex: 7;
`;
