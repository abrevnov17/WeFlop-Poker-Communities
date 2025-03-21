import React, { useState } from 'react';

import { Rectangle, Button, TextField, Typography } from '../../../components';
import { Notification } from '../../../layout';

import { checkValidation } from '../../../helpers';
import { useForm } from 'react-hook-form';

import styled from 'styled-components';

interface FormProps {
    onSubmit: (data: any) => void;
    notification: JSX.Element | null;
    className?: string;
    isCreated: boolean;
}

interface FormBaseProps {
    isClicked: boolean;
}

const BaseForm = styled.form<FormBaseProps>`
    .inner-inputs-play {
        margin: 1.6rem 0 1.6rem 0.3rem;
    }
    .input-and-title-wrapper {
        display: flex;
        align-items: center;
    }
    .title-create-table {
        margin-left: 1.6rem !important;
    }
    .rect-play-input {
        margin-left: 0.8rem;
    }
    .play-button-notification-wrapper {
        max-width: 24rem;
    }
    .input-and-title-wrapper + .input-and-title-wrapper {
        margin-top: 0.8rem;
    }
    .input-pulsate {
        animation: ${({ theme, isClicked }) => (isClicked ? null : theme.animations.inputs)};
    }
`;

const Form = ({ onSubmit, notification, className, isCreated }: FormProps) => {
    const opacity = isCreated ? { opacity: '0' } : { opacity: '1' };
    const [isClicked, setIsClicked] = useState(false);

    const { register, errors, handleSubmit, watch } = useForm({
        mode: 'all',
        shouldFocusError: true,
    });
    const isActive = {
        name: watch('name', ''),
        blinds: watch('blinds', ''),
        max_buyin: watch('max_buyin', 5),
        min_buyin: watch('min_buyin', 1),
    };

    const check = {
        name: checkValidation(isActive.name, errors.name),
        blinds: checkValidation(isActive.blinds, errors.blinds),
        max_buyin: !errors.max_buyin,
        min_buyin: !errors.min_buyin,
        time: !errors.time,
    };
    const isValidated = check.name && check.blinds && check.max_buyin && check.min_buyin && check.time;

    return (
        <BaseForm onSubmit={handleSubmit(onSubmit)} noValidate className={className} isClicked={isClicked}>
            <div className="inner-inputs-play">
                <div className="input-and-title-wrapper">
                    <TextField
                        onFocus={() => setIsClicked(true)}
                        className="input-pulsate"
                        style={opacity}
                        size="medium"
                        tooltipAlign="left"
                        validated={check.name}
                        type="name"
                        width="23.4rem"
                        name="name"
                        register={register({
                            required: {
                                value: true,
                                message: 'This field is required',
                            },
                            minLength: {
                                value: 4,
                                message: 'Game name should be at least 4 symbols',
                            },
                            maxLength: {
                                value: 254,
                                message: 'Game name should not be longer then 254 symbols',
                            },
                        })}
                        errorMessage={errors.name && errors.name.message}
                    />
                    <Typography
                        style={opacity}
                        component="span"
                        variant="h4"
                        textTransform="uppercase"
                        fontWeight={800}
                        color="yellow"
                        className="title-create-table"
                    >
                        TABLE NAME
                    </Typography>
                </div>
                <div className="input-and-title-wrapper">
                    <TextField
                        onFocus={() => setIsClicked(true)}
                        className="input-pulsate"
                        style={opacity}
                        size="medium"
                        tooltipAlign="left"
                        validated={check.blinds}
                        type="number"
                        name="blinds"
                        width="11.4rem"
                        register={register({
                            required: {
                                value: true,
                                message: 'This field is required',
                            },
                            max: {
                                value: 999,
                                message: 'SMALL BLIND MUST BE BETWEEN $.01 & $999',
                            },
                            min: {
                                value: 0.01,
                                message: 'SMALL BLIND MUST BE BETWEEN $.01 & $999',
                            },
                        })}
                        errorMessage={errors.blinds && errors.blinds.message}
                    />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Typography
                        style={opacity}
                        component="span"
                        variant="h4"
                        textTransform="uppercase"
                        fontWeight={800}
                        color="yellow"
                        className="title-create-table"
                    >
                        BLINDS
                    </Typography>
                </div>
                <div className="input-and-title-wrapper">
                    <TextField
                        onFocus={() => setIsClicked(true)}
                        className="input-pulsate"
                        style={opacity}
                        size="medium"
                        tooltipAlign="left"
                        validated={check.max_buyin}
                        type="number"
                        name="max_buyin"
                        width="11.4rem"
                        defaultValue={150}
                        register={register({
                            validate: (value) =>
                                Number(value) > Number(isActive.min_buyin) ||
                                'MAX BUY-IN MUST BE GREATER THAN MIN BUY-IN',
                            required: {
                                value: true,
                                message: 'This field is required',
                            },
                            max: {
                                value: 999,
                                message: 'MAX BUY-IN MUST BE BETWEEN 10 & 999 BIG BLINDS',
                            },
                            min: {
                                value: 10,
                                message: 'MAX BUY-IN MUST BE BETWEEN 10 & 999 BIG BLINDS',
                            },
                        })}
                        errorMessage={errors.max_buyin && errors.max_buyin.message}
                    />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Typography
                        style={opacity}
                        component="span"
                        variant="h4"
                        textTransform="uppercase"
                        fontWeight={800}
                        color="yellow"
                        className="title-create-table"
                    >
                        MAX BUY-IN
                    </Typography>
                </div>
                <div className="input-and-title-wrapper">
                    <TextField
                        onFocus={() => setIsClicked(true)}
                        className="input-pulsate"
                        style={opacity}
                        size="medium"
                        tooltipAlign="left"
                        validated={check.min_buyin}
                        type="number"
                        name="min_buyin"
                        width="11.4rem"
                        defaultValue={50}
                        register={register({
                            validate: (value) =>
                                Number(value) < Number(isActive.max_buyin) || 'MIN BUY-IN MUST BE LESS THAN MAX BUY-IN',
                            required: {
                                value: true,
                                message: 'This field is required',
                            },
                            max: {
                                value: 999,
                                message: 'MIN BUY-IN MUST BE BETWEEN 10 & 999 BIG BLINDS',
                            },
                            min: {
                                value: 10,
                                message: 'MIN BUY-IN MUST BE BETWEEN 10 & 999 BIG BLINDS',
                            },
                        })}
                        errorMessage={errors.min_buyin && errors.min_buyin.message}
                    />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Typography
                        style={opacity}
                        component="span"
                        variant="h4"
                        textTransform="uppercase"
                        fontWeight={800}
                        color="yellow"
                        className="title-create-table"
                    >
                        MIN BUY-IN
                    </Typography>
                </div>
                <div className="input-and-title-wrapper">
                    <TextField
                        onFocus={() => setIsClicked(true)}
                        className="input-pulsate"
                        style={opacity}
                        size="medium"
                        tooltipAlign="left"
                        validated={check.time}
                        type="number"
                        name="time"
                        width="11.4rem"
                        defaultValue={30}
                        register={register({
                            required: {
                                value: true,
                                message: 'This field is required',
                            },
                            max: {
                                value: 999,
                                message: 'TIME BANK MUST BE BETWEEN 10 and 999 SECONDS',
                            },
                            min: {
                                value: 10,
                                message: 'TIME BANK MUST BE BETWEEN 10 and 999 SECONDS',
                            },
                        })}
                        errorMessage={errors.time && errors.time.message}
                    />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Rectangle width="3.2rem" height="3.2rem" color="yellow" className="rect-play-input" />
                    <Typography
                        style={opacity}
                        component="span"
                        variant="h4"
                        textTransform="uppercase"
                        fontWeight={800}
                        color="yellow"
                        className="title-create-table"
                    >
                        TIME BANK
                    </Typography>
                </div>
            </div>
            <Rectangle height="large" width="large" color="yellow" />
            <div className="play-button-notification-wrapper">
                <Button
                    variant="secondary"
                    validated={isValidated}
                    className="button-play"
                    title="create"
                    isActionCompleted={isCreated}
                    actionCompletedColor="secondary"
                />
            </div>
            <Notification type="play">{notification}</Notification>
        </BaseForm>
    );
};

export default Form;
