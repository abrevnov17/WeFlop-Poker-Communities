import React from 'react';
import styled from 'styled-components';
import { Typography } from '../../../../components';

interface LedgerItemBaseProps {
    balance: number;
}

const LedgerItemBase = styled.div<LedgerItemBaseProps>`
    display: flex;
    align-items: center;
    & + & {
        margin-top: 1.2rem;
    }
    .player-name-ledger {
        width: 20rem;
        display: block;
        text-align: right;
    }
    .round {
        margin: 0 2.4rem 0 2.4rem;
        width: 3.6rem;
        height: 3.6rem;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 1.8rem;
        background-color: ${({ balance, theme }) => (balance < 0 ? theme.palette.error : theme.palette.success)};
    }
`;

interface LedgerItemProps {
    name: string;
    id: string;
    balance: number;
}

const LedgerItem = ({ name, id, balance }: LedgerItemProps) => {
    return (
        <LedgerItemBase balance={balance}>
            <Typography
                variant="h3"
                component="span"
                textTransform="none"
                className="player-name-ledger"
                fontWeight={600}
            >
                {name}
            </Typography>
            <div className="round">
                <Typography variant="h3" component="span" textTransform="none" fontWeight={800}>
                    {id}
                </Typography>
            </div>
            <Typography variant="h3" component="span" textTransform="none" fontWeight={600}>
                {balance}
            </Typography>
        </LedgerItemBase>
    );
};

export default LedgerItem;
