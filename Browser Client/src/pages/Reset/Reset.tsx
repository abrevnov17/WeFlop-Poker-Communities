import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { Button, TextField, Line, Typography } from '../../components';
import { Title, LineContent, Subtitle, Form, Navigation, Content } from '../../layout';
import { checkValidation } from '../../helpers';
import Container from '../../layout/Container/Container';
import { useForm } from 'react-hook-form';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { resetPassword, RESET_PASSWORD } from '../../store/actions/application';

const Reset = () => {
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);
    const { register, errors, handleSubmit, watch } = useForm({
        mode: 'all',
        shouldFocusError: true,
    });

    const [isEmailSent, setIsEmailSent] = useState(false);
    const [isClicked, setIsClicked] = useState(false);

    const onSubmit = (data) => {
        setIsEmailSent(true);
        dispatch(resetPassword(data));
    };

    const isActive = {
        email: watch('email', ''),
    };

    const check = {
        email: checkValidation(isActive.email, errors.email),
    };

    const isValidated = check.email;
    const buttonSubmit = (
        <Button
            variant="secondary"
            validated={isValidated}
            className="button-form"
            title="send reset"
            isActionCompleted={isEmailSent}
            animated
        />
    );
    const notification = (
        <>
            <Typography component="span" variant="button2" color="primary">
                AN EMAIL WAS SENT TO:
            </Typography>
            <Typography component="span" variant="h2" color="primary">
                {isActive.email}
            </Typography>
        </>
    );

    useEffect(() => {
        if (application.isLoading[RESET_PASSWORD.REQUEST]) {
            console.log('Started RESET_PASSWORD request...');
        } else {
            console.log('RESET_PASSWORD request finished!');
        }
    }, [application.isLoading[RESET_PASSWORD.REQUEST]]);

    useEffect(() => {
        console.log('Error updated: ', application.error[RESET_PASSWORD.ERROR]);
    }, [application.error[RESET_PASSWORD.ERROR]]);

    useEffect(() => {
        // setIsEmailSent(true);
    }, [application.user]);

    const opacity = isEmailSent ? { opacity: 0 } : { opacity: 1 };

    return (
        <>
            <Helmet>
                <title>Reset Password</title>
            </Helmet>
            <Container>
                <Navigation type={'basic'} />
                <Content>
                    <LineContent>
                        <Title animated color="secondary" style={opacity}>
                            Forgot Your Password?
                        </Title>
                        <div className="subtitles-wrapper-inner">
                            <Subtitle animated style={opacity}>
                                Enter the email address associated with your account.
                            </Subtitle>
                            <Subtitle animated style={opacity}>
                                We’ll send you an email with a reset form.
                            </Subtitle>
                        </div>
                        <Line color="secondary" width="large" height="short" align="left" animated />
                        <Form
                            onSubmit={handleSubmit(onSubmit)}
                            buttonSubmit={buttonSubmit}
                            isClicked={isClicked}
                            notification={isEmailSent ? notification : null}
                        >
                            <TextField
                                onFocus={() => setIsClicked(true)}
                                placeholder="email adress"
                                type="email"
                                name="email"
                                style={opacity}
                                validated={check.email}
                                animated
                                disabled={isEmailSent}
                                register={register({
                                    required: {
                                        value: true,
                                        message: 'This field is required',
                                    },
                                    minLength: {
                                        value: 4,
                                        message: 'Email should be at least 4 symbols',
                                    },
                                    maxLength: {
                                        value: 254,
                                        message: 'Email should not be longer then 254 symbols',
                                    },
                                    pattern: {
                                        value: /\S+@\S+\.\S+/,
                                        message: 'Email should have form user@email.com',
                                    },
                                })}
                                errorMessage={errors.email && errors.email.message}
                            />
                        </Form>
                    </LineContent>
                </Content>
            </Container>
        </>
    );
};

export default Reset;
