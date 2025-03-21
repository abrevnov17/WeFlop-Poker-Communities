import React from 'react';
import { Line } from '../../components';
import { Notification } from '../';
import styled from 'styled-components';

interface FormProps {
    children?: JSX.Element[] | JSX.Element;
    onSubmit?: any;
    buttonSubmit?: JSX.Element;
    isClicked?: boolean;
    isRightIcon?: boolean;
    notification?: JSX.Element[] | JSX.Element | null;
    className?: string;
    page?: 'contact';
}

interface FormAnimationProps {
    isClicked?: boolean;
}

const FormAnimationWrapper = styled.div<FormAnimationProps>`
    animation: ${({ theme, isClicked }) => (isClicked ? null : theme.animations.inputs)};
    & > * {
        margin-top: 1.6rem;
    }

    min-width: 30.4rem;
`;

const Form = ({ children, onSubmit, buttonSubmit, isClicked, notification, className, page }: FormProps) => {
    return (
        <div className={className}>
            <form onSubmit={onSubmit} className="form" noValidate>
                <FormAnimationWrapper isClicked={isClicked}>{children}</FormAnimationWrapper>
                <div className="lines-container">
                    <Line color="secondary" width="large" height="short" align="left" animated />
                    <Line color="secondary" width="large" height="short" align="right" animated />
                </div>
                {buttonSubmit}
                <Notification page={page} type="auth">
                    {notification}
                </Notification>
            </form>
            <Line
                color="secondary"
                wrapperClassName="line-form-fix"
                width="large"
                align="left"
                className="form-line-right"
                animated
            />
        </div>
    );
};

export default styled(Form)`
    display: flex;
    flex-direction: row;
    .line-form-fix {
        margin-top: 1.6rem;
    }
    .line-wrapper-page {
        display: flex;
        padding-left: 2.5rem;
        max-width: 75.6rem;
        width: 100%;
    }
    .form-line-right {
        margin: 0 0.8rem 0 0.8rem !important;
    }
    .form {
        position: relative;
        & > *:not(:first-child) {
            margin-top: 1.6rem;
        }
    }
`;
