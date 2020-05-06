import { Guard } from 'router-guards';

import { UserSession, Roles } from 'api/user';

export function onlyAnon(): Guard<UserSession | null> {
  return (route, context) => (context && context ? null : route);
}

export function onlyUsers(): Guard<UserSession | null> {
  return (route, context) => (context && context ? route : null);
}

export function onlyFor(roles: Roles[]): Guard<UserSession | null> {
  return (route, context) =>
    context && context.role && roles.includes(context.role) ? route : null;
}
