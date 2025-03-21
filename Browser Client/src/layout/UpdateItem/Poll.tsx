import React, { useState } from 'react';
import styled from 'styled-components';
import { useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { votePoll } from '../../store/actions/application';

import { Description } from '../';
import { Typography } from '../../components';

interface PollItemProps {
    active?: boolean;
}

const PollItem = styled.div<PollItemProps>`
    margin-left: 1.2rem;
    display: flex;
    align-items: center;
    cursor: pointer;

    & + & {
        margin-top: 0.2rem;
    }

    .poll-vote-icon {
        height: 1.6rem;
        width: 1.6rem;
        display: block;
        background-color: ${({ active, theme }) => (active ? theme.palette.secondary : theme.palette.primary)};
        border-radius: 0.8rem;
    }
    .poll-vote-text {
        margin-left: 0.9rem;
    }
`;

const Poll = ({ content, optional, updateId }) => {
    const dispatch = useDispatch();
    const [active, setActive] = useState<number | undefined>(content.selected ? content.selected : undefined);

    const optionalText =
        optional !== undefined ? (
            <div style={{ marginTop: '0.4rem' }}>
                <Description content={optional} />
            </div>
        ) : null;

    const onClickHandler = (id) => {
        setActive(id);
        dispatch(votePoll({ updateId, id }));
    };

    const totalVotes = () => {
        return content.options.reduce((accumulator, currentValue) => accumulator + currentValue.votes, 0);
    };

    return (
        <div>
            {content.options.map((element, index) => {
                const votes = active !== undefined ? ` (${Math.round((element.votes / totalVotes()) * 100)}%)` : null;
                return (
                    <PollItem
                        key={`${element.title}${index}`}
                        active={index === active ? true : false}
                        onClick={() => onClickHandler(index)}
                    >
                        <span className="poll-vote-icon" />
                        <div className="poll-vote-text">
                            <Typography
                                component="a"
                                variant="body1"
                                color={index === active ? 'secondary' : 'primary'}
                            >
                                {element.title}
                                {votes}
                            </Typography>
                        </div>
                    </PollItem>
                );
            })}
            {optionalText}
        </div>
    );
};

export default Poll;
