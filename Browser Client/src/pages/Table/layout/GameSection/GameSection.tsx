// @ts-nocheck
import React, { useState, useEffect } from 'react';

import { Typography } from '../../../../components';
import { Card, Balance } from '../../components';
import { Player } from '../../../../store/reducers/table/types';

import styled from 'styled-components';
import { useTransition, animated } from 'react-spring';
import useMeasure from 'react-use-measure';

interface GameSectionProps {
    totalPot?: number;
    currentPot?: number;
    sidePots?: number[];
    className?: string;
    winner?: Player;
    cards?: string[];
    alignments: {
        top?: number,
        bottom?: number,
        left?: number,
        right?: number
    }[];
}

const GameSection = ({ totalPot, currentPot, sidePots, cards, className, winner, alignments }: GameSectionProps) => {
    const [flipped, setFlipped] = useState([false, false, false, false, false]);
    const [potStatus, setPotStatus] = useState('pot');

    const [winRef, winBounds] = useMeasure();

    useEffect(() => {
        if (winner) {
            winAnimate();
        }
    }, [winner]);

    const winAnimate = async () => {
        await setPotStatus('win');
        await new Promise((resolve) => setTimeout(resolve, 2000));
        await setPotStatus(null);
    };

    const setFlippedHandler = (num) => {
        setFlipped((prev) =>
            prev.map((el, id) => {
                if (id === num) {
                    return !el;
                }
                return el;
            }),
        );
    };

    const cardsAnimation = useTransition(cards, (item, index) => index, {
        from: { transform: 'translateY(-200%)', opacity: '0' },
        enter: { transform: 'translateY(0%)', opacity: '1' },
        leave: { transform: 'translateY(-200%)', opacity: '0' },
        trail: 100,
        config: { duration: 500 },
    });

    const potComponent = useTransition(potStatus, null, {
        from: { opacity: 0, top: '1rem' },
        enter: { opacity: 1 },
        leave: potStatus === null ? { top: '-100%', opacity: 0 } : potStatus === "win" ? { top: "-100%", opacity: 0 } : { opacity: 0 },
        config: { duration: 300 },
    });

    return (
        <div className={className}>
            <div className="wrapper">
                {potComponent.map(({ item, key, props }) =>
                    item === 'pot' ? (
                        <animated.div style={props} key={key} className="total-pot-wrapper">
                            <Typography component="span" variant="h2" color="yellow">
                                {`TOTAL POT: $${totalPot.toFixed(2)}`}
                            </Typography>
                        </animated.div>
                    ) : item === 'win' ? (
                        <animated.div style={props} key={key} className="total-pot-wrapper">
                            <Balance value={totalPot} size="small" className="balance-gutter" ref={winRef} />
                        </animated.div>
                    ) : null,
                )}
                 <div className="wrapper-cards">
                    {cardsAnimation.map(
                        ({ item, key, props }, index) =>
                            <Card style={props} flipped={true} variant={item} className={`card-${index+1}`} />,
                    )}
                    <div className="skeleton card-1" />
                    <div className="skeleton card-2" />
                    <div className="skeleton card-3" />
                    <div className="skeleton card-4" />
                    <div className="skeleton card-5" />
                </div>

                <div className="wrapper-balances">                
                    <Balance value={sidePots.length > 2 && sidePots[2]} size="small" className={`balance-gutter ${(sidePots[2] === 0 || !sidePots[2]) && "disable"}`} />
                    <Balance value={sidePots.length > 0 && sidePots[0]} size="small" className={`balance-gutter ${(sidePots[0] === 0 || !sidePots[0]) && "disable"}`} />
                    <Balance value={currentPot} size="big" className={`balance-gutter ${currentPot === undefined && "disable"}`} />
                    <Balance value={sidePots.length > 1 && sidePots[1]} size="small" className={`balance-gutter ${(sidePots[1] === 0 || !sidePots[1]) && "disable"}`} />
                    <Balance value={sidePots.length > 3 && sidePots[3]} size="small" className={`balance-gutter ${(sidePots[3] === 0 || !sidePots[3]) && "disable"}`} />
                </div>
            </div>
        </div>
    );
};

GameSection.defaultProps = {
    sidePots: [],
    cards: [],
    totalPot: 0
}

export default styled(GameSection)`
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    .wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        position: relative;
    }
    .total-pot-wrapper {
        position: absolute;
        top: 0;
    }

    .wrapper-cards {
        width: 33rem;
        height: 7.9rem;
        margin: 6.4rem 0 1rem 0;
        position: relative;
    }

    .wrapper-balances {
        display: flex;
        align-items: center;
    }

    .balance-gutter {
        margin: 0 0.25rem;
    }

    .disable {
        opacity: 0;
    }

    .card-item {
        width: 5.8rem;
        height: 7.9rem;
    }

    .card-1 {
        left: 0
    }

    .card-2 {
        left: 6.8rem
    }

    .card-3 {
        left: 13.6rem
    }

    .card-4 {
        left: 20.4rem
    }

    .card-5 {
        left: 27.2rem
    }

    .skeleton {
        position: absolute;
        border: 0.3rem solid ${({ theme }) => theme.palette.secondary};
        border-radius: 0.3rem;
        width: 5.8rem;
        height: 7.9rem;
    }
`;
