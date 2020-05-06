import { request } from 'lib/request';

import { SDK } from '../sdk';

export type Roles = 'USER' | 'ANON';

export type UserSession = {
  role: Roles;
  avatarUrl: string;
  userName: string;
};

export type AuthProps = {
  privateToken: string;
};

export const logIn = ({ privateToken }: AuthProps): Promise<UserSession> => {
  return SDK.getSessionInteractor()
    .loginOnCustomServer('https://gitlab.com/', privateToken)
    .then(() => {
      const {
        avatarUrl,
        userName,
      } = SDK.getSessionInteractor().getCurrentUserAccount();

      return {
        role: 'USER',
        userName,
        avatarUrl,
      };
    });
};

export type Credentials = {
  login: string;
  password: string;
};

export const getUser = (): Promise<{ payload: UserSession }> =>
  request({
    method: 'get',
    url: '/user/get-user',
  });

export const logOut = (): Promise<void> =>
  request({
    method: 'get',
    url: '/user/logout',
  });
