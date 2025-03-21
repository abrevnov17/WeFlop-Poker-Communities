import React, { useContext } from 'react';
import styled from 'styled-components';
import { Divider } from '../../layout';
import { Line } from '../../../../components';
import Checkbox from './CheckboxSettings';
import { RootState } from '../../../../store';
import { setMusic, setSounds, setAutoMuck } from '../../../../store/actions/table';
import { Container } from '../../../../layout';
import { useDispatch, useSelector } from 'react-redux';
import { SocketContext } from '../../../../providers';

interface SettingsProps {
    className?: string;
}

const Settings = ({ className }: SettingsProps) => {
    const dispatch = useDispatch();
    const table = useSelector((state: RootState) => state.table);
    const { socket } = useContext(SocketContext);

    return (
        <Container type="modal">
            <div className={className}>
                <Line width="large" color="yellow" wrapperClassName="line-short" />
                <Divider margin="0.6rem" />
                <Line width="large" color="yellow" wrapperClassName="line-long" />
                <Divider margin="0.6rem" />
                <div className="checkboxes-wrapper">
                    <Checkbox
                        title="AUTO-MUCK ALL HANDS"
                        active={table.autoMuck}
                        onClick={() => dispatch(setAutoMuck(socket))}
                    />
                    <Checkbox title="MUSIC" active={table.music} onClick={() => dispatch(setMusic())} />
                    <Checkbox title="GAME SOUNDS" active={table.gameSounds} onClick={() => dispatch(setSounds())} />
                </div>
                <Divider margin="0.6rem" />
                <Line width="large" color="yellow" wrapperClassName="line-long" />
                <Divider margin="0.6rem" />
                <Line width="large" color="yellow" wrapperClassName="line-short" />
            </div>
        </Container>
    );
};

export default styled(Settings)`
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    .line-long {
        flex: 1;
    }
    .line-short {
        height: 2.4rem !important;
    }
    .checkboxes-wrapper {
        margin-right: 22rem;
    }
`;
