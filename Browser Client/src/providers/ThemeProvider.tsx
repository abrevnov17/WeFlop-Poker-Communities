/* eslint-disable @typescript-eslint/no-unused-vars */
import React, { useState } from 'react';
import { ThemeProvider as StyledProvider } from 'styled-components';
import GlobalStyle from '../app/GlobalStyle';
import { useWindowSize } from '../helpers';

interface ThemeProviderProps {
    children: any;
    mode?: 'light' | 'dark';
}

type Palette = {
    common: Record<string, string>;
    initial: string;
    primary: string;
    secondary: string;
    error: string;
    warning: string;
    success: string;
    text: string;
    background: string;
    yellow: string;
};

type Text = {
    fontFamily?: string;
    fontWeight: number;
    fontSize: string;
    lineHeight?: string;
    letterSpacing?: string;
    textTransform?: string;
    fontStyle?: string;
};

type Animations = {
    line: string;
    text: string;
    inputs: string;
};

type Typography = {
    fontFamily: string;
    fontSize: string;
    h1?: Text;
    h2?: Text;
    h3?: Text;
    h4?: Text;
    h5?: Text;
    h6?: Text;
    title?: Text;
    subtitle1?: Text;
    subtitle2?: Text;
    body1?: Text;
    body2?: Text;
    tooltip?: Text;
    button1?: Text;
    button2?: Text;
    button3?: Text;
    date?: Text;
    input1?: Text;
    input2?: Text;
    input3?: Text;
    display1?: Text;
};

interface Theme {
    palette: Palette;
    typography: Typography;
    animations: Animations;
}

declare module 'styled-components' {
    // eslint-disable-next-line
    export interface DefaultTheme extends Theme {}
}

type ThemeContextType = {
    switchMode?: (themeType: 'dark' | 'light') => void;
    theme: Theme;
};

const defaultLightPalette: Palette = {
    common: {
        darkblue: '#042444',
        blue: '#23578C',
        red: '#8B112F',
        gray: '#E9E9E9',
    },
    initial: '#fff',
    primary: '#23578C',
    secondary: '#8B112F',
    yellow: '#C49D3A',
    error: '#8B112F',
    warning: 'yellow',
    success: '#028561',
    text: '#031D38',
    background: '#E9E9E9',
};

const defaultDarkPalette: Palette = {
    common: {
        darkblue: '#042444',
        blue: '#23578C',
        red: '#8B112F',
        gray: '#E9E9E9',
    },
    initial: '#fff',
    yellow: '#C49D3A',
    primary: '#fff',
    secondary: '#8B112F',
    error: '#8B112F',
    warning: 'yellow',
    success: '#028561',
    text: '#fff',
    background: '#031D38',
};

