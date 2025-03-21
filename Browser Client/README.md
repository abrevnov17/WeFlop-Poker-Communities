# We Flop - player-supported online poker platform
![We Flop platform](https://i.imgur.com/g0pqt8S.png)

## Deployment

### Requirements
 - Node >= 8.10 - [https://nodejs.org/en/](https://nodejs.org/en/)
 - npm >= 5.6 - [https://nodejs.org/en/](https://nodejs.org/en/)
 - yarn >= 1.2 - [https://classic.yarnpkg.com/en/docs/install](https://classic.yarnpkg.com/en/docs/install)

### Process
 1. Clone this repository - `git clone https://github.com/abrevnov17/WeFlop-Browser-Client.git`
 2. Open project folder in terminal - `cd WeFlop-Browser-Client`
 3. Install all dependencies using Yarn - `yarn install`
 4. Run the app - `yarn start`

### Other scripts
`yarn run build` - Builds the app for production to the `build` folder.

`yarn run lint` - Runs eslint script to fix the code by eslint rules

## Dependencies
- Typescript = 3.7.2
- React >= 16.13
- React Router >= 5.2
- React Hook Form >= 6.0
- Redux >= 4.0
- React Redux >= 7.2
- Redux Saga >= 1.1
- Redux Persist >= 6.0
- Styled Components >= 5.1
- React Helmet >= 6.1 
- axios >= 0.19

## Folder sctructure
`api` - API requests

`app` - main component with routes, global styles

`components` - base components

`pages` - pages of the app

`providers` - providers based on Context API

`store` - Redux store, also contains `actions`, `reducers` and `sagas` folders to manage Redux 

`assets` - images, fonts, static files, etc

`layout` - components based on base components for creating working and responsive pages

`helpers` - utils that help to check or process something
