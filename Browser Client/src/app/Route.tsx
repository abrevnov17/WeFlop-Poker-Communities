import React from 'react';
import { Route as ReactRoute, Redirect } from 'react-router-dom';

interface RouteProps {
    children: React.ReactNode;
    path: string;
    exact: boolean;
    isAuthenticated?: boolean;
    publicRoute?: boolean;
    privateRoute?: boolean;
}

const Route = ({ children, publicRoute, privateRoute, isAuthenticated, ...rest }: RouteProps) => {
	if (publicRoute) {
		if (!isAuthenticated) {
			return <ReactRoute {...rest} render={() => children} />;
		}
		
		return <Redirect to="/updates" />;
	}

	if (privateRoute) {
		if (isAuthenticated) {
			return <ReactRoute {...rest} render={() => children} />;
		}
	}

	return <Redirect to="/" />;
};

export default Route;
