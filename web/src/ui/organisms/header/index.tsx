import * as React from 'react';
import styled from 'styled-components'
import { GitlabFilled, ImportOutlined } from '@ant-design/icons';
import { Layout, Button } from 'antd'
import { useStore } from 'effector-react';

import { logOut } from 'features/user';
import { $projectsList } from 'features/project'
import { Nav } from 'ui/molecules';
import { Logo, User } from 'ui/atoms';

import { ROUTES } from 'pages/routes'

export const Header = () => {

  const projectsList = useStore($projectsList)

  const navLinks = [
    {
      path: ROUTES.projects.path,
      title: ROUTES.projects.name,
      count: projectsList?.length || 0,
    }
  ]

  return (
    <HeaderContainer>
      <Logo to="/" icon={<GitlabFilled style={{ color: '#de7e00', fontSize: 25 }} />} />
      <Nav data={navLinks} />
      <HeaderProfile />
      <LogoutButton type="primary" onClick={() => logOut()}>
        Logout <ImportOutlined style={{ transform: 'rotate(180deg)' }} />
      </LogoutButton>
    </HeaderContainer>
  );
};

const HeaderContainer = styled(Layout.Header)`
  display: flex;
  align-items: center;
`

const LogoutButton = styled(Button)`
  margin-left: 0; 
`

const HeaderProfile = styled(User)`
  margin-left: auto; 
  align-items: center;
  color: #fff;

  .ant-avatar {
    margin-left: 18px;
  }
`
