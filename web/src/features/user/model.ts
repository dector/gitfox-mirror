import { createStore, createDomain } from 'effector';
import { message } from 'antd';

import * as API from 'api/user';

import { errorCodes } from 'lib/constants';

export const userDomain = createDomain();

export const $user = createStore<API.UserSession | null>(null);
export const $isUserPending = createStore<boolean>(false);

export const logIn = userDomain.createEffect<API.AuthProps, API.UserSession>();
export const logOut = userDomain.createEffect<void, void>();

logIn.use(API.logIn);

$user.on(logIn.done, (_, { result }) => result).on(logOut.done, () => null);

$isUserPending.on(logIn.finally, () => false);

userDomain.onCreateEffect((effect) => {
  $user.on(effect.fail, (user, payload) => {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const err = payload.error as any;

    if (err.status === errorCodes.NOT_AUTHED) {
      return null;
    }

    return user;
  });

  // effect.done.watch(() => {
  //   message.success('Authorization success');
  // });

  effect.fail.watch(() => {
    message.warning('Authorization fail');
  });
});
