import React from 'react';
import styled from 'styled-components';

import { Typography } from '../../../../components';

interface ChatItemBaseProps {
    color: 'primary' | 'secondary' | 'button' | 'initial' | string;
}

const ChatItemBase = styled.div<ChatItemBaseProps>`
    display: flex;
    align-items: center;
    margin-bottom: 0.6rem;
    height: 100%;
    .chat-item-name {
        width: 15rem;
        display: block;
        text-align: right;
    }
    .line-chat {
        height: inherit;
        min-width: 0.8rem;
        background-color: ${({ color }) => color};
        margin: 0 1.2rem 0 1.2rem;
        border-radius: 0.1rem;
    }
    .message-wrapper {
        display: flex;
        max-width: 91rem;
        width: 100%;
    }
    .chat-item-message {
        width: 100%;
        max-width: 85.2rem;
    }
`;

interface ChatItemProps {
    color: 'primary' | 'secondary' | 'button' | 'initial' | string;
    name?: string;
    message: string;
}

const ChatItem = ({ color, name, message }: ChatItemProps) => {
    return (
        <ChatItemBase color={color}>
            <Typography variant="body1" component="span" className="chat-item-name">
                {name}
            </Typography>
            <div className="message-wrapper">
                <div className="line-chat" />
                <Typography variant="body1" component="span" className="chat-item-message">
                    {message}
                </Typography>
            </div>
        </ChatItemBase>
    );
};

export default ChatItem;
