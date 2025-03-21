import React, { useState } from 'react';
import { Helmet } from 'react-helmet';
import { Button, TextField, Typography, Line } from '../../components';
import { Title, LineContent, Subtitle, Form, Navigation, PatreonIcon, Content } from '../../layout';
import Container from '../../layout/Container/Container';
import { useForm } from 'react-hook-form';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { sendFeedback, SEND_FEEDBACK } from '../../store/actions/application';

const Contact = () => {
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);
    const { register, watch, errors, handleSubmit } = useForm({
        mode: 'all',
        shouldFocusError: true,
    });
    const [isClicked, setIsClicked] = useState(false);
    const [isMessageSent, setIsMessageSent] = useState(false);

    const feedback = watch('feedback', '');

    const buttonSubmit = (
        <Button
            variant="secondary"
            className="button-form"
            validated={Boolean(feedback)}
            title="Enter"
            isActionCompleted={isMessageSent}
        />
    );
    const notification = (
        <Typography component="span" variant="button2" color="yellow" textTransform="uppercase">
            message was sent
        </Typography>
    );

    const onSubmit = (data) => {
        setIsMessageSent(true);
        dispatch(sendFeedback(data));
    };

    const onEnterPress = (e) => {
        if (e.keyCode == 13 && e.shiftKey == false) {
            e.preventDefault();
            handleSubmit(onSubmit)();
        }
    };

    const opacity = isMessageSent ? { opacity: 0 } : { opacity: 1 };

    return (
        <>
            <Helmet>
                <title>Contact</title>
            </Helmet>
            <Container>
                <Navigation type={'auth'} />
                <Content>
                    <LineContent>
                        <Title animated color="secondary" style={opacity}>
                            GIVE US FEEDBACK!
                        </Title>
                        <div className="subtitles-wrapper-inner">
                            <Subtitle animated style={opacity}>
                                We want to hear what you have to say.
                            </Subtitle>
                            <Subtitle animated style={opacity}>
                                We read all your messsages, even the not so nice ones…
                            </Subtitle>
                            <Subtitle animated style={opacity}>
                                Please also consider donating to our Patreon!
                            </Subtitle>
                        </div>
                        <Line color="secondary" width="large" height="short" align="left" animated />
                        <div className="d-flex">
                            <Form
                                onSubmit={handleSubmit(onSubmit)}
                                buttonSubmit={buttonSubmit}
                                isClicked={isClicked}
                                isRightIcon={true}
                                notification={isMessageSent ? notification : null}
                                page="contact"
                            >
                                <TextField
                                    animated={true}
                                    style={opacity}
                                    variant="textarea"
                                    onKeyDown={onEnterPress}
                                    onFocus={() => setIsClicked(true)}
                                    disabled={isMessageSent}
                                    name="feedback"
                                    validated={Boolean(feedback) && !errors.message}
                                    register={(ref) =>
                                        register(ref, {
                                            required: true,
                                            minLength: 1,
                                        })
                                    }
                                />
                            </Form>
                            <PatreonIcon />
                        </div>
                    </LineContent>
                </Content>
            </Container>
        </>
    );
};

export default Contact;
