import React, { useEffect } from 'react';
import { BrowserRouter, Switch } from 'react-router-dom';
import Route from './Route';
import { ThemeProvider, SocketProvider } from '../providers';
import { Helmet } from 'react-helmet';
import { useSelector } from 'react-redux';
import { RootState } from '../store';

import '../assets/styles/index.scss';
import '../assets/styles/animations.scss';

import { Welcome, SignUp, SignIn, Reset, ResetPassword, Updates, Play, Contact, Table } from '../pages';

const App = () => {
    const application = useSelector((state: RootState) => state.application);

    return (
        <ThemeProvider mode={application.isAuthenticated ? 'dark' : 'light'}>
            <Helmet defaultTitle="We Flop" titleTemplate="We Flop - %s" />
            <BrowserRouter>
                <Switch>
                    <Route path="/" exact publicRoute isAuthenticated={application.isAuthenticated}>
                        <Welcome />
                    </Route>
                    <Route path="/signup" exact publicRoute isAuthenticated={application.isAuthenticated}>
                        <SignUp />
                    </Route>
                    <Route path="/signin" exact publicRoute isAuthenticated={application.isAuthenticated}>
                        <SignIn />
                    </Route>
                    <Route path="/reset/password" exact publicRoute isAuthenticated={application.isAuthenticated}>
                        <Reset />
                    </Route>
                    <Route
                        path="/reset/password/:token"
                        exact
                        publicRoute
                        isAuthenticated={application.isAuthenticated}
                    >
                        <ResetPassword />
                    </Route>
                    <Route path="/updates" exact privateRoute isAuthenticated={application.isAuthenticated}>
                        <Updates />
                    </Route>
                    <Route path="/play" exact privateRoute isAuthenticated={application.isAuthenticated}>
                        <Play />
                    </Route>
                    <Route path="/contact" exact privateRoute isAuthenticated={application.isAuthenticated}>
                        <Contact />
                    </Route>
                    <Route path="/table/:id" exact privateRoute isAuthenticated={application.isAuthenticated}>
                        <SocketProvider>
                            <Table />
                        </SocketProvider>
                    </Route>
                </Switch>
            </BrowserRouter>
        </ThemeProvider>
    );
};

export default App;
