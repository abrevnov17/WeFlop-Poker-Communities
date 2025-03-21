import React, { useState, useContext } from 'react';

import { Range } from 'react-range';
import styled from 'styled-components';
import Button from './Button';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../../../store';
import { SocketContext } from '../../../../providers';

import { bet, check, fold, raise, call } from '../../../../store/actions/table';

const BaseButtonsPanel = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    .range-track {
        height: 0.7rem;
        width: 100%;
        background: ${({ theme }) => theme.palette.background};
        border-radius: 0.35rem;
    }
    .range-thumb {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 1.7rem;
        width: 1.7rem;
        background-color: ${({ theme }) => theme.palette.primary};
        border-radius: 0.4rem;
        outline: none;
        padding: 0.5rem 0.4rem;
    }
    .line-thumb-small {
        display: block;
        width: 0.2rem;
        height: 0.5rem;
        border-radius: 0.1rem;
        background-color: ${({ theme }) => theme.palette.background};
    }
    .line-thumb-big {
        display: block;
        width: 0.3rem;
        height: 0.7rem;
        border-radius: 0.1rem;
        background-color: ${({ theme }) => theme.palette.background};
    }
    .slider-wrapper {
        padding: 0.8rem;
        background-color: ${({ theme }) => theme.palette.secondary};
        width: 21.7rem;
        display: flex;
        align-items: center;
        position: relative;
        &::before {
            content: '';
            display: block;
            background-color: ${({ theme }) => theme.palette.secondary};
            width: 104%;
            position: absolute;
            top: 0.4rem;
            height: 0.4rem;
            left: -0.4rem;
        }
        &::after {
            content: '';
            display: block;
            background-color: ${({ theme }) => theme.palette.secondary};
            width: 104%;
            position: absolute;
            bottom: 0.4rem;
            height: 0.4rem;
            left: -0.4rem;
        }
    }
    .wrapper-slider-buttons {
        display: flex;
    }
    .button-section {
        background-color: ${({ theme }) => theme.palette.background};
        display: flex;
        z-index: 1;
        & > * + * {
            margin-left: 0.5rem;
        }
    }
    .button-top-section {
        padding: 0.7rem 0.5rem;
        margin-bottom: -1.6rem;
        border-radius: 0 0 1.6rem 1.6rem;
    }
    .button-bottom-section {
        padding: 0.7rem 0.2rem;
        margin-top: -1.6rem;
        border-radius: 1.6rem 1.6rem 0 0;
    }
`;

interface ButtonsPanelProps {
    balance: number;
    type: 'bet' | 'call';
}

const ButtonsPanel = ({ balance, type }: ButtonsPanelProps) => {
    const min = 0.5;
    const [rangeValue, setRangeValue] = useState<number[]>([min]);

    const table = useSelector((state: RootState) => state.table);
    const dispatch = useDispatch();
    const { socket } = useContext(SocketContext);

    const buttonPercentHandler = (percents) => {
        setRangeValue([(balance / 100) * percents]);
    };

    const action = (type) => {
        switch (type) {
            case 'bet':
                dispatch(bet({ value: rangeValue[0] }, socket));
                break;
            case 'call':
                dispatch(call({ value: rangeValue[0] }, socket));
                break;
            case 'raise':
                dispatch(raise({ value: rangeValue[0] }, socket));
                break;
            case 'check':
                dispatch(check(socket));
                break;
            case 'fold':
                dispatch(fold(socket));
                break;
        }
        setRangeValue([min]);
    };

    return (
        <BaseButtonsPanel>
            <div className="button-section button-top-section">
                <Button
                    title={type === 'bet' ? '33%' : 'MIN'}
                    size="small"
                    border="left"
                    alignItems="flex-end"
                    justifyContent="flex-end"
                    onClick={() => buttonPercentHandler(33)}
                />
                <Button
                    title={type === 'bet' ? '50%' : '3X'}
                    size="small"
                    alignItems="flex-end"
                    justifyContent="flex-end"
                    onClick={() => buttonPercentHandler(50)}
                />
                <Button
                    title={type === 'bet' ? '66%' : '5X'}
                    size="small"
                    alignItems="flex-end"
                    justifyContent="flex-start"
                    onClick={() => buttonPercentHandler(66)}
                />
                <Button
                    title={type === 'bet' ? '100%' : 'POT'}
                    size="small"
                    border="right"
                    alignItems="flex-end"
                    justifyContent="flex-start"
                    onClick={() => buttonPercentHandler(100)}
                />
            </div>
            <div className="wrapper-slider-buttons">
                <Button
                    title={rangeValue}
                    size="large"
                    border="left"
                    alignItems="center"
                    justifyContent="flex-end"
                    backgroundColor="background"
                    bold
                />
                <div className="slider-wrapper">
                    <Range
                        step={1}
                        min={min}
                        max={balance}
                        values={rangeValue}
                        onChange={(values) => setRangeValue(values)}
                        renderTrack={({ props, children }) => (
                            <div
                                className="range-track"
                                {...props}
                                style={{
                                    ...props.style,
                                }}
                            >
                                {children}
                            </div>
                        )}
                        renderThumb={({ props }) => (
                            <div
                                {...props}
                                style={{
                                    ...props.style,
                                }}
                                className="range-thumb"
                            >
                                <span className="line-thumb-small" />
                                <span className="line-thumb-big" />
                                <span className="line-thumb-small" />
                            </div>
                        )}
                    />
                </div>
                <Button
                    title={type === 'bet' ? 'bet' : 'raise'}
                    size="large"
                    border="right"
                    alignItems="center"
                    justifyContent="flex-start"
                    backgroundColor="secondary"
                    onClick={() => action(type === 'bet' ? 'bet' : 'raise')}
                />
            </div>
            <div className="button-section button-bottom-section">
                <Button
                    size="medium"
                    border="left"
                    title="fold"
                    alignItems="flex-end"
                    justifyContent="flex-end"
                    onClick={() => action('fold')}
                />
                <Button
                    size="medium"
                    border="right"
                    title={type === 'bet' ? 'check' : 'call'}
                    alignItems="flex-end"
                    justifyContent="flex-start"
                    optionalText={type === 'bet' ? undefined : '.50'}
                    onClick={() => action(type === 'bet' ? 'check' : 'call')}
                />
            </div>
        </BaseButtonsPanel>
    );
};

export default ButtonsPanel;
