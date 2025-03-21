import React from 'react';
import styled from 'styled-components';

interface PetreonIconProps {
    className?: string;
}

const PatreonIcon = ({ className }: PetreonIconProps) => {
    return (
        <a href="#" className={className}>
            <span className="patreon-icon-line" />
            <span className="patreon-icon-round" />
        </a>
    );
};

export default styled(PatreonIcon)<PetreonIconProps>`
    display: flex;
    margin-top: 1.6rem;
    .patreon-icon-line {
        display: block;
        height: 5.1rem;
        width: 1rem;
        border-radius: 0.4rem;
        background-color: #fff;
    }

    .patreon-icon-round {
        margin-left: 0.5rem;
        display: block;
        height: 3.8rem;
        width: 3.8rem;
        border-radius: 50%;
        background-color: #fff;
    }
`;
