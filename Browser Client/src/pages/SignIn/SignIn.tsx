import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { useHistory } from 'react-router-dom';
import { Button, TextField, Line } from '../../components';
import { Title, LineContent, Subtitle, Form, Navigation, Content, Container } from '../../layout';
import { checkValidation } from '../../helpers';
import { useForm } from 'react-hook-form';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { signIn, SIGN_IN } from '../../store/actions/application';

const SignIn = () => {
    const history = useHistory();
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);
    const { register, errors, handleSubmit, watch } = useForm({
        mode: 'all',
        shouldFocusError: true,
    });

    const [isClicked, setIsClicked] = useState(false);

    const onSubmit = (data) => {
        dispatch(signIn(data));
    };

    const isActive = {
        email: watch('email', ''),
        password: watch('password', ''),
    };

    const check = {
        email: checkValidation(isActive.email, errors.email),
        password: checkValidation(isActive.password, errors.password),
    };

    const isValidated = check.email && check.password;
    const buttonSubmit = (
        <Button variant="secondary" validated={isValidated} className="button-form" title="Enter" animated />
    );

    useEffect(() => {
        if (application.isLoading[SIGN_IN.REQUEST]) {
            console.log('Started SIGN_IN request...');
        } else {
            console.log('SIGN_IN request finished!');
        }
    }, [application.isLoading[SIGN_IN.REQUEST]]);

    useEffect(() => {
        console.log('Error updated: ', application.error[SIGN_IN.ERROR]);
    }, [application.error[SIGN_IN.ERROR]]);

    useEffect(() => {
        if (application.isAuthenticated) {
            history.push('/updates');
        }
    }, [application.isAuthenticated]);

    return (
        <>
            <Helmet>
                <title>Sign In</title>
            </Helmet>
            <Container>
                <Navigation type={'basic'} />
                <Content>
                    <LineContent>
                        <Title animated color="secondary">
                            Welcome back!
                        </Title>
                        <div className="subtitles-wrapper-inner">
                            <Subtitle animated>WeFlop is a player-supported online poker platform.</Subtitle>
                            <Subtitle animated>We are committed to building the features you want.</Subtitle>
                            <Subtitle animated>Our beta supports home games with simple, intuitive ledgers.</Subtitle>
                        </div>
                        <Line width="large" height="short" align="left" color="secondary" animated />
                        <Form onSubmit={handleSubmit(onSubmit)} buttonSubmit={buttonSubmit} isClicked={isClicked}>
                            <TextField
                                animated
                                onFocus={() => setIsClicked(true)}
                                placeholder="email adress"
                                type="email"
                                name="email"
                                validated={check.email}
                                errorMessage={errors.email && errors.email.message}
                            />
                            <TextField
                                animated
                                onFocus={() => setIsClicked(true)}
                                placeholder="password"
                                type="password"
                                name="password"
                                validated={check.password}
                                register={register({
                                    required: {
                                        value: true,
                                        message: 'This field is required',
                                    },
                                    minLength: {
                                        value: 8,
                                        message: 'Password should have at least 8 symbols',
                                    },
                                    maxLength: {
                                        value: 24,
                                        message: 'Password should not be longer then 24 symbols',
                                    },
                                    pattern: {
                                        // eslint-disable-next-line
                                        value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])/,
                                        message:
                                            'Passwords must contain at least one lower case letter, at least one upper case letter, have a least one 0-9 digit, and have at least one special character',
                                    },
                                })}
                                errorMessage={errors.password && errors.password.message}
                            />
                        </Form>
                    </LineContent>
                </Content>
            </Container>
        </>
    );
};

export default SignIn;
