import React, { useState } from 'react';
import { Line, Typography, SlideDown, Rectangle } from '../../components';
import { Poll, Description } from '../';
import styled from 'styled-components';
import moment from 'moment';

interface UpdateItemProps {
    element?: any;
    className?: string;
}

const UpdateItem = ({ element, className }: UpdateItemProps) => {
    const [open, setOpen] = useState<boolean>(false);

    const list =
        element.type === 'poll' ? (
            <div style={{ marginTop: '0.8rem' }}>
                <Poll content={element.content} updateId={element.id} optional={element.optionalText} />
            </div>
        ) : element.type === 'text' ? (
            <Description content={element.content} />
        ) : null;

    return (
        <div className={className}>
            <div className="updates-note-item" onClick={() => setOpen((prev) => !prev)}>
                <div className="updates-note-item-date-wrapper">
                    <Typography variant="date" component="span" animated>
                        {moment(element.date).format('MM.DD.YYYY')}
                    </Typography>
                    <Typography variant="date" component="span" animated>
                        {moment(element.date).format('HH:mm')}
                    </Typography>
                </div>
                <Line className="updates-note-item-line" width="large" color={open ? 'yellow' : 'primary'} />
                <div className="updates-note-item-text-wrapper">
                    <Typography variant="button2" component="a" animated>
                        {element.title}
                    </Typography>
                </div>
            </div>
            <SlideDown open={open}>{list}</SlideDown>
            <div className="rectangle-wrapper-item">
                <Rectangle height="extralarge" width="extralarge" />
            </div>
        </div>
    );
};

export default styled(UpdateItem)`
    .updates-note-item {
        height: 4.8rem;
        cursor: pointer;
        display: flex;
        align-items: center;
        margin: 0.8rem 0 0 -9.8rem;
        &-line {
            height: 4.8rem;
        }
        &-wrapper {
            display: flex;
            flex-direction: row;
        }
        &-text-wrapper {
            margin-left: 1.1rem;
        }
        &-date-wrapper {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            margin-right: 1.1rem;
        }
    }
    .rectangle-wrapper-item {
        margin-top: 0.8rem;
    }
`;
