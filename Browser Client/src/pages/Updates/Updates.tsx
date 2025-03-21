import React, { useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { Title, LineContent, Subtitle, Navigation, Container, UpdateItem, Content } from '../../layout';
import { Rectangle, Line } from '../../components';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { getUpdates, GET_UPDATES } from '../../store/actions/application';

const Updates = () => {
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);

    useEffect(() => {
        dispatch(getUpdates());
    }, []);

    return (
        <>
            <Helmet>
                <title>Updates</title>
            </Helmet>
            <Container>
                <Navigation type={'auth'} />
                <Content>
                    <LineContent>
                        <Title animated color="secondary">
                            BETA V1.01
                        </Title>
                        <div className="subtitles-wrapper-inner">
                            <Subtitle animated>
                                We are constantly working to make WeFlop a better product for you.
                            </Subtitle>
                            <Subtitle animated>This is where we post our updates and poll our community.</Subtitle>
                        </div>
                        <Line color="secondary" width="large" height="short" align="left" animated />
                    </LineContent>
                    <div style={{ margin: '0.8rem 0 0 1.6rem' }}>
                        <Rectangle height="extralarge" width="extralarge" />
                        {application.updates &&
                            application.updates.map((element, index) => <UpdateItem element={element} />)}
                    </div>
                </Content>
            </Container>
        </>
    );
};

export default Updates;
