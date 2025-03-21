import React from 'react';
import ReactTooltip from 'react-tooltip';
import styled from 'styled-components';
import { useTransition, animated } from 'react-spring';

interface BaseToolTipProps {
    align: 'left' | 'right';
}

interface ToolTipProps {
    message: string;
    name: string | undefined;
    show: boolean | undefined;
    align: 'left' | 'right';
}

const BaseToolTip = styled('div')<BaseToolTipProps>`
    .tooltip {
        width: ${({ align }) => (align !== 'left' ? '40rem' : 'unset')};
        padding: 0;
    }
    .tooltip-img {
        right: ${({ align }) => (align !== 'left' ? '-6.3rem' : null)};
        left: ${({ align }) => (align === 'left' ? '-3.2rem' : null)};
        width: 2.4rem;
        height: 2.4rem;
        position: absolute;
        top: 50%;
        right: -6.3rem;
    }
    .tooltip-text {
        font-size: ${({ theme }) => theme.typography.tooltip!.fontSize};
        font-weight: ${({ theme }) => theme.typography.tooltip!.fontWeight};
        letter-spacing: ${({ theme }) => theme.typography.tooltip!.letterSpacing};
        text-transform: ${({ theme }) => theme.typography.tooltip!.textTransform};
        color: ${({ theme, align }) => (align !== 'left' ? theme.palette.primary : theme.palette.secondary)};
        font-family: ${({ theme }) => theme.typography.fontFamily};
    }
    #img-1 {
        fill: ${({ theme, align }) => (align !== 'left' ? theme.palette.primary : theme.palette.secondary)};
    }
`;

const ToolTip = ({ message, name, show, align }: ToolTipProps) => {
    const tooltipAnimated = useTransition(show, null, {
        from: { opacity: 0, transform: `translate(-1.5rem, ${align === 'left' ? '0.5rem' : '1.4rem'})` },
        enter: { opacity: 1, transform: `transform: translateX(0, ${align === 'left' ? '0.5rem' : '1.4rem'})` },
        leave: { opacity: 0, transform: `translate(-1.5rem, ${align === 'left' ? '0.5rem' : '1.4rem'})` },
    });
    return (
        <BaseToolTip className="base_tooltip" align={align}>
            {tooltipAnimated.map(
                ({ item, key, props }) =>
                    item && (
                        <animated.div style={props}>
                            <svg
                                width="2.4rem"
                                height="2.4rem"
                                viewBox="0 0 24 24"
                                className="tooltip-img"
                                data-tip
                                data-for={`tooltip-${name}`}
                            >
                                <g id="Page-1" stroke="none" strokeWidth="1" fill="none" fillRule="evenodd">
                                    <g id="img-1" transform="translate(-512.000000, -368.000000)" fillRule="nonzero">
                                        <g id="minus-outline" transform="translate(512.000000, 368.000000)">
                                            <path
                                                d="M12,24 C5.372583,24 0,18.627417 0,12 C0,5.372583 5.372583,0 12,0 C18.627417,0 24,5.372583 24,12 C24,18.627417 18.627417,24 12,24 Z M12,21.6 C17.3019336,21.6 21.6,17.3019336 21.6,12 C21.6,6.6980664 17.3019336,2.4 12,2.4 C6.6980664,2.4 2.4,6.6980664 2.4,12 C2.4,17.3019336 6.6980664,21.6 12,21.6 Z M18,10.8 L18,13.2 L6,13.2 L6,10.8 L18,10.8 Z"
                                                id="Shape"
                                            ></path>
                                        </g>
                                    </g>
                                </g>
                            </svg>
                            <ReactTooltip
                                id={`tooltip-${name}`}
                                effect="solid"
                                place={align}
                                border={false}
                                multiline={true}
                                className="tooltip"
                                backgroundColor="transparent"
                            >
                                <span className="tooltip-text">{message}</span>
                            </ReactTooltip>
                        </animated.div>
                    ),
            )}
        </BaseToolTip>
    );
};

export default ToolTip;
