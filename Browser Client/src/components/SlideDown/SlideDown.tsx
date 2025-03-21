import React from 'react';
import { SlideDown as SlideDownBase } from 'react-slidedown';
import 'react-slidedown/lib/slidedown.css';

interface SlideDownProps {
    open: boolean;
    children: React.ReactNode | React.ReactNode[];
}

const SlideDown = ({ open, children }) => {
    return <SlideDownBase className="slidedown-updates">{open ? children : null}</SlideDownBase>;
};

export default SlideDown;
