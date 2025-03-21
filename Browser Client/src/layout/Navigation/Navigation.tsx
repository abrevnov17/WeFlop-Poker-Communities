import React from 'react';
import { useLocation } from 'react-router-dom';
import { Button } from '../../components';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { signOut } from '../../store/actions/application';
import styled from 'styled-components';
interface NavigationProps {
    type: 'auth' | 'basic' | 'table';
    className?: string;
}

const Navigation = ({ type, className }: NavigationProps) => {
    const location = useLocation();
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);

    switch (type) {
        case 'basic':
            return (
                <div className={className}>
                    <div>
                        <Button
                            bottomGutter
                            title={'Sign up'}
                            to="/signup"
                            component="link"
                            active={location.pathname === '/signup'}
                        />
                        <Button
                            bottomGutter
                            title={'Sign in'}
                            to="/signin"
                            component="link"
                            active={location.pathname === '/signin'}
                        />
                        <Button
                            title={'Reset'}
                            to="/reset/password"
                            component="link"
                            active={location.pathname === '/reset/password'}
                        />
                    </div>
                </div>
            );
        case 'auth':
            return (
                <div className={className}>
                    <div>
                        <Button
                            bottomGutter
                            title={'Updates'}
                            to="/updates"
                            component="link"
                            active={location.pathname === '/updates'}
                        />
                        <Button
                            bottomGutter
                            title={'Play'}
                            to="/play"
                            component="link"
                            active={location.pathname === '/play'}
                        />
                        <Button
                            bottomGutter
                            title={'Contact'}
                            to="/contact"
                            component="link"
                            active={location.pathname === '/contact'}
                        />
                        <Button title={'Sign out'} onClick={() => dispatch(signOut())} component="link" />
                    </div>
                </div>
            );
        case 'table':
            return (
                <div className={className} style={{ height: '100%' }}>
                    <div>
                        <Button
                            bottomGutter
                            title={'Seat status'}
                            to="/"
                            component="link"
                            active={location.pathname === ''}
                        />
                        <Button
                            bottomGutter
                            title={'Settings'}
                            to="/"
                            component="link"
                            active={location.pathname === ''}
                        />
                        <Button
                            bottomGutter
                            title={'Ledger'}
                            to="/"
                            component="link"
                            active={location.pathname === ''}
                        />
                        <Button bottomGutter title={'Info'} to="/" component="link" active={location.pathname === ''} />
                        <Button bottomGutter title={'Chat'} to="/" component="link" active={location.pathname === ''} />
                    </div>
                    <div>
                        <Button
                            bottomGutter
                            title={'Hide menu'}
                            to="/"
                            component="link"
                            active={location.pathname === ''}
                        />
                    </div>
                </div>
            );
    }
};

export default styled(Navigation)`
    display: flex;
    flex-direction: column;
    flex: 3;
    position: sticky;
    top: 0;
    align-self: flex-start;
    justify-content: space-between;
`;
