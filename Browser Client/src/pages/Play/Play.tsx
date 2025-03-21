import React, { useState } from 'react';
import { Helmet } from 'react-helmet';
import { Line, Typography } from '../../components';
import { Title, LineContent, Subtitle, Navigation, Container, Content } from '../../layout';

import { Form, Tabs } from './components';

const Play = () => {
    const [isCreated, setIsCreated] = useState(false);

    const onSubmit = (data) => {
        setIsCreated(true);
        console.log(data);
    };

    const notification = isCreated ? (
        <Typography component="span" variant="button2" color="secondary">
            TABLE CREATED.
        </Typography>
    ) : null;

    return (
        <>
            <Helmet>
                <title>Play</title>
            </Helmet>
            <Container>
                <Navigation type={'auth'} />
                <Content>
                    <div style={{ marginLeft: '13.6rem' }}>
                        <LineContent>
                            <Title animated color="secondary">
                                HOME GAMES
                            </Title>
                            <div className="subtitles-wrapper-inner">
                                <Subtitle animated>Click “CREATE” to customize your own poker game. </Subtitle>
                                <Subtitle animated>To invite your friends, simply share the table’s link.</Subtitle>
                                <Subtitle animated>
                                    Every game you create or join via link is saved in “TABLES.”
                                </Subtitle>
                            </div>
                            <Line color="secondary" width="large" height="short" align="left" animated />
                        </LineContent>
                    </div>
                    <Tabs
                        form={<Form onSubmit={onSubmit} notification={notification} isCreated={isCreated} />}
                        isCreated={isCreated}
                    />
                </Content>
            </Container>
        </>
    );
};

export default Play;
