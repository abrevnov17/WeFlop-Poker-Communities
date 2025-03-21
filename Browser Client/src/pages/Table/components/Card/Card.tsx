import React from 'react';
import styled from 'styled-components';
import { isCard } from '../../../../helpers';
import { useSpring, animated } from 'react-spring';

const baseImagePath = '/deck';

interface CardProps {
    className?: string;
    variant?: string;
    color?: string;
    right?: boolean;
    left?: boolean;
    animated?: boolean;
    flipped?: boolean;
    onClick?: any;
    style?: any;
}

interface BaseCardProps {
    path?: string;
    back?: boolean;
    opacity?: any;
    transform?: any;
}


const BaseCard = styled(animated.div)<BaseCardProps>`
    border: 0.3rem solid
        ${({ theme, color }) =>
            !color
                ? theme.palette.secondary
                : theme.palette[color]
                ? theme.palette[color]
                : theme.palette.common[color]};
    border-radius: 0.3rem;
    width: inherit;
    height: inherit;
    position: absolute;
    background: ${({ path, back, theme }) =>
        back ? theme.palette.secondary : path ? `url(${path})` : theme.palette.secondary};
    background-repeat: no-repeat;
    background-size: cover;
    background-position: center;
`;

const Card = React.forwardRef(({ variant, color, flipped, onClick, style, className }: CardProps, ref: any) => {
    const isCardRes = isCard(variant);
    const { opacity, transform, top } = useSpring<any>({
        opacity: flipped ? 1 : 0,
        transform: `perspective(60rem) rotateY(${flipped ? 180 : 0}deg)`,
        config: { mass: 5, tension: 500, friction: 80 },
    });

    return (
        <animated.div className={className} onClick={onClick} style={style} ref={ref}>
            <BaseCard
                color={color}
                style={{ opacity, top, transform: transform.interpolate((t) => `${t} rotateX(180deg)`) }}
                path={isCardRes ? `${baseImagePath}/${variant}.svg` : undefined}
            ></BaseCard>
            {isCardRes && <BaseCard back style={{ opacity: opacity.interpolate((o: any) => 1 - o), transform, top }} />}
        </animated.div>
    );
});

Card.defaultProps = {
    flipped: false,
};

export default styled(Card)`
    position: absolute;
    width: 5.8rem;
    height: 7.9rem;
`;
