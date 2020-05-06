import * as React from 'react';
import { useLocation } from 'react-router-dom';
import { Badge } from 'antd';
import { NavContainer, NavLink, NavMenu, NavItem } from './styles';

type MenuItem = {
  path: string;
  title: string;
  count?: number;
};

type MenuData = {
  data: Array<MenuItem>;
};

export const Nav: React.FC<MenuData> = ({ data }) => {

  const location = useLocation();
  const path = location.pathname
    .split('/')
    .splice(0, 2)
    .join('/')
    .replace('/', '');

  return (
    <NavContainer>
      <NavMenu theme="dark" mode="horizontal" selectedKeys={[path]} style={{ lineHeight: '64px' }}>
        {data.map(({ path, title, count }, key) => (
          <NavItem key={key}>
            <NavLink to={path}>
              {title}
              {count && count > 0 && (
                <>
                  &nbsp;
                  &nbsp;
                  <Badge count={count} style={{ boxShadow: 'none' }} />
                </>
              )}</NavLink>
          </NavItem>
        ))}
      </NavMenu>
    </NavContainer>
  );
};
