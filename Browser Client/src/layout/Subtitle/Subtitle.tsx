import React from 'react';
import { Rectangle, Typography } from '../../components';
import styled from 'styled-components';

interface SubtitleProps {
    children: string;
    className?: string;
    color?: string;
    style?: Record<string, unknown>;
    animated?: boolean;
}

const Subtitle = ({ children, className, color, style, animated }: SubtitleProps) => {
    return (
        <div className={className}>
            <Rectangle height="small" width="small" border="small" color={color ? color : 'yellow'} />
            <div className="subtitles-wrapper" style={style}>
                <Typography component="h3" variant="body1" fontStyle="italic" animated={animated}>
                    {children}
                </Typography>
            </div>
        </div>
    );
};

export default styled(Subtitle)`
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-left: 0.3rem;

    .subtitles-wrapper {
        margin-left: 0.8rem;
    }
`;
