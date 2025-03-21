import React, { useState } from 'react';
import { Divider } from '../';
import { NavButton } from '../../components';
import { Rectangle } from '../../../../components';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../../../store';
import { setAutoAction } from '../../../../store/actions/table';

import styled from 'styled-components';
import { animated, InterpolationChain } from 'react-spring';

interface ActionMenuProps {
    className?: string;
    type: 'fold' | 'check-fold' | 'muck';
    style: any;
}

interface ActionMenuBaseProps {
    type: 'fold' | 'check-fold' | 'muck';
    style: any;
}

const ActionMenuBase = styled(animated.div)<ActionMenuBaseProps>`
    .button-hor {
        margin-bottom: ${({ type }) => (type === 'muck' ? '2.4rem' : '0.8rem')};
    }
    .rect-nav {
        cursor: pointer;
        &:hover > span {
            background-color: ${({ theme }) => theme.palette.yellow};
        }
    }
`;

const ActionMenu = ({ className, type, style }: ActionMenuProps) => {
    const dispatch = useDispatch();
    const table = useSelector((state: RootState) => state.table);

    const setActive = (name) => {
        dispatch(setAutoAction({ name }));
    };
    return (
        <ActionMenuBase type={type} style={style}>
            <Divider
                topContent={
                    type === 'muck' ? undefined : (
                        <Rectangle
                            className="rect-nav"
                            border="small"
                            color={table.autoAction === 'default' ? 'yellow' : 'initial'}
                            width="2.4rem"
                            height="large"
                            onClick={() => setActive('default')}
                        />
                    )
                }
                leftContent={
                    <NavButton
                        align="right"
                        title={type === 'fold' ? 'fold' : type === 'check-fold' ? 'check/fold' : 'muck'}
                        noMargin
                        className="button-hor"
                        active={table.autoAction === 'checkfold' || table.autoAction === 'muck'}
                        onClick={() => setActive(type === 'fold' || type === 'check-fold' ? 'checkfold' : 'muck')}
                    />
                }
                rightContent={
                    <NavButton
                        align="left"
                        title={type === 'fold' ? 'call any' : type === 'check-fold' ? 'call any' : 'show'}
                        noMargin
                        className="button-hor"
                        active={table.autoAction === 'callany' || table.autoAction === 'show'}
                        onClick={() => setActive(type === 'fold' || type === 'check-fold' ? 'callany' : 'show')}
                    />
                }
                className={className}
                lineBottom
            />
        </ActionMenuBase>
    );
};

export default ActionMenu;
