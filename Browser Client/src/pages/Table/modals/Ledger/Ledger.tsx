import React from 'react';
import styled from 'styled-components';
import { Typography } from '../../../../components';
import LedgerItem from './LedgerItem';

import { Container } from '../../../../layout';
import { useSelector } from 'react-redux';
import { RootState } from '../../../../store';

interface LedgerProps {
    className?: string;
}

const Ledger = ({ className }: LedgerProps) => {
    const table = useSelector((state: RootState) => state.table);

    return (
        <Container type="modal">
            <div className={className}>
                <Typography variant="h1" component="span" className="ledger-title" color="yellow">
                    {table.tableName}
                </Typography>
                <div className="ledger-list-wrapper">
                    {table.ledger.map((el) => (
                        <LedgerItem name={el.name} id={el.id} balance={el.balance} key={el.id} />
                    ))}
                </div>
            </div>
        </Container>
    );
};

export default styled(Ledger)`
    display: flex;
    flex-direction: column;
    align-items: center;
    .ledger-list-wrapper {
        padding: 4rem 7.5rem 4rem 0;
        overflow-y: auto;
        &::-webkit-scrollbar {
            display: none;
        }
    }
    .ledger-title {
        margin-top: 8rem;
    }
`;
