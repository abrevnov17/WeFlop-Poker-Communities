import React from 'react';
import styled from 'styled-components';

import { Typography, Line } from '../../components';

interface DescriptionProps {
    className?: string;
    content: any;
}

const Description = ({ content, className }: DescriptionProps) => {
    const contentArray = content.split('\n');

    return contentArray.map((element, index) => {
        return (
            <div className={className} key={`${element}${index}`}>
                <Line width="medium" color="yellow" />
                <div className="desc-wrapper-text">
                    <Typography component="p" variant="body1">
                        {element}
                    </Typography>
                </div>
            </div>
        );
    });
};

export default styled(Description)`
    display: flex;
    margin: 0.8rem 0 0 1.3rem;
    .desc-wrapper-text {
        margin-left: 1.1rem;
    }
`;
