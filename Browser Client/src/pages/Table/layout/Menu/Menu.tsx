import React from 'react';
import styled from 'styled-components';
import { NavButton } from '../../components';
import { Divider } from '../';
import MenuItem from './MenuItem';
import { useDispatch } from 'react-redux';
import { cancelChoosing } from '../../../../store/actions/table';

interface BaseMenuProps {
    navState: string | undefined;
}

const BaseMenu = styled.div<BaseMenuProps>`
    .menu-block-left {
        position: absolute;
        flex-direction: column;
        align-items: flex-start;
        display: flex;
    }
    .menu-block-right {
        position: absolute;
        flex-direction: column;
        align-items: flex-end;
        display: flex;
    }
    .top-right-menu {
        top: 0;
        right: 0;
        z-index: 6;
    }
    .bottom-left-menu {
        bottom: 0;
        left: 0;
        z-index: 6;
    }
    .top-left-menu {
        top: 0;
        left: 0;
        z-index: 6;
    }
    .bottom-right-menu {
        right: 0;
        bottom: 0;
        z-index: 1;
    }
    .animation-opacity {
        transition: ${({ navState }) => (!navState ? 'opacity 0.2s ease-in-out' : 'none')};
        opacity: ${({ navState }) => (!navState ? '25%' : '100%')};
        &:hover {
            opacity: 100%;
        }
    }
`;

const Menu = ({ navState, setNavState, type }) => {
    return (
        <BaseMenu navState={navState}>
            <div className="bottom-left-menu menu-block-left animation-opacity">
                <MenuItem bottom left>
                    <NavButton
                        title="ledger"
                        align="left"
                        active={navState === 'ledger'}
                        onClick={() => setNavState('ledger')}
                    />
                </MenuItem>
                <MenuItem bottom left>
                    <NavButton
                        title="chat"
                        align="left"
                        active={navState === 'chat'}
                        onClick={() => setNavState('chat')}
                    />
                </MenuItem>
                <Divider lineLeft lineBottom />
            </div>

            <div className="top-left-menu menu-block-left animation-opacity">
                <Divider lineLeft lineTop />
                <MenuItem top left>
                    <NavButton title="back to game" align="left" onClick={() => setNavState(null)} />
                </MenuItem>
                <MenuItem top left>
                    <NavButton
                        title="leave"
                        align="left"
                        active={navState === 'leave'}
                        onClick={() => setNavState('leave')}
                    />
                </MenuItem>
            </div>

            <div className="menu-block-right top-right-menu animation-opacity">
                <Divider lineRight lineTop />
                <MenuItem top right>
                    <NavButton
                        title="settings"
                        align="right"
                        active={navState === 'settings'}
                        onClick={() => setNavState('settings')}
                    />
                </MenuItem>
            </div>
            <div className="menu-block-right bottom-right-menu">
                <BottomRightMenu type={type} />
            </div>
        </BaseMenu>
    );
};

const BottomRightMenu = ({ type }) => {
    const dispatch = useDispatch();
    switch (type) {
        case 'sit-in-next':
            return (
                <>
                    <MenuItem bottom right>
                        <NavButton title="SIT IN" align="right" />
                    </MenuItem>
                    <MenuItem bottom right>
                        <NavButton title="SIT OUT NEXT HAND" align="right" />
                        <NavButton title="SIT OUT NEXT BB" align="right" />
                    </MenuItem>
                    <Divider lineRight lineBottom />
                </>
            );
        case 'blind':
            return (
                <>
                    <MenuItem bottom right>
                        <NavButton title="WAIT FOR BIG BLIND" align="right" />
                        <NavButton title="POST BIG BLIND" align="right" />
                        <NavButton title="SIT OUT" align="right" />
                    </MenuItem>
                    <MenuItem bottom right>
                        <NavButton title="CHANGE SEATS" align="right" />
                    </MenuItem>
                    <Divider lineRight lineBottom />
                </>
            );
        case 'sit-in':
            return (
                <>
                    <MenuItem bottom right>
                        <NavButton title="SIT IN" align="right" />
                    </MenuItem>
                    <MenuItem bottom right>
                        <NavButton title="SIT OUT NEXT HAND" align="right" />
                    </MenuItem>
                    <MenuItem bottom right>
                        <NavButton title="SIT OUT NEXT BB" align="right" />
                    </MenuItem>
                    <Divider lineRight lineBottom />
                </>
            );
        case 'cancel':
            return (
                <>
                    <MenuItem bottom right>
                        <NavButton title="CANCEL" align="right" onClick={() => dispatch(cancelChoosing())} />
                    </MenuItem>
                    <Divider lineRight lineBottom />
                </>
            );
        default:
            return null;
    }
};

export default Menu;
