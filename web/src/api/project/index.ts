import { SDK } from '../sdk';

export type Project = {
  id: number;
  name: string;
  avatarUrl: string;
  description: string;
  webUrl: string;
  starCount: number;
  forksCount: number;
  owner: string;
  lastActivity: string;
};

export type ProjectProps = {
  id: number;
};

const mapProject = (item) => {
  console.log('object', item);
  return {
    id: item.id.low_,
    name: item.name,
    avatarUrl:
      item?.avatarUrl ||
      'https://assets.gitlab-static.net/assets/illustrations/golden_tanuki-a88ad492b973a0ea6be2316b12aeb3a76ee4e926b3b217dc26d01a57033c9948.svg',
    description: item?.description || 'Unknown',
    webUrl: item.webUrl,
    starCount: item.starCount.low_,
    forksCount: item.forksCount.low_,
    owner: item?.owner?.name || 'Unknown',
    lastActivity: item?.lastActivityAt?.isoString || 'Unknown',
  };
};

export const getProject = ({ id }: ProjectProps): Promise<Project> => {
  return SDK.getProjectInteractor()
    .getProject(id)
    .then((result) => mapProject(result));
};

export const getProjectsList = (): Promise<Project[]> => {
  return SDK.getProjectInteractor()
    .getProjectsList()
    .then((result) => {
      const data = result[Object.keys(result)[1]].map((item) =>
        mapProject(item),
      );
      return data;
    });
};
