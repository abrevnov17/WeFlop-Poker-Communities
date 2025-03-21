import { createGlobalStyle } from 'styled-components';

interface GlobalStyleProps {
    size: any
}

const GlobalStyle = createGlobalStyle<GlobalStyleProps>`
    html {
        font-size: ${({size}) => size.height! > size.width!/(16/9) ? size.width*0.0065 : size.height*0.0115}px;
    }

    body {
        background-color: ${({ theme }) => theme.palette.background};
        margin: 0;
        box-sizing: border-box;
    }

    .button-form {
    	bottom: -9.6rem;
		margin: 0 -2.5rem 0 -2.5rem;
		position: absolute;
		min-width: 35.2rem;
    }

    .button-play {
        margin: 4.8rem -2.5rem 0 -2.5rem;
        min-width: 28.6rem;
    }

    .d-flex {
        display: flex;
    }

    .justify-content-center {
        justify-content: center;
    }

    .justify-content-start {
        justify-content: flex-start;
    }

    .justify-content-end {
        justify-content: flex-end;
    }

    .align-items-center {
        align-items: center;
    }

    .align-items-start {
        align-items: flex-start;
    }

    .align-items-end {
        align-items: flex-end;
    }
`;

export default GlobalStyle;