const defaultTypography: Typography = {
    fontFamily: 'Avenir',
    fontSize: '1.4rem',
    h1: {
        fontWeight: 700,
        fontSize: '3.2rem',
        letterSpacing: '2.86px',
    },
    h2: {
        fontWeight: 700,
        fontSize: '2.4rem',
        letterSpacing: '2.14px',
    },

    // h3
    h3: {
        fontWeight: 400,
        fontSize: '2rem',
        textTransform: 'uppercase',
        letterSpacing: '1.79px',
    },

    //h4
    h4: {
        fontWeight: 400,
        fontSize: '1.8rem',
        letterSpacing: '1.61px',
    },
    /*    playInputTitle: {
        fontWeight: 800,
        textTransform: 'uppercase',
        letterSpacing: '1.61px',
        fontSize: '1.8rem',
    },
    gameSectionTitle: {
        fontWeight: 700,
        textTransform: 'uppercase',
        letterSpacing: '1.61px',
        fontSize: '1.8rem',
    },
    buttonGroupMedium: {
        fontWeight: 400,
        fontSize: '1.8rem',
        textTransform: 'uppercase',
    },*/

    //balanceBig

    //h5
    h5: {
        fontWeight: 400,
        fontSize: '1.4rem',
        letterSpacing: '1.25px',
    },
    /*    tableItemButton: {
        fontWeight: 400,
        fontSize: '1.4rem',
        letterSpacing: '1.25px',
        textTransform: 'uppercase',
    },
    playInput: {
        fontWeight: 600,
        letterSpacing: '1.25px',
        fontSize: '1.4rem',
    },*/

    //h6
    h6: {
        fontWeight: 400,
        fontSize: '1.2rem',
        letterSpacing: '1.07px',
    },
    /*    playTablesMedium: {
        fontWeight: 400,
        fontSize: '1.2rem',
        letterSpacing: '1.07px',
        textTransform: 'lowercase',
    },
    playTablesBold: {
        textTransform: 'uppercase',
        fontWeight: 700,
        fontSize: '1.2rem',
        letterSpacing: '1.07px',
    },
    gameSectionItalic: {
        fontWeight: 400,
        fontSize: '1.2rem',
        letterSpacing: '1.07px',
        fontStyle: 'italic',
        textTransform: 'uppercase',
    },
    balanceSmall: {
        fontWeight: 400,
        fontSize: '1.2rem',
        letterSpacing: '1.07px',
    },*/

    body1: {
        fontWeight: 500,
        fontSize: '1.6rem',
        letterSpacing: '1.43px',
    },
    button1: {
        fontWeight: 600,
        fontSize: '3.2rem',
        letterSpacing: '2.86px',
    },
    button2: {
        fontWeight: 700,
        fontSize: '2.4rem',
        letterSpacing: '2.14px',
    },
    button3: {
        fontWeight: 600,
        fontSize: '2.8rem',
        letterSpacing: '2.5px',
    },
    input1: {
        letterSpacing: '1.43px',
        fontSize: '1.6rem',
        fontWeight: 500,
    },
    input2: {
        fontWeight: 600,
        letterSpacing: '1.25px',
        fontSize: '1.4rem',
    },
    input3: {
        fontWeight: 500,
        letterSpacing: '1.07px',
        fontSize: '1.2rem',
    },
    tooltip: {
        fontSize: '1.2rem',
        fontWeight: 700,
        textTransform: 'uppercase',
        letterSpacing: '1.07px',
    },
    display1: {
        fontSize: '3.2rem',
        fontWeight: 700,
        letterSpacing: '2.86px',
        textTransform: 'uppercase',
    },
    date: {
        fontSize: '1.6rem',
        fontStyle: 'italic',
        letterSpacing: '1.43px',
        fontWeight: 400,
    },
};

const defaultAnimations: Animations = {
    text: 'fadeText 0.4s ease-in-out 0.4s forwards',
    line: 'setHeight 0.4s ease-in-out 0s forwards',
    inputs: 'fadeInputs 5s infinite 0.8s ease-in-out',
};

const themeContextState: ThemeContextType = {
    theme: {
        palette: defaultLightPalette,
        typography: defaultTypography,
        animations: defaultAnimations,
    },
};

export const ThemeContext = React.createContext<ThemeContextType>(themeContextState);

export const ThemeProvider = ({ children, mode }: ThemeProviderProps) => {
    const size = useWindowSize();
    const [themeMode, setThemeMode] = useState<'dark' | 'light'>(mode ? mode : 'light');
    const [lightPalette, setLightPalette] = useState<Palette>(defaultLightPalette);
    const [darkPalette, setDarkPalette] = useState<Palette>(defaultDarkPalette);

    const switchMode = (mode: 'dark' | 'light') => setThemeMode(mode);
    const theme = {
        palette: mode === 'light' ? lightPalette : darkPalette,
        typography: defaultTypography,
        animations: defaultAnimations,
    };

    return (
        <ThemeContext.Provider
            value={{
                switchMode,
                theme,
            }}
        >
            <StyledProvider theme={theme}>
                <GlobalStyle size={size} />
                {children}
            </StyledProvider>
        </ThemeContext.Provider>
    );
};
