import React from 'react';
import styled from 'styled-components';

import { Typography, Rectangle } from '../../../../components';

interface CheckboxBaseProps {
    onClick: () => void;
}

const CheckboxBase = styled.div<CheckboxBaseProps>`
    cursor: pointer;
    display: flex;
    align-items: center;
    & + & {
        margin-top: 0.6rem;
    }
    .name-checkbox {
        width: 26rem;
        text-align: right;
    }
    .checkbox-activity {
        width: 4.1rem;
        text-align: left;
    }
    .rect-nav {
        margin: 0 1rem;
    }
`;

interface CheckboxProps {
    active: boolean | undefined;
    title: string;
    onClick: () => void;
}

const Checkbox = ({ active, title, onClick }: CheckboxProps) => {
    return (
        <CheckboxBase onClick={onClick}>
            <Typography
                variant="h4"
                color={active ? 'yellow' : 'initial'}
                component="span"
                fontWeight={700}
                textTransform="uppercase"
                className="name-checkbox"
            >
                {title}
            </Typography>
            <Rectangle
                className="rect-nav"
                border="small"
                color={active ? 'yellow' : 'initial'}
                width="large"
                height="2.4rem"
            />
            <Typography
                variant="h4"
                color={active ? 'yellow' : 'initial'}
                component="span"
                fontWeight={700}
                textTransform="uppercase"
                className="checkbox-activity"
            >
                {active ? 'on' : 'off'}
            </Typography>
        </CheckboxBase>
    );
};

export default Checkbox;
