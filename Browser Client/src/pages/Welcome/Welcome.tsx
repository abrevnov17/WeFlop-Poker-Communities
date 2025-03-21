import React from 'react';
import { Helmet } from 'react-helmet';
import { Button, Line, Typography } from '../../components';
import styled from 'styled-components';

interface WelcomeProps {
    className?: string;
}

const Welcome = ({ className }: WelcomeProps) => {
    return (
        <>
            <Helmet>
                <title>Welcome</title>
            </Helmet>
            <div className={className}>
                <div className="inner-enter">
                    <div className="inner-enter-content">
                        <div className="button-wrapper-enter">
                            <Button title={'Enter'} color="initial" component="link" to={'/signup'} />
                        </div>
                        <div className="content-enter">
                            <div className="content-inner">
                                <Line width="large" color="darkblue" animated />
                                <Line width="large" color="gray" animated />
                                <Line width="large" color="darkblue" animated />
                            </div>
                            <div className="typography-enter">
                                <Typography
                                    component="h1"
                                    variant="display1"
                                    color="initial"
                                    textTransform="uppercase"
                                    animated
                                >
                                    all bets are on
                                </Typography>
                            </div>
                            <div className="content-inner content-inner-footer">
                                <Line width="large" color="darkblue" animated />
                                <Line width="large" color="gray" animated />
                                <Line width="large" color="darkblue" animated />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

export default styled(Welcome)<WelcomeProps>`
    height: 100vh;
    width: 100%;
    background-color: #8b112f;

    .inner-enter {
        padding: 6rem;
        height: 100%;
    }

    .inner-enter-content {
        position: relative;
        display: flex;
        align-content: center;
        justify-content: center;
        height: 100%;
    }

    .button-wrapper-enter {
        position: absolute;
        top: 0;
        left: 0;
    }

    .content-enter {
        display: flex;
        flex-direction: column;
        max-width: 33.7rem;
        width: 100%;
    }

    .content-inner {
        margin: 0 auto 0 auto;
        max-width: 30.4rem;
        width: 100%;
        display: flex;
        flex: 3;
        justify-content: space-between;
    }

    .typography-enter {
        padding: 3.4rem 0 3.4rem 0;
        text-align: center;
    }

    .content-inner-footer {
        flex: 1;
    }
`;
