import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import { Button, TextField, Line } from '../../components';
import { Title, LineContent, Subtitle, Form, Navigation, Container, Content } from '../../layout';
import { checkValidation } from '../../helpers';
import { useForm } from 'react-hook-form';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store';
import { signUp, SIGN_UP } from '../../store/actions/application';

const SignUp = () => {
    const dispatch = useDispatch();
    const application = useSelector((state: RootState) => state.application);
    const { register, errors, handleSubmit, watch } = useForm({
        mode: 'all',
        shouldFocusError: true,
    });

    const [isClicked, setIsClicked] = useState(false);

    const onSubmit = (data) => {
        dispatch(signUp(data));
    };

    const isActive = {
        name: watch('name', ''),
        email: watch('email', ''),
        password: watch('password', ''),
        password_repeat: watch('password_repeat', ''),
    };

    const check = {
        name: checkValidation(isActive.name, errors.name),
        email: checkValidation(isActive.email, errors.email),
        password: checkValidation(isActive.password, errors.password),
        password_repeat: checkValidation(isActive.password_repeat, errors.password_repeat),
    };

    const isValidated = check.name && check.email && check.password && check.password_repeat;
    const buttonSubmit = (
        <Button variant="secondary" validated={isValidated} className="button-form" title="register" animated />
    );

    useEffect(() => {
        if (application.isLoading[SIGN_UP.REQUEST]) {
            console.log('Started SIGN_UP request...');
        } else {
            console.log('SIGN_UP request finished!');
        }
    }, [application.isLoading[SIGN_UP.REQUEST]]);

    useEffect(() => {
        console.log('Error updated: ', application.error[SIGN_UP.ERROR]);
    }, [application.error[SIGN_UP.ERROR]]);

    useEffect(() => {
        console.log('User updated: ', application.user);
    }, [application.user]);

    return (
        <>
            <Helmet>
                <title>Sign Up</title>
            </Helmet>
            <Container>
                <Navigation type={'basic'} />
                <Content>
                    <LineContent>
                        <Title color="secondary" animated>
                            Welcome!
                        </Title>
                        <div className="subtitles-wrapper-inner">
                            <Subtitle animated>WeFlop is a player-supported online poker platform.</Subtitle>
                            <Subtitle animated>We are committed to building the features you want.</Subtitle>
                            <Subtitle animated>Our beta supports home games with simple, intuitive ledgers.</Subtitle>
                        </div>
                        <Line color="secondary" width="large" height="short" align="left" animated />
                        <Form onSubmit={handleSubmit(onSubmit)} buttonSubmit={buttonSubmit} isClicked={isClicked}>
                            <TextField
                                animated
                                onFocus={() => setIsClicked(true)}
                                placeholder="unique display name"
                                type="text"
                                name="name"
                                validated={check.name}
                                register={register({
                                    required: {
                                        value: true,
                                        message: 'This field is required',
                                    },
                                    minLength: {
                                        value: 5,
                                        message: 'Username should be at least 5 symbols',
                                    },
                                    maxLength: {
                                        value: 24,
                                        message: 'Username should not be longer then 254 symbols',
                                    },
                                    pattern: {
                                        value: /^[a-zA-Z0-9]{5,24}$/,
                                        message: 'Username should consist from a-Z 0-9 symbols',
                                    },
                                })}
                                errorMessage={errors.name && errors.name.message}
                            />
                            <TextField
                                animated
                                onFocus={() => setIsClicked(true)}
                                placeholder="email adress"
                                type="email"
                                name="email"
                                validated={check.email}
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
                            <TextField
                                animated
                                onFocus={() => setIsClicked(true)}
                                placeholder="retype password"
                                type="password"
                                name="password_repeat"
                                validated={check.password && check.password_repeat}
                                register={register({
                                    validate: (value) => value === isActive.password || 'The passwords do not match',
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
                                errorMessage={errors.password_repeat && errors.password_repeat.message}
                            />
                        </Form>
                    </LineContent>
                </Content>
            </Container>
        </>
    );
};

export default SignUp;
