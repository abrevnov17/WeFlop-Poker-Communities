import React from 'react';
import styled from 'styled-components';
import { useTransition, animated } from 'react-spring';

interface BaseNotfProps {
    page?: 'contact';
}

const NotificationAnimationWrapper = styled.div<BaseNotfProps>`
    .notification-auth {
        margin: ${({ page }) => (page === 'contact' ? '15rem -0.7rem 0 0.7rem' : '15rem -3rem 0 -3rem')};
        display: flex;
        flex-direction: column;
        text-align: center;
        position: absolute;
    }
    .notification-play {
        margin-top: 4.8rem;
    }
`;

interface NotificationProps {
    children?: null | JSX.Element | JSX.Element[];
    type: 'auth' | 'play';
    page?: 'contact';
}

const Notification = ({ children, type, page }: NotificationProps) => {
    const notification = useTransition(children, null, {
        from: { marginTop: '2rem', opacity: 0 },
        enter: { marginTop: '0', opacity: 1 },
    });
    return (
        <NotificationAnimationWrapper page={page}>
            <div className="form-line-notification">
                <div className={`notification-form ${type === 'auth' ? 'notification-auth' : 'notification-play'}`}>
                    {notification.map(({ item, key, props }) => (
                        <animated.div style={props}>{item && children}</animated.div>
                    ))}
                </div>
            </div>
        </NotificationAnimationWrapper>
    );
};

export default Notification;
