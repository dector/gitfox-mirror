import * as React from 'react';
import { Redirect } from 'react-router-dom';

import { DashboardPage, LoginPage } from 'pages';
import { onlyAnon, onlyUsers } from 'features/user';

export const ROUTES = {
  login: {
    name: 'Login',
    path: '/',
    component: LoginPage,
    guards: [onlyAnon()],
  },
  projects: {
    name: 'Projects',
    path: '/projects',
    component: DashboardPage,
    guards: [onlyUsers()],
  },
  notFoundForUsers: {
    component: () => <Redirect to="/projects" />,
    guards: [onlyUsers()],
    path: '*',
  },
  notFoundForAnon: {
    component: () => <Redirect to="/" />,
    guards: [onlyAnon()],
    path: '*',
  },

};
