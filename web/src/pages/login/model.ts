import { createEvent, sample, guard, forward } from 'effector';
import { createInput, createForm } from 'effector-form';

import { AuthProps } from 'api/user';
import { logIn } from 'features/user';

export const submitForm = createEvent<void>();
export const resetForm = createEvent<void>();

export const loginStore = createInput({
  name: 'privateToken',
  initialValue: 'dLsY5vsuzohLMFMxzWeh',
});

export const loginForm = createForm<AuthProps>({
  name: 'loginForm',
  fields: [loginStore],
  submit: submitForm,
  reset: resetForm,
});

guard({
  source: sample(loginForm.$values, submitForm),
  filter: loginForm.$isValid,
  target: logIn,
});

forward({
  from: logIn.done,
  to: resetForm,
});
