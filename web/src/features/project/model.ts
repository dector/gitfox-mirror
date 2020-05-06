import { createStore, createDomain } from 'effector';

import * as API from 'api/project';

const projectDomain = createDomain();

export const $project = createStore<API.Project | null>(null);
export const $projectsList = createStore<API.Project[] | null>(null);

export const getProject = projectDomain.createEffect<
  API.ProjectProps,
  API.Project
>();

export const getProjectsList = projectDomain.createEffect<
  void,
  API.Project[]
>();

getProject.use(API.getProject);
getProjectsList.use(API.getProjectsList);

$project.on(getProject.done, (_, { result }) => result);
$projectsList.on(getProjectsList.done, (_, { result }) => result);
