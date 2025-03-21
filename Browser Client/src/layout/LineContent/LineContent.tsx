import React from 'react';
import { Line } from '../../components';
import styled from 'styled-components';

interface LineContentProps {
    children: JSX.Element[];
    className?: string;
}

const LineContent = ({ children, className }: LineContentProps) => {
    return (
        <div className={className}>
            <Line color="secondary" width="large" align="right" animated />
            <div className="title-wrapper-text">{children}</div>
        </div>
    );
};

export default styled(LineContent)<LineContentProps>`
    display: flex;
    flex-direction: row;
    .lines-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    .title-wrapper-text {
        margin-left: 0.8rem;
    }

    .subtitles-wrapper-inner {
        margin: 1rem 0 1rem 0;
    }
`;
