import * as React from 'react';
import styled from 'styled-components'
import { useStore } from 'effector-react';
import moment from 'moment';
import { PageHeader, Spin, Avatar, List } from 'antd';
import { StarFilled, ApiFilled } from '@ant-design/icons';

import { $projectsList, getProjectsList } from 'features/project'

export const DashboardPage: React.FC = () => {

  React.useEffect(() => {
    getProjectsList()
  }, [])

  const projectsList = useStore($projectsList)
  const isPending = useStore(getProjectsList.pending)

  return <Spin spinning={isPending}>
    <Container>
      <PageHeader
        title={<div>Projects List</div>}
      />
      <List
        size="large"
        bordered
        dataSource={projectsList || []}
        renderItem={project => (
          <ListItem>
            <a href={project.webUrl}>
              <Avatar src={project.avatarUrl} shape="square" size="large" />
            </a>
            <ListWrap>
              <ListName>{project.owner} / <b>&nbsp;{project.name}</b></ListName>
              <ListDesc dangerouslySetInnerHTML={{ __html: project.description }} />
            </ListWrap>
            <ListAcivity>Last activity was {moment().from(project.lastActivity)}</ListAcivity>
            <ListStats>
              <StarFilled /> {project.starCount}
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <ApiFilled /> {project.forksCount}
            </ListStats>
          </ListItem>
        )}
      />
    </Container>
  </Spin>
}

const Container = styled.section``

const ListItem = styled(List.Item)`
  display: flex;
  justify-content: flex-start;
`;

const ListWrap = styled.div`
   margin: 0 24px;
   max-width: 700px;
   width: 100%;
`;

const ListStats = styled.div`
   margin: 0 24px;
`;

const ListName = styled.div`
  display: flex;
`;

const ListDesc = styled.div`
  display: flex;
`;

const ListAcivity = styled.div`
  margin-left: auto;
`;