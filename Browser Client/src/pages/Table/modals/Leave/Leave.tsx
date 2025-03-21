import React from 'react';
import styled from 'styled-components';
import { NavButton } from '../../components';
import { Divider } from '../../layout';
import { Typography, Line } from '../../../../components';
import { Container } from '../../../../layout';

interface LeaveProps {
    className?: string;
    onLeave: () => void;
    onCancel: () => void;
}

const Leave = ({ className, onLeave, onCancel }: LeaveProps) => {
    return (
        <Container type="modal">
            <div className={className}>
                <Typography variant="h1" textTransform="uppercase" component="span" color="yellow">
                    ARE YOU SURE?
                </Typography>
                <Divider
                    leftContent={<NavButton align="right" title="no" noMargin onClick={onCancel} />}
                    rightContent={<NavButton align="left" title="yes" noMargin onClick={onLeave} />}
                    className="buttons"
                />
                <Line width="large" color="yellow" wrapperClassName="line-long" />
                <Divider margin="0.6rem" />
                <Line width="large" color="yellow" wrapperClassName="line-short" />
            </div>
        </Container>
    );
};

export default styled(Leave)`
    height: calc(50% + 4.5rem);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    align-self: flex-end;
    .line-long {
        flex: 1;
    }
    .buttons {
        margin: 5.3rem 0 0 0.5rem;
    }
    .line-short {
        height: 2.4rem !important;
    }
`;
