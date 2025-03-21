import React, { useState } from 'react';
import styled from 'styled-components';
import { useHistory, useLocation } from 'react-router-dom';
import { Line, Typography, SlideDown, Rectangle } from '../../components';
import moment from 'moment';

const TableItemBase = styled.div`
    display: flex;
    flex-direction: column;
    margin-left: 10.9rem;
    .table-item-wrapper {
        height: 4.8rem;
    }
    .table-item-wrapper-line {
        width: 1.2rem;
        margin: 0 1rem 0 1rem;
    }
    .table-item-wrapper-line-bottom {
        height: auto;
        width: 1.2rem;
        margin: 0 0.8rem 0 0.8rem;
    }
    .table-item-text {
        display: flex;
        flex-direction: column;
    }
    .button-wrapper {
        display: flex;
        flex-direction: row;
        align-items: center;
        cursor: pointer;
    }
    .button-play-container {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
    }
    .table-item-container {
        cursor: pointer;
        display: flex;
        align-items: center;
        margin-left: 3.5rem;
    }
    .rect-wrapper-play {
        margin: 0 0 0 0.4rem;
    }
    .button-and-lines-play {
        display: flex;
        margin-top: 0.8rem;
    }
    .play-items-wrapper {
        display: flex;
        flex-direction: column;
    }
    .rectangle-small {
        margin: 0.2rem 0.6rem 0 0;
    }
    .info-play-wrapper {
        padding: 1.2rem 0 1.2rem 0;
    }
    .button-play-container {
        padding: 1rem 0 1rem 0;
    }
    .ledger-balance {
        margin: 0 0.4rem 0 0.4rem;
    }
    .ledger-item-inner {
        display: flex;
        align-items: center;
        width: 9.8rem;
    }
    .ledger-item {
        align-items: center;
        display: flex;
    }
    .ledger-item + .ledger-item {
        margin-top: 0.3rem;
    }
    .rectangle-ledger {
        margin-bottom: 0.1rem;
    }
    .line-horizontal-ledger {
        width: 100%;
        margin-bottom: 0.1rem;
    }
    .player-name-ledger {
        margin-left: 0.6rem;
    }
    .play-info-item {
        display: flex;
        align-items: center;
    }
    .info-item-text {
        margin-left: 0.8rem;
    }
    .hide-text-wrapper {
        display: flex;
        align-items: center;
    }
    .hide-text-text {
        margin-left: 0.6rem;
    }
    .hide-text-wrapper + .hide-text-wrapper {
        margin-top: 0.3rem;
    }
    .play-info-item + .play-info-item {
        margin-top: 0.3rem;
    }
    .hide-text-button {
        cursor: pointer;
        align-items: center;
        display: flex;
        margin-top: 1.9rem;
    }
    .wrapper-play-button {
        display: flex;
    }
    .play-rect-top {
        margin: 0.8rem 0 0.8rem 8.8rem;
    }
`;

interface TableItemProps {
    table: Record<string, any>;
    onRemove: (id: any) => void;
    type: 'archive' | 'active';
}

const TableItem = ({ table, type, onRemove }: TableItemProps) => {
    const location = useLocation();
    const [active, setActive] = useState(false);
    const [selectedButton, setSelectedButton] = useState<string | undefined>();

    const playersTable = table.online !== 0 ? `[${table.online}/${table.max}]` : '[EMPTY]';

    return (
        <>
            <TableItemBase style={type === 'archive' ? { marginLeft: '6.1rem' } : undefined}>
                <Rectangle height="small" width="small" border="small" color="yellow" className="play-rect-top" />
                <div
                    className="table-item-container"
                    onClick={() => setActive((prev) => !prev)}
                    style={table.online === 0 ? { marginLeft: '0.5rem' } : undefined}
                >
                    <Typography component="span" variant="body1">
                        {playersTable}
                    </Typography>
                    <Line
                        width="large"
                        color={active ? 'yellow' : 'primary'}
                        className="table-item-wrapper-line"
                        height="short"
                    />
                    <div className="table-item-text">
                        <Typography component="span" variant="h3">
                            {table.name}
                        </Typography>
                        <Typography component="span" variant="h5" textTransform="capitalize">
                            {table.type}
                        </Typography>
                    </div>
                </div>
                <SlideDown open={active}>
                    <div className="button-and-lines-play">
                        <div className="button-play-container">
                            {type !== 'archive' ? (
                                <TablesButton
                                    name="play"
                                    selectedButton={selectedButton}
                                    onClick={() => {
                                        setSelectedButton('play');
                                        window.open(
                                            `${window.location.protocol}//${window.location.hostname}${
                                                window.location.port ? ':' + window.location.port : ''
                                            }/table/${table.gameNumber}`,
                                        );
                                    }}
                                />
                            ) : null}
                            <TablesButton
                                name="ledger"
                                selectedButton={selectedButton}
                                onClick={() => setSelectedButton('ledger')}
                            />
                            <TablesButton
                                name="info"
                                selectedButton={selectedButton}
                                onClick={() => setSelectedButton('info')}
                            />
                            <TablesButton
                                name="hide"
                                selectedButton={selectedButton}
                                onClick={() => setSelectedButton('hide')}
                            />
                        </div>
                        <Line width="medium" color="yellow" className="table-item-wrapper-line" />
                        <div className="info-play-wrapper">
                            <div className="play-items-wrapper">
                                <ActiveMenu selectedButton={selectedButton} table={table} onRemove={onRemove} />
                            </div>
                        </div>
                    </div>
                </SlideDown>
            </TableItemBase>
        </>
    );
};

