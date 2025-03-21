import React, { useState } from 'react';
import { getTables, GET_TABLES } from '../../../store/actions/application';
import { Button, Line, SlideDown, Rectangle } from '../../../components';
import { TableItem, Subtitle } from '../../../layout';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../../store';
import styled from 'styled-components';

interface TabsProps {
    form: JSX.Element;
    className?: string;
    isCreated: boolean;
}

const Tabs = ({ form, className, isCreated }: TabsProps) => {
    const opacity = isCreated ? { opacity: '0' } : { opacity: '1' };

    const removeGame = (id) => {
        // setData((prev) => data.filter((el) => el.game_number !== id));
    };
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);
    const [active, setActive] = useState({
        create: false,
        tables: false,
        archive: false,
        active: false,
    });

    const activeStateHandler = (first, second) => {
        setActive((prev) => {
            if (active[second]) {
                return {
                    ...prev,
                    [second]: false,
                    [first]: !active[first],
                };
            }
            return {
                ...prev,
                [first]: !active[first],
            };
        });
    };

    const buttonHandler = (title) => {
        if (!isCreated) {
            switch (title) {
                case 'archive':
                    !active[title] && dispatch(getTables({ type: title }));
                    activeStateHandler('archive', 'active');
                    break;
                case 'active':
                    !active[title] && dispatch(getTables({ type: title }));
                    activeStateHandler('active', 'archive');
                    break;
                case 'tables':
                    activeStateHandler('tables', 'create');
                    break;
                case 'create':
                    activeStateHandler('create', 'tables');
                    break;
            }
        }
    };
    return (
        <div className={className}>
            <div className="button-wrapper-play">
                <div className="left-button-wrapper">
                    <Button
                        variant="play"
                        title="create"
                        active={active.create}
                        align="right"
                        onClick={() => buttonHandler('create')}
                        textStyle={opacity}
                        animated
                    />
                </div>
                <Rectangle height="extralarge" width="extralarge" className="rect-play-wrapper" />
                <div className="right-button-wrapper">
                    <Button
                        variant="play"
                        title="tables"
                        active={active.tables}
                        align="left"
                        onClick={() => buttonHandler('tables')}
                        animated
                    />
                </div>
            </div>
            <SlideDown open={active.create}>
                <div className="create-table-slidedown">
                    <Subtitle className="subtitle-create" color="secondary" style={opacity}>
                        More customizability coming soon. Let us know what you want!
                    </Subtitle>
                    <Line color="yellow" width="large" height="short" align="left" />
                    {form}
                </div>
            </SlideDown>
            <SlideDown open={active.tables}>
                <div className="buttons-bottom-play">
                    <div className="tables-subtitles-wrapper">
                        <Subtitle color="secondary">Active tables are currently playable games.</Subtitle>
                        <Subtitle color="secondary">
                            When a game is over, the creator of a game can “archive” it.
                        </Subtitle>
                        <Subtitle color="secondary">
                            Archived games are unplayable, but you can still access their ledgers.
                        </Subtitle>
                        <Subtitle color="secondary">
                            “HIDE” lets you hide the games you don’t want to see anymore.
                        </Subtitle>
                    </div>
                </div>
                <div className="buttons-and-options-wrapper">
                    <div className="buttons-inside-wrapper">
                        <div className="left-button-wrapper bottom">
                            <Button
                                activeColor="secondary"
                                variant="play"
                                title="archive"
                                active={active.archive}
                                align="right"
                                onClick={() => buttonHandler('archive')}
                            />
                        </div>
                        <Line width="large" height="short" className="line-bottom-updates" color="yellow" />
                        <div className="right-button-wrapper bottom">
                            <Button
                                activeColor="secondary"
                                variant="play"
                                title="active"
                                active={active.active}
                                align="left"
                                onClick={() => buttonHandler('active')}
                            />
                        </div>
                    </div>
                    <SlideDown open={active.active}>
                        {application.tables?.active?.map((table) => (
                            <TableItem key={table.gameNumber} table={table} onRemove={removeGame} type="active" />
                        ))}
                        <Rectangle
                            height="small"
                            width="small"
                            border="small"
                            color="yellow"
                            className="play-rect-bottom"
                        />
                    </SlideDown>
                    <SlideDown open={active.archive}>
                        {application.tables?.archive?.map((table) => (
                            <TableItem key={table.gameNumber} table={table} onRemove={removeGame} type="archive" />
                        ))}
                        <Rectangle
                            height="small"
                            width="small"
                            border="small"
                            color="yellow"
                            className="play-rect-bottom play-rect-bottom-left"
                        />
                    </SlideDown>
                </div>
            </SlideDown>
        </div>
    );
};

export default styled(Tabs)`
    display: flex;
    margin-top: 0.8rem;
    flex-direction: column;

    .left-button-wrapper {
        margin-right: 0.8rem;
        height: 5.8rem;
    }

    .rect-play-wrapper {
        height: 4rem;
    }

    .right-button-wrapper {
        margin-left: 0.8rem;
        height: 5.8rem;
    }

    .left-button-wrapper.bottom {
        margin-top: 2.2rem;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
    }

    .right-button-wrapper.bottom {
        margin-top: 2.2rem;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        align-self: flex-start;
    }

    .tables-subtitles-wrapper {
        margin: 1rem 0 1rem 0;
    }

    .buttons-inside-wrapper {
        display: flex;
        align-content: flex-start;
    }

    .buttons-and-options-wrapper {
        margin-left: 3rem;
    }

    .button-wrapper-play {
        display: flex;
    }
    .buttons-bottom-play {
        display: flex;
        flex-direction: column;
        margin-left: 20rem;
    }
    .play-rect-bottom {
        align-self: flex-end;
        margin: 0.8rem 0 0.8rem 19.7rem;
    }

    .play-rect-bottom-left {
        margin: 0.8rem 0 0.8rem 14.9rem;
    }

    .create-table-slidedown {
        margin-left: 12.6rem;
    }
`;