interface ActiveMenuProps {
    selectedButton: string | undefined;
    table: Record<string, any>;
    onRemove: Function;
}

const ActiveMenu = ({ selectedButton, table, onRemove }: ActiveMenuProps) => {
    switch (selectedButton) {
        case 'play':
            return (
                <div className="wrapper-play-button">
                    <Rectangle
                        height="small"
                        width="small"
                        color="secondary"
                        border="small"
                        className="rectangle-small"
                    />
                    <Typography component="span" variant="h6">
                        {`${table.name} has opened in a new tab.`}
                    </Typography>
                </div>
            );
        case 'ledger':
            return (
                <>
                    {Object.keys(table.ledger!).map((key: string) => (
                        <LedgerItem key={`${key}${table.ledger[key]}`} name={key} balance={table.ledger[key]} />
                    ))}
                </>
            );
        case 'info':
            return (
                <>
                    <InfoItem name="url" value={`www.weflop.com/game/${table.gameNumber}`} />
                    <InfoItem name="table name" value={table.name} />
                    <InfoItem name="creator" value={table.createdBy} />
                    <InfoItem name="stake" value={`${table.smallBlind}/${table.bigBlind}`} />
                    <InfoItem name="variant" value={table.variant} uppercase />
                    <InfoItem name="birth date" value={moment.unix(table.dateCreated).format('DD.MM.YYYY / HH.mm')} />
                    <InfoItem name="time bank" value={`${table.turnDuration}s`} />
                    <InfoItem name="MIN/MAX BUYIN" value={`${table.minBuyIn}BB/${table.maxBuyIn}BB`} uppercase />
                </>
            );
        case 'hide':
            return (
                <>
                    <div className="hide-text-wrapper">
                        <Rectangle
                            height="medium"
                            width="medium"
                            color="secondary"
                            border="small"
                            className="rectangle-ledger"
                        />
                        <Typography component="span" variant="h6" className="hide-text-text">
                            If you hide this table, it will no longer be displayed in this list.
                        </Typography>
                    </div>
                    <div className="hide-text-wrapper">
                        <Rectangle
                            height="medium"
                            width="medium"
                            color="secondary"
                            border="small"
                            className="rectangle-ledger"
                        />
                        <Typography component="span" variant="h6" className="hide-text-text">
                            However, if you were to rejoin this game via link, it will reappear in this list.
                        </Typography>
                    </div>
                    <div className="hide-text-button" onClick={() => onRemove(table.gameNumber)}>
                        <Rectangle
                            height="medium"
                            width="medium"
                            color="initial"
                            border="small"
                            className="rectangle-ledger"
                        />
                        <Typography component="span" variant="h5" className="hide-text-text">
                            hide
                        </Typography>
                    </div>
                </>
            );
        default:
            return null;
    }
};

interface TablesButtonProps {
    name: string;
    selectedButton: string | undefined;
    onClick: any;
}

const TablesButton = ({ name, selectedButton, onClick }: TablesButtonProps) => {
    const color = selectedButton === name ? 'secondary' : 'primary';
    return (
        <div className="button-wrapper" onClick={onClick}>
            <Typography component="span" variant="h5" textTransform="uppercase" color={color}>
                {name}
            </Typography>
            <Rectangle height="medium" width="medium" color={color} border="small" className="rect-wrapper-play" />
        </div>
    );
};

interface LedgerItemProps {
    balance: number;
    name: string;
}

const LedgerItem = ({ balance, name }: LedgerItemProps) => {
    const color = balance > 0 ? 'success' : 'error';
    return (
        <div className="ledger-item">
            <div className="ledger-item-inner">
                <Rectangle height="medium" width="medium" color={color} border="small" className="rectangle-ledger" />
                <Typography variant="h6" component="span" color={color} className="ledger-balance">
                    {balance}
                </Typography>
                <Rectangle
                    height="initial"
                    width="medium"
                    color={color}
                    border="small"
                    className="line-horizontal-ledger"
                />
            </div>
            <Typography variant="h6" component="span" className="player-name-ledger">
                {name}
            </Typography>
        </div>
    );
};

interface InfoItemProps {
    name: string;
    value: any;
    uppercase?: boolean;
}

const InfoItem = ({ name, value, uppercase }: InfoItemProps) => {
    return (
        <div className="play-info-item">
            <Rectangle height="medium" width="medium" color="secondary" border="small" className="rectangle-ledger" />
            <Typography component="p" variant="h6" className="info-item-text" textTransform="uppercase">
                {`${name}:`}
                <Typography component="span" variant="h6" style={uppercase ? { textTransform: 'uppercase' } : null}>
                    {` ${value}`}
                </Typography>
            </Typography>
        </div>
    );
};

export default TableItem;
